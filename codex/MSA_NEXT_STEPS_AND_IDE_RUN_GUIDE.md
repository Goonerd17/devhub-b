# DevHub Modular Monolith 현재 상태 및 MSA 전환 가이드

## 1. 현재 상태

DevHub는 다음 Gradle 모듈을 가진 물리적 Modular Monolith 구조를 갖추고 있다.

```text
bootstrap
platform
web
identity
member
project
community
administration
media
notification
readmodel
```

각 모듈에는 실제 production source가 존재하며, 대부분의 모듈에 독립적인 `@SpringBootApplication` 진입점과 `bootJar`가 구성되어 있다.

- Root production source: 0
- Root test source: 0
- 전체 테스트: 537건 통과
- 전체 Gradle build: 성공
- 각 도메인 모듈 `bootJar`: 성공
- `member-service.jar` 독립 기동 및 H2/JPA 초기화: 성공
- 데이터베이스: `jdbc:h2:mem:devhub`

다만 현재 모듈은 아직 완전히 독립된 Microservice가 아니다. 일부 모듈은 다른 컨텍스트의 Gradle 구현 모듈과 Spring Bean에 의존하며, 모듈 간 통신도 Java 메서드 호출 방식이다.

## 2. 현재 모듈 의존성의 의미

현재 주요 방향은 다음과 같다.

```text
bootstrap → 모든 모듈
web → 모든 도메인 모듈
identity → member, administration, notification
project → member, administration
community → member, identity
readmodel → administration, member, community, project
```

이는 하나의 JVM에서 실행되는 Modular Monolith 단계에서는 동작하지만, Microservice 추출 시에는 다음과 같이 semantic API 또는 네트워크 계약으로 전환해야 한다.

```text
community → member.api
project → member.api
administration → project.api
```

다른 컨텍스트의 Repository, Entity, Aggregate, `core`, `outbound` 구현을 직접 참조해서는 안 된다.

## 3. MSA 전환 전 우선 작업

### 3.1 모듈 경계 강화

- 각 모듈의 `api`, `core`, `outbound` 패키지 경계를 유지한다.
- `web`에서 도메인 `core` 및 `outbound` 직접 참조를 줄인다.
- 불필요한 Gradle 구현 의존성을 제거한다.
- `scanBasePackages`를 필요한 모듈 범위로 축소한다.
- ArchUnit으로 다음 규칙을 자동 검증한다.

```text
context A → context B.api       허용
context A → context B.core      금지
context A → context B.outbound  금지
```

### 3.2 첫 번째 서비스 추출

초기 후보는 `notification` 또는 `identity`가 적절하다.

`notification`은 이메일/알림 책임이 비교적 명확하고 장애 격리 효과가 크다. `identity`는 인증과 JWT라는 명확한 보안 경계를 갖지만 OAuth callback, 회원 생성, JWT 검증 계약을 함께 정리해야 하므로 난이도가 더 높다.

권장 학습 순서는 다음과 같다.

```text
notification-service
→ identity-service
→ member-service
→ readmodel-service
→ project-service
→ community-service
→ administration-service
```

실제 순서는 추출 시점의 의존성 및 트랜잭션 결합도를 다시 확인한다.

### 3.3 데이터베이스 분리

현재는 모든 모듈이 다음 H2를 사용한다.

```text
jdbc:h2:mem:devhub
```

서비스 추출 시에는 서비스별로 논리적 데이터 소유권을 물리 DB로 옮긴다.

```text
identity-service     → jdbc:h2:mem:identity
member-service       → jdbc:h2:mem:member
notification-service → jdbc:h2:mem:notification
```

DB 분리는 다음 순서로 진행한다.

```text
논리적 데이터 소유권 확정
→ foreign Repository 제거
→ semantic API 계약 확정
→ 서비스 추출
→ 서비스별 H2 분리
→ cross-database join 제거
```

### 3.4 서비스 간 통신

서비스 추출 후 Java 직접 호출은 유지할 수 없다. 즉시 결과가 필요한 기능은 우선 단순한 동기 HTTP API로 전환한다.

Kafka, RabbitMQ, Saga, 분산 트랜잭션은 초기 추출 단계에서 도입하지 않는다. 비동기 이벤트가 실제로 필요한 시점에 별도 설계한다.

## 4. Eureka와 Gateway 도입 순서

최소 2개의 서비스가 독립 실행되고 DB와 API 경계가 분리된 후 도입한다.

```text
notification-service 독립화
        ↓
identity-service 독립화
        ↓
Eureka Server 추가
        ↓
각 서비스 Eureka Client 등록
        ↓
Spring Cloud Gateway 추가
        ↓
외부 요청을 Gateway로 통합
```

예상 라우팅은 다음과 같다.

```text
/auth/**      → identity-service
/members/**   → member-service
/projects/**  → project-service
/community/** → community-service
/admin/**     → administration-service
```

OAuth callback은 다음 흐름으로 유지하는 것이 적절하다.

```text
Client → Gateway → identity-service OAuth callback
```

Gateway 도입 전에 다음을 결정해야 한다.

- JWT를 Gateway와 각 서비스 중 어디에서 검증할지
- 서비스가 JWT를 자체 검증할지
- 내부 서비스 호출 인증 방식
- CORS 처리 위치
- timeout/retry 정책
- OAuth callback 라우팅

## 5. IntelliJ IDEA에서 모듈별 실행

### 5.1 Gradle 프로젝트 새로고침

1. IntelliJ IDEA에서 `C:\Users\PC\devhub-b`를 연다.
2. 오른쪽 Gradle 창에서 프로젝트를 새로고침한다.
3. `settings.gradle`에 다음 프로젝트가 표시되는지 확인한다.

```text
bootstrap, platform, web, identity, member, project,
community, administration, media, notification, readmodel
```

### 5.2 Application 실행 구성 생성

1. 실행할 모듈의 main 클래스를 연다.
2. 클래스 옆의 초록색 실행 아이콘을 클릭한다.
3. `Run '<ApplicationName>.main()'`을 선택한다.
4. 실행 구성에서 다음을 확인한다.

   - Main class: 해당 모듈의 `*Application`
   - Use classpath of module: 해당 모듈
   - JDK: Java 17 이상
   - Active profile: `local` 필요 시 지정
   - VM options 또는 Environment variables: OAuth/메일 값은 로컬 환경변수로 주입

주요 main 클래스는 다음과 같다.

| 모듈 | Main class |
|---|---|
| bootstrap | `teamdevhub.devhub.DevhubApplication` |
| platform | `teamdevhub.devhub.platform.PlatformApplication` |
| web | `teamdevhub.devhub.web.WebApplication` |
| identity | `teamdevhub.devhub.identity.IdentityApplication` |
| member | `teamdevhub.devhub.member.MemberApplication` |
| project | `teamdevhub.devhub.project.ProjectApplication` |
| community | `teamdevhub.devhub.community.CommunityApplication` |
| administration | `teamdevhub.devhub.administration.AdministrationApplication` |
| media | `teamdevhub.devhub.media.MediaApplication` |
| notification | `teamdevhub.devhub.notification.NotificationApplication` |
| readmodel | `teamdevhub.devhub.readmodel.ReadmodelApplication` |

### 5.3 IntelliJ에서 실행 시 권장 설정

현재 각 모듈의 기본 설정은 H2 메모리 데이터베이스를 사용하고 포트를 자동 할당한다.

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devhub
server:
  port: 0
```

따라서 여러 모듈을 동시에 실행해도 포트 충돌 가능성이 낮다. API를 직접 호출하려면 실행 로그에서 할당된 포트를 확인하거나 실행 구성마다 고정 포트를 지정한다.

예:

```text
identity: 8081
member: 8082
notification: 8083
```

IntelliJ의 Run Configuration에서 `Program arguments`에 다음을 지정할 수 있다.

```text
--server.port=8082
--spring.profiles.active=local
```

### 5.4 환경변수 설정

OAuth client id/secret, JWT secret, 메일 설정은 Java나 Gradle 파일에 작성하지 않는다. IntelliJ 실행 구성의 `Environment variables`에 로컬 값을 넣거나 기존 `application-local.yml`을 사용한다.

예시 형식:

```text
OAUTH_GITHUB_CLIENT_ID=...
OAUTH_GITHUB_CLIENT_SECRET=...
JWT_SECRET=...
```

실제 프로젝트의 property 이름은 `application-local.yml`과 각 `@Value`/`@ConfigurationProperties` 선언을 기준으로 맞춘다.

## 6. Gradle 명령으로 모듈 실행

IntelliJ 외에도 다음과 같이 실행할 수 있다.

```powershell
.\gradlew.bat :member:bootRun --args="--server.port=8082"
.\gradlew.bat :identity:bootRun --args="--server.port=8081 --spring.profiles.active=local"
.\gradlew.bat :notification:bootRun --args="--server.port=8083"
```

패키징 후에는 다음과 같이 실행한다.

```powershell
java -jar member/build/libs/member-service.jar --server.port=8082
java -jar identity/build/libs/identity-service.jar --server.port=8081
```

## 7. 현재 단계의 결론

현재 구조는 **독립 실행 진입점을 가진 Modular Monolith**이다. 다음 목표는 곧바로 모든 모듈을 서비스화하는 것이 아니라, `notification` 또는 `identity`를 선택해 데이터·설정·통신까지 실제로 독립시키는 것이다.

그 이후 Eureka와 Gateway를 도입하면 서비스 위치 탐색과 외부 진입점을 통합할 수 있다. Gateway/Eureka를 먼저 추가하면 현재의 Gradle 및 shared H2 결합을 숨기는 분산 모놀리스가 될 위험이 있으므로, 서비스 하나 이상의 실제 추출 이후 진행하는 것이 안전하다.
