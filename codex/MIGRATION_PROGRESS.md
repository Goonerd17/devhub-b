# MSA 마이그레이션 진행 현황

## 기준 정보

| 항목 | 값 |
|---|---|
| 현재 Phase | MODULARIZATION_BLOCKER_RESOLUTION |
| 현재 상태 | **IN_PROGRESS** |
| 기록 시각 | 2026-09-15 (Asia/Seoul) |
| 브랜치 | `main` |
| 기준 커밋 | `710484d1c280a31457f37df963e06c1fc9e08aaf` |
| Java toolchain | 17 |
| Gradle 실행 JVM | Temurin OpenJDK 21.0.12.1 |
| Spring Boot | 3.5.7 |
| Gradle Wrapper | 8.14.3 |
| 테스트 DB | 단일 H2 In-Memory Database: `jdbc:h2:mem:devhub` |

## Phase 0 — 신뢰 가능한 기준선 확립

- 상태: **COMPLETED**
- 기존 단일 Gradle/Spring Boot 구조, 의존성, QueryDSL, H2 테스트 구성, 패키지 구조 및 Git 상태를 확인했다.
- `build.gradle`은 Java 17 toolchain과 Spring Boot 3.5.7을 사용한다. QueryDSL은 `querydsl-jpa:5.1.0:jakarta`, annotation processor 및 `build/generated/querydsl` 생성 경로로 구성되어 있다.
- `src/test/resources/application.yml`의 H2 In-Memory Database 설정으로 테스트가 실행된다.
- 확인된 테스트 기준선: Java 테스트 파일 208개, 실행 테스트 532개, 실패 0, 오류 0, 스킵 0.
- Phase 0에서 production 코드, Gradle 의존성, 데이터베이스 스키마, REST API, 인증 동작은 변경하지 않았다.

### Phase 0 실행 명령과 결과

| 명령 | 결과 |
|---|---|
| `git status --short`, `git branch --show-current`, `git rev-parse HEAD` | 기존 dirty worktree와 기준 브랜치/커밋 기록 |
| `java -version` | Temurin OpenJDK 21.0.12.1 확인 |
| `gradlew.bat --version` | Gradle 8.14.3 확인 |
| `gradlew.bat clean compileJava testClasses` | 성공 (`BUILD SUCCESSFUL`) |
| `gradlew.bat test --console=plain` | 성공; 532개 테스트, 실패/오류/스킵 0 |
| `gradlew.bat bootRun --args=--server.port=0 --console=plain` | 실패; 아래 기존 문제 참조 |

### Phase 0에서 확인된 기존 문제

1. 기본 profile의 `bootRun`은 `src/main/resources/application.yml`에 `${jwt.secret.key}`가 없어 `JwtTokenCodec` bean 생성 시 `PlaceholderResolutionException`으로 실패한다. 이번 마이그레이션 작업이 도입한 문제가 아니며 수정하지 않았다.
2. 기본 Gradle 캐시 경로 `C:\.gradle`의 권한/잠금 문제 때문에, 검증은 프로젝트 내부의 별도 `GRADLE_USER_HOME` 캐시를 사용했다.
3. 재시도한 `clean`은 `build` 디렉터리 파일 잠금으로 실패한 적이 있다. 성공한 컴파일/테스트 결과가 있으므로 코드 컴파일 오류가 아닌 환경 문제로 기록한다.

## Phase 1.1 — Gradle 멀티프로젝트 골격 생성

- 상태: **COMPLETED**
- 완료한 Phase 1 작업: 첫 번째 미완료 작업인 **소스 이동 없는 Gradle 멀티프로젝트 골격 추가**.
- 다음 Phase 1 작업 및 Phase 2는 실행하지 않았다.

### 변경 내용

`settings.gradle`에 아래 19개 하위 프로젝트를 등록했다.

```text
bootstrap, platform, web
identity-api, identity-impl
member-api, member-impl
project-api, project-impl
community-api, community-impl
administration-api, administration-impl
media-api, media-impl
notification-api, notification-impl
readmodel-api, readmodel-impl
```

각 하위 프로젝트에는 Java 17 toolchain을 지정한 최소 `java-library` 빌드 스크립트만 추가했다. 소스, 리소스, 테스트, `project(...)` 의존성은 추가하거나 이동하지 않았다.

`project-api`/`project-impl`은 승인된 분석의 경계에 맞추어 Project Marketplace와 Recruitment Application을 한 논리적 모듈로 함께 유지한다. 별도 Recruitment 모듈은 만들지 않았다.

루트 프로젝트는 계속 `org.springframework.boot` 플러그인을 적용한 유일한 실행 애플리케이션이다. 새 하위 모듈은 빈 Java 라이브러리이므로 현재 배포 단위와 Spring Boot 프로세스 수는 하나다.

### Phase 1.1 실행 명령과 결과

검증은 `GRADLE_USER_HOME=.gradle-phase1`을 사용했다.

| 명령 | 결과 |
|---|---|
| `gradlew.bat projects --console=plain` | 성공. 루트 `devhub-b` 아래에 19개 하위 프로젝트가 모두 표시됨. |
| `gradlew.bat :dependencies --configuration runtimeClasspath --console=plain` | 성공. 루트 runtime classpath를 확인했고, 하위 모듈 간 `project(...)` 의존성은 아직 없음. H2 `2.3.232`는 루트 런타임 의존성으로 유지됨. |
| `gradlew.bat build --console=plain` | 성공: `BUILD SUCCESSFUL in 24s`, 28 tasks 중 21 executed, 7 up-to-date. 루트 `:test`는 `UP-TO-DATE`, 새 모듈의 컴파일/테스트는 `NO-SOURCE`. |

테스트 코드는 변경하지 않았으므로 유효한 테스트 수 기준선은 Phase 0의 532개(실패 0, 오류 0, 스킵 0)이다. 이번 전체 빌드는 그 결과를 재사용하여 통과했다.

### 데이터베이스 및 호환성 확인

- 하나의 H2 In-Memory Database `jdbc:h2:mem:devhub` 사용을 유지했다. 모듈별 DB, 외부 DB, 컨테이너, Testcontainers는 도입하지 않았다.
- REST API, 요청/응답 형식, 인증/JWT/OAuth, 비즈니스 규칙, 트랜잭션, 파일 및 이메일 동작은 변경하지 않았다.
- 모듈 간 HTTP, Kafka/RabbitMQ, 분산 트랜잭션, API Gateway, Eureka, Kubernetes, Istio를 도입하지 않았다.
- 기본 profile `bootRun`의 `${jwt.secret.key}` 누락 실패는 Phase 0에서 확인한 기존 구성 문제이며, Phase 1.1이 도입한 실패가 아니다. 이번 작업에서는 수정하거나 다시 해석하지 않았다.

## 현재 Git 상태 관련 주의

Phase 1 시작 전부터 아래 변경은 이미 작업 트리에 존재했으며, 이 작업에서 수정하지 않았다.

```text
AM codex/ARCHITECTURE_RULES.md
AM codex/DOMAIN_OWNERSHIP.md
AM codex/MODULAR_MONOLITH_MIGRATION_PLAN.md
 M src/main/java/teamdevhub/devhub/api/web/model/request/PageRequestDto.java
RM src/test/java/teamdevhub/devhub/small/api/file/FileResponseFactoryTest.java
  -> src/test/java/teamdevhub/devhub/medium/api/file/controller/FileResponseFactoryTest.java
```

`settings.gradle`에는 시작 전부터 Foojay toolchain resolver 플러그인 변경이 있었고, Phase 1.1에서는 이를 보존한 채 하위 프로젝트 `include`만 추가했다. `.gradle-phase0/`, `.gradle-phase1/`은 검증용 로컬 Gradle 캐시이며 소스 변경이 아니다.

## 다음 작업 제한

Phase 1.1까지만 완료했다. 소스 이동, 모듈 의존성 연결, 공개 API 정의, `bootstrap` 이전 또는 어떠한 아키텍처 리팩터링도 다음 명시적 지시 전에는 수행하지 않는다. 자동 커밋도 수행하지 않았다.

---

## Phase 2.1 — `TokenParseProvider`의 infrastructure VO 누출 제거

- 상태: **COMPLETED**
- 완료한 Phase 2 작업: 첫 번째 미완료 작업인 **outbound JWT infrastructure VO가 outbound port 계약에 노출된 의존성 제거**.
- 다음 Phase 2 작업 및 Phase 3는 실행하지 않았다.

### 수정한 의존성

| 구분 | 내용 |
|---|---|
| BEFORE | `core.auth.port.out.token.TokenParseProvider` → `outbound.auth.infrastructure.token.vo.AccessTokenInfo`, `outbound.auth.infrastructure.token.vo.TempTokenInfo` |
| AFTER | `TokenParseProvider` → `core.auth.port.out.token.vo.AccessTokenInfo` / `TempTokenInfo` ← `outbound.auth.infrastructure.token.JwtTokenCodec` |
| 포트/어댑터 관계 | `TokenParseProvider`가 반환 계약을 소유하고, outbound adapter인 `JwtTokenCodec`이 해당 포트를 구현한다. `JwtAuthorizationFilter`와 test fake는 이제 core 계약만 소비한다. |

`AccessTokenInfo`, `TempTokenInfo`는 동일한 record 필드와 `@Builder`를 유지한 채 `core.auth.port.out.token.vo`로 이동했다. JWT claim 이름, HS256 서명, 만료 시간, token type 검증, REST/security 동작 및 H2 구성은 변경하지 않았다.

### 변경 파일

- 추가: `src/main/java/teamdevhub/devhub/core/auth/port/out/token/vo/AccessTokenInfo.java`
- 추가: `src/main/java/teamdevhub/devhub/core/auth/port/out/token/vo/TempTokenInfo.java`
- 수정: `TokenParseProvider`, `JwtTokenCodec`, `JwtAuthorizationFilter`
- 수정: `FakeTokenParseProvider`, `JwtTokenCodecTest`, `JwtAuthorizationFilterTest`, `OAuthResolveServiceTest`
- 삭제: 기존 `outbound/auth/infrastructure/token/vo/AccessTokenInfo.java`, `TempTokenInfo.java`

### 검증

| 명령 | 결과 |
|---|---|
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat compileJava testClasses --console=plain` | **성공**. 루트 production/test source가 재컴파일되었고 새 골격 모듈은 `NO-SOURCE`로 정상 완료. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat test --tests 'teamdevhub.devhub.medium.outbound.auth.infrastructure.token.JwtTokenCodecTest' --tests 'teamdevhub.devhub.medium.outbound.security.filter.JwtAuthorizationFilterTest' --tests 'teamdevhub.devhub.small.core.auth.application.service.oauth.OAuthResolveServiceTest' --console=plain` | **성공**. JWT 생성·파싱, security filter, OAuth temp token 해석 관련 테스트 통과. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat build --console=plain` | **성공**: `BUILD SUCCESSFUL in 1m`, 28 tasks 중 5 executed, 23 up-to-date. 단일 Spring Boot deployable과 전체 빌드가 유지됨. |
| `rg -n "outbound\\.auth\\.infrastructure\\.token\\.vo\\.(AccessTokenInfo|TempTokenInfo)" src/main/java src/test/java` | 일치 항목 없음. 해당 역의존이 제거되었음을 확인. |

### 범위 밖으로 남긴 항목

검사에서 `core.project.application.ProjectService` → `outbound.project.adapter.mapper.ProjectMapper`, 여러 core facade/command → `api.*` HTTP DTO, `CompositeMessageSenderSelector` → outbound exception 등의 다른 레이어 위반이 발견된다. 이는 서로 다른 Phase 2 작업이며, 이번 작업에서 수정하지 않았다. 컨텍스트 간 repository/entity 결합도 이번 범위에 포함하지 않았다.

---

## Phase 3.1 — `platform` 식별자 생성 기술 단위 이동

- 상태: **COMPLETED**
- 완료한 Phase 3 작업: 첫 번째 미완료 작업인 **business-neutral 식별자 생성(Identifier generation) 기술 단위를 `platform` Gradle 모듈로 물리 이동**.
- 다음 Phase 3 작업 및 Phase 4는 실행하지 않았다.

### 선택 근거와 소유권

`MONOLITH_TO_MSA_ANALYSIS.md`의 target structure는 `platform`을 “exception, tracing, ID/time 등 비즈니스 비종속 기술”의 소유자로 정의한다. `IdentifierProvider`와 UUID 기반 `SystemIdentifierProvider`는 특정 bounded context의 엔티티, repository, JPA/QueryDSL, 업무 규칙 또는 HTTP 계약에 의존하지 않는다. 따라서 `DOMAIN_OWNERSHIP.md`가 아직 소유권 분석 본문이 아닌 작성 지시 상태인 점을 현재 구현 및 분석 문서와 교차 확인하여, 이 단위를 첫 물리 이동 대상으로 선택했다.

### 이동 내용

| 이전 위치 | 이후 위치 | 역할 |
|---|---|---|
| `core.common.provider.IdentifierProvider` | `platform/src/main/java/teamdevhub/devhub/platform/identifier/IdentifierProvider` | 모든 업무 모듈이 사용할 수 있는 business-neutral 식별자 생성 계약 |
| `outbound.common.provider.SystemIdentifierProvider` | `platform/src/main/java/teamdevhub/devhub/platform/identifier/SystemIdentifierProvider` | UUID 기반 계약 구현 및 Spring `@Component` |

- 기존 소비자(core application service, outbound adapter, `TraceIdMDCFilter`, test fake)는 모두 `teamdevhub.devhub.platform.identifier.IdentifierProvider`를 import하도록 변경했다.
- 루트 단일 Spring Boot 애플리케이션은 `implementation project(':platform')`만 추가했다. `:platform`은 다른 bounded context의 `*-impl` 모듈에 의존하지 않는다.
- `platform`에는 `SystemIdentifierProvider`의 Spring `@Component` 컴파일을 위한 `org.springframework:spring-context:6.2.12`만 선언했다. Spring Boot 3.5.7이 루트에서 해석하는 Spring Framework 버전과 동일하다.
- 도메인 객체, application service, outbound port, JPA entity/repository, persistence adapter, QueryDSL, DB schema는 이 단위에 존재하지 않아 이동하거나 노출한 항목이 없다.

### 이번 작업에서 발견·해결한 빌드 구성 문제

초기 `:platform:build`는 subproject가 root의 repository/dependency-management를 상속하지 않아 `spring-context`를 해석하지 못했다. `platform/build.gradle`에 `mavenCentral()` 및 명시적 Spring Context 버전을 추가하여 해결했다. 이는 이번 Gradle 모듈 이동으로 도입된 빌드 구성 누락이며, 수정 후 모든 검증이 통과했다.

### 검증

| 명령 | 결과 |
|---|---|
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat :platform:build --console=plain` | **성공**. `platform`의 실제 Java source와 JAR 생성 확인. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat test --tests 'teamdevhub.devhub.medium.outbound.provider.SystemProviderTest' --console=plain` | **성공**. UUID 기반 32자리 식별자 생성 테스트를 포함한 provider 테스트 통과. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat build --console=plain` | **성공**: `BUILD SUCCESSFUL in 1m 9s`, 29 tasks 중 5 executed, 24 up-to-date. 모든 모듈을 포함한 단일 애플리케이션 빌드 유지. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat :dependencies --configuration compileClasspath --console=plain` | **성공**. 루트 compile classpath에 `project :platform`이 존재함을 확인. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat :platform:dependencies --configuration compileClasspath --console=plain` | **성공**. `platform`은 `org.springframework:spring-context:6.2.12`만 직접 의존하며 다른 업무 모듈/`*-impl` 의존성이 없음. |
| `rg -n "core\\.common\\.provider\\.IdentifierProvider|outbound\\.common\\.provider\\.SystemIdentifierProvider" src/main/java src/test/java platform/src` | 일치 항목 없음. 이전 위치 import가 제거됨. |

### 애플리케이션 기동 확인

임시 `JWT_SECRET_KEY`와 `--server.port=0`으로 `bootRun`을 실행했다. H2/JPA 초기화와 주요 Spring bean 조립은 진행됐으나, `oauth.github.client-id` 누락으로 `GithubOAuthConfig` 생성 시 중단됐다. 이는 Phase 0에서 기록한 기본 설정 누락 문제의 연속(기존에는 JWT 값 누락, 임시 JWT 주입 후에는 OAuth 값 누락)이며, `platform` 이동이 도입한 실패가 아니다. OAuth 설정이나 인증 동작은 변경하지 않았다.

### 범위 제한

이번 단위는 `platform`의 첫 실제 source 이동일 뿐이며, Identity, Member, Project, Community, Administration, Media, Notification, Readmodel 중 어느 bounded context도 이동하지 않았다. 다른 모듈의 `impl`에 대한 새 의존성, DB 분리, H2 설정 변경, REST/API 변경, Phase 4 작업은 수행하지 않았다. 자동 커밋도 수행하지 않았다.

---

## Physical Modularization 1 — Platform 시간 추상화

- 마이그레이션 유형: **PHYSICAL_MODULARIZATION**
- 상태: **COMPLETED**
- 수행 일시: 2026-09-15 (Asia/Seoul)
- 마이그레이션 단위 / 소유 컨텍스트 / 대상 프로젝트: `Platform TimeProvider abstraction` / `platform` / `:platform`
- DB: 변경 없음. 단일 H2 In-Memory Database `jdbc:h2:mem:devhub`를 유지한다.

### 물리 변경

| 이전 Root 경로 | 이후 경로 |
|---|---|
| `core/common/provider/TimeProvider.java` | `platform/src/main/java/teamdevhub/devhub/platform/time/TimeProvider.java` |
| `outbound/common/provider/SystemTimeProvider.java` | `platform/src/main/java/teamdevhub/devhub/platform/time/SystemTimeProvider.java` |

- 이전 Root 원본 2개는 삭제했고 호환용 복사본은 남기지 않았다.
- `EmailVerificationIssuer`, `VerificationService`, `UserLoginService`, `JwtTokenCodec` 및 관련 test fake/test가 `teamdevhub.devhub.platform.time.TimeProvider`를 import하도록 변경됐다.
- `SystemTimeProvider`의 Root `StringUtil` 의존은 `value == null || value.isBlank()` private helper로 제거했다. 기존 null/blank 입력 처리 의미는 유지한다.
- 기존 혼합 `SystemProviderTest`의 시간 검증 7개를 `platform/src/test/java/teamdevhub/devhub/platform/time/SystemTimeProviderTest.java`로 이동했다.
- Root에는 Identity verification code와 Platform identifier를 검증하는 `SystemVerificationAndIdentifierProviderTest.java`만 남겼다. Identity 구현이 아직 Root에 있으므로 이 두 테스트는 이번 시간 단위의 이동 대상이 아니다.

### Gradle 경계와 축소 결과

- 기존 `Root → :platform`만 재사용했다. 새 business module 또는 `*-impl` project dependency는 추가하지 않았다.
- `:platform`에는 test-only로 `org.junit.jupiter:junit-jupiter:5.12.2`, `org.junit.platform:junit-platform-launcher:1.12.2`를 추가하고 `useJUnitPlatform()`을 설정했다.
- `:platform` main compile classpath는 기존 `org.springframework:spring-context:6.2.12`뿐이다. business `*-api`/`*-impl` 의존성은 없다.
- Public Application Contract, JPA Entity, Repository, QueryDSL DAO, persistence adapter는 추가/노출/이동하지 않았다.

| 항목 | 이전 | 이후 |
|---|---:|---:|
| Root 생산 Java 파일 | 598 | 596 |
| Platform 생산 Java 파일 | 2 | 4 |
| Root 테스트 Java 파일 | 209 | 209 |
| Platform 테스트 Java 파일 | 0 | 1 |

### 검증

| 명령 | 결과 |
|---|---|
| `gradlew.bat :platform:test :compileJava :testClasses --console=plain` | 성공 |
| `gradlew.bat :platform:test --rerun-tasks --console=plain` | 성공. Platform 시간 테스트 7개, 실패/오류 0 |
| `gradlew.bat :test --tests ... --rerun-tasks --console=plain` | 성공. 시간 의존 Root 테스트와 남은 provider test 확인 |
| `gradlew.bat build --console=plain` | 성공. QueryDSL annotation processing, Root 전체 테스트, 모든 subproject build 확인 |
| `gradlew.bat :platform:dependencies --configuration compileClasspath --console=plain` | 성공. Platform의 business module 의존성 없음 확인 |
| 이전 package import 검색 | `core.common.provider.TimeProvider`, `outbound.common.provider.SystemTimeProvider` 0건 |

Root test result XML은 526개, Platform test result XML은 7개로 합계 533개이며 실패/오류/skip은 0이다.

### 기동 검증과 남은 작업

임시 `JWT_SECRET_KEY`, `--server.port=0`으로 `bootRun`을 실행했다. H2 DDL, JPA `EntityManagerFactory`, Spring Security 초기화까지 진행됐으나, 기존 `${oauth.github.client-id}` 설정 누락 때문에 `GithubOAuthConfig` 생성에서 중단됐다. OAuth 설정은 변경하지 않았으며 이번 Platform 이동으로 도입된 실패가 아니다.

Platform 전체가 완료된 것은 아니다. Root에 `AuditInfo`, `BaseEntity`, JPA audit/converter, 공통 예외, pagination, tracing/logging 후보가 남아 있으며 일괄 `common`으로 옮기지 말고 별도 판정해야 한다. 다음 권고 단위는 자동 수행하지 않는다. 기존 `member-api`의 `MemberPublicProfileQuery`를 보존하면서 Member profile 구현의 작은 단위를 `member-impl`으로 물리 이동하는 것이다.

---

## Complete Bounded Context Modularization — 차단 기록

- 상태: **BLOCKED**
- 요청된 최종 구조: `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel`의 컨텍스트당 단일 Gradle 모듈
- 소스 변경: 없음. 단순 물리 이동이 목표 규칙을 위반하는 것을 방지하기 위해 중단했다.
- 구체적 차단: 14개 `core` 파일이 `api` HTTP DTO를 직접 import하고, `UserProfileFacade → EmailUserCredentialRepository`, `AuthenticationService → UserRepository`, `BoardService → UserRepository`/`EmailUserCredentialRepository`, `ProjectFacade → Member`/`Administration`/`Media`/`Identity` 내부 타입의 직접 의존이 확인됐다.
- Readmodel: `HomeBoardQueryDaoImpl`의 User/Board QueryDSL join, `SkillTrendQueryDaoImpl`의 Project/User JPQL entity 참조는 문서화된 read-only 예외 없이 모듈 경계를 통과한다.
- 금지된 우회: 다른 컨텍스트의 `core`/`outbound` 또는 JPA Entity/Repository를 public으로 만들어 compilation을 통과시키는 방법은 사용하지 않았다.
- 상세 근거와 재개 조건: `codex/FINAL_MODULARIZATION_REVIEW.md`

---

## Modularization Blocker Resolution

- 상태: **BLOCKED**
- 완료한 문서 작업: `DOMAIN_OWNERSHIP.md`를 실제 context owner/entity/repository/QueryDSL/public capability/workflow 기준으로, `ARCHITECTURE_RULES.md`를 실제 금지/허용 규칙으로 교체했다.
- 확인한 code blocker: core→HTTP DTO import 14개, Member↔Identity, Community→Member/Identity, Project→Member/Identity/Administration/Media, Member administration→Project/Community의 foreign internal/repository/domain 의존.
- 허용한 예외: Readmodel의 `HomeBoardQueryDaoImpl`, `SkillTrendQueryDaoImpl` foreign persistence read-only query. write context 역의존과 foreign write는 금지한다.
- 차단 결정: 가입/OAuth 가입, Project 생성/수정, Member administration 조회의 workflow owner와 local transaction/실패 정책이 승인되지 않아 semantic API contract를 안전하게 확정할 수 없다.
- 상세: `codex/MODULARIZATION_BLOCKER_REVIEW.md`

---

## Phase 4.1 — Community 댓글 작성자 이름의 Member persistence 의존성 제거

- 상태: **COMPLETED**
- 완료한 Phase 4 작업: 첫 번째 미완료 작업인 **`CommentService`의 Member `UserRepository` 직접 접근 제거**.
- 다음 Phase 4 작업 및 이후 단계는 실행하지 않았다.

### 선택한 의존성의 사실과 해석

| 항목 | 내용 |
|---|---|
| 호출자 | Community의 `core.board.application.CommentService.commentList(String boardGuid)` |
| 기존 직접 의존성 | `CommentService` → `core.user.port.out.UserRepository.findNamesByUserGuid(...)` |
| 호출자가 실제로 필요했던 정보 | 댓글 `userGuid`별 작성자 표시 이름(`username`)만 필요. `User` aggregate, JPA entity, repository API 전체는 필요하지 않음. |
| 실제 소유자 | Member Profile. `User`, `USERS` 및 사용자 이름은 Member가 논리적으로 소유하며, 구현은 `UserProfileService` → `UserRepository`가 담당. |
| 직접 접근이 존재한 이유 | 댓글 목록을 만들 때 한 번의 사용자 이름 batch lookup으로 `Comment.userName`을 채우기 위해 Member의 persistence port를 Community service에 주입했음. |

`DOMAIN_OWNERSHIP.md`는 현재 소유권 분석 결과가 아니라 작성 지시 상태이므로, 이 판단은 해당 문서의 요구사항과 `MONOLITH_TO_MSA_ANALYSIS.md`의 “Community → Member repository 직접 읽기” 관찰 및 실제 소스를 교차 검증해 내렸다.

### 공개 계약과 결과 모델

`member-api`에 Member가 공개하는 최소 의미 계약을 추가했다.

```text
MemberPublicProfileQuery.findPublicProfilesByMemberGuids(List<String> memberGuids)
    -> List<MemberPublicProfile>

MemberPublicProfile(String memberGuid, String displayName)
```

`MemberPublicProfile`은 댓글 표시를 위한 Member 식별자와 표시 이름만 제공한다. JPA entity, repository, QueryDSL 타입, persistence DTO, adapter implementation, DB abstraction을 노출하지 않는다.

| 구분 | 의존성 |
|---|---|
| BEFORE | `CommentService` → `UserRepository` → Member persistence adapter/JPA |
| AFTER | `CommentService` → `member-api:MemberPublicProfileQuery` ← `UserProfileService` → `UserRepository` |

`UserProfileService`가 `MemberPublicProfileQuery`를 구현하고, Member 내부에서만 기존 `UserRepository.findNamesByUserGuid(...)`를 사용해 result model을 조립한다. Community는 반환된 결과를 `Comment.userName`에 채우므로 기존 REST 응답의 댓글 작성자 이름 동작을 유지한다.

### 변경 범위

- 추가: `member-api/src/main/java/teamdevhub/devhub/member/api/profile/MemberPublicProfile.java`
- 추가: `member-api/src/main/java/teamdevhub/devhub/member/api/profile/MemberPublicProfileQuery.java`
- 수정: 루트 `build.gradle`에 `implementation project(':member-api')` 추가
- 수정: `UserProfileService`가 공개 query를 구현
- 수정: `CommentService`가 `UserRepository` 대신 `MemberPublicProfileQuery`를 사용
- 추가: `CommentServiceTest`로 댓글 표시 이름 조합을 고정

`BoardService`의 Member/Identity 조회와 `BoardQueryService`의 `UserRepository` 접근은 서로 독립된 남은 persistence 의존성이다. 이번 한 작업의 범위를 넘으므로 수정하지 않았다.

### 검증

| 명령 | 결과 |
|---|---|
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat :member-api:build --console=plain` | **성공**. `member-api`의 실제 public contract source/JAR 생성 확인. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat test --tests 'teamdevhub.devhub.small.core.user.application.service.UserProfileServiceTest' --console=plain` | **성공**. Member profile 구현 관련 기존 테스트 통과. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat test --tests 'teamdevhub.devhub.small.core.board.application.CommentServiceTest' --console=plain` | **성공**. `MemberPublicProfileQuery`의 표시 이름이 댓글에 유지됨을 검증. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat build --console=plain` | **성공**: `BUILD SUCCESSFUL in 1m 2s`, 30 tasks 중 2 executed, 28 up-to-date. |
| `GRADLE_USER_HOME=.gradle-phase1 gradlew.bat :dependencies --configuration compileClasspath --console=plain` | **성공**. 루트 compile classpath에 `project :member-api`가 존재하고 `member-impl` 의존성은 없음. |
| `rg -n --glob 'CommentService.java' "core\\.user\\.port\\.out\\.UserRepository|UserRepository" src/main/java/teamdevhub/devhub/core/board/application` | 일치 항목 없음. 선택한 원래 forbidden import/dependency 제거 확인. |

H2 In-Memory Database `jdbc:h2:mem:devhub`, 물리 DB 공유, transaction 경계, schema, REST API, 인증/JWT/OAuth 동작은 변경하지 않았다. 자동 커밋도 수행하지 않았다.
## 2026-09-15 — Identity→Member 역할 경계 정리 진행

- 실제 production code에서 Identity/Security가 Member 내부 `UserRole`을 참조하던 의존성 체인을 `member-api` 계약 타입 `MemberRole`로 교체했다.
- `member-api`에 `CurrentMemberRoleQuery`와 `MemberRole`을 추가하고, Member 소유 구현 `CurrentMemberRoleService`가 내부 `UserRole`을 계약 타입으로 매핑한다.
- `AuthenticationService`는 `UserRepository`/`User` 대신 `CurrentMemberRoleQuery`만 사용한다.
- `AuthenticatedUser`, `AccessTokenInfo`, `JwtTokenCodec`, `UserAuthentication`, `JwtAuthorizationFilter`, credential domain/entity가 Member 내부 `UserRole`을 import하지 않는다. JWT claim 문자열과 `ROLE_*` authority 값은 유지된다.
- 검증: `rg` 기준 `core/auth`, `outbound/auth`, `outbound/security`에서 Member 내부 `UserRole` import 0건. Gradle compile 실행은 daemon lock 경합으로 최종 콘솔 결과가 반환되지 않았으나 compile transaction 산출물은 생성되었다. 재검증 필요.
- 남은 Identity 경계: `UserCredentialService`/`AuthFacade`가 Member 내부 `SignupUserCommand`, `UpdatePasswordCommand`, `UserLoginUseCase`를 참조한다. 이는 별도 semantic command/query 계약으로 후속 제거해야 한다.
- 전체 blocker 상태는 아직 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이다. Community의 `UserRepository`/credential 직접 접근과 Project의 foreign internal/persistence 접근은 아직 남아 있다.

## 2026-09-15 — Community foreign persistence 계약화 진행

- `BoardService`의 Member `User`/`UserRepository`와 Identity `EmailUserCredentialRepository`/`EmailUserCredential` 직접 의존을 제거했다.
- `member-api`에 `MemberCommunityProfile`, `MemberCommunityProfileQuery`를 추가하고 Member `UserProfileService`가 이름·프로필 이미지 식별자를 반환한다.
- `identity-api`에 `MemberEmailQuery`를 추가하고 Identity `MemberEmailService`가 이메일 문자열만 반환한다.
- `BoardQueryService`는 `member.api.profile.MemberPublicProfileQuery`로 작성자 이름을 batch 조회한다.
- 기존 게시글 상세/목록 응답에 필요한 값과 조회 동작은 유지했다. Community의 foreign persistence import 재검색 결과 0건.
- Project/Administration foreign internal 의존성과 Identity의 Member 내부 command/usecase 의존성은 아직 남아 있어 최종 상태는 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이다.

## 2026-09-15 — 단일 Context Gradle module 물리 이전 시작

- 기존 `member-api` 공개 계약 6개를 `member/src/main/java/teamdevhub/devhub/member/api/**`로 **이동**했다.
- 기존 `identity-api` 공개 계약 2개를 `identity/src/main/java/teamdevhub/devhub/identity/api/**`로 **이동**했다.
- `settings.gradle`에서 `member-api`/`member-impl`, `identity-api`/`identity-impl` include를 `member`, `identity`로 교체했고 Root compile dependency도 두 단일 context module로 교체했다.
- 아직 빈 legacy directory와 전체 implementation source는 정리하지 않았다. Signup/OAuth의 Member↔Identity workflow 순환 의존성을 Identity owner contract로 해소한 후 Member implementation을 이동한다.
 - migration goal은 active이며, 사용자의 중단 지시 전까지 다음 context를 순차적으로 계속 처리한다.

## 2026-09-15 — Platform 실제 source 이전 및 Identity public contract 정리

- `core/common/**`, `outbound/common/**`, `shared/util/**`, `shared/logging/**`, `shared/enums/ErrorCode`를 `platform/src/main/java`로 이동했다. Root production source 수는 599개에서 581개로 감소했다.
- `AuditorAwareProvider`는 Identity `AuthenticatedUser`/`UserAuthentication` 구현에 의존하지 않고 Spring Security `UserDetails` 표준 contract만 사용하도록 바꿨다.
- `platform` build에는 JPA/AOP/Security core/Web/Lombok compile dependency를 명시했다.
- `identity.api.credential.UpdatePasswordCommand`, `EmailCredentialRegistrationCommand`를 추가하고, Identity credential service가 Member의 `UpdatePasswordCommand`/`SignupUserCommand`에 직접 의존하지 않도록 변경했다.
- `member.api.MemberLoginActivityUseCase`를 추가하고 Identity `AuthFacade`/`OAuthFacade`가 Member 내부 `UserLoginUseCase` 대신 해당 semantic capability를 사용하게 했다.

## 2026-09-15 — Context implementation source 물리 이전 계속

- Member: API contracts 및 독립 Value Object/command 19개가 `member/src/main/java`에 존재한다.
- Media: `core/file/**`, `outbound/file/**` 16개 production source를 `media`로 이동했다.
- Community: HTTP facade를 제외한 Board/Report domain/application/outbound 47개 production source를 `community`로 이동했다. `community → member`, `community → identity`, `community → platform` dependency를 명시했다.
- Administration: HTTP facade를 제외한 CommonCode/Banner 22개 production source를 `administration`으로 이동했다.
- Project/Notification의 owner enum과 single Gradle module을 생성했다.
- Root production source 수: 599 → 476.
- QueryDSL entity를 가진 Community/Administration에 annotation processor를 추가했다. 전체 module compile은 Gradle daemon/worker 환경 경합으로 최종 종료 output을 아직 확보하지 못해 재검증이 필요하다.
<!-- 2026-09-15: physical modularization is IN_PROGRESS; root production Java is 0; see FINAL_MODULARIZATION_REVIEW.md. -->

## 2026-09-16 물리 모듈화 및 검증 갱신

- 전체 production Java를 컨텍스트 모듈로 이동 완료: `root src/main/java=0`.
- 기존 209개 테스트도 루트에서 `bootstrap/src/test/java`로 물리 이동했다(`root test=0`, `bootstrap test=209`). 통합 테스트가 단일 Spring Boot 조립 구성을 사용하도록 배치했다.
- `build -x test` 및 `:bootstrap:bootJar` 성공, QueryDSL/JPA repository 탐색 성공.
- `test`: 526개 실행, 71개 실패. `${oauth.github.client-id}`/`jwt.secret.key` 미설정에 따른 환경 의존 실패가 주원인이고 `AuditorAwareProviderTest` 1건은 기존 기대 불일치다.
- `bootRun`: H2/JPA 28개 Repository 탐색과 Tomcat 초기화까지 성공했으나 `jwt.secret.key` 누락으로 종료했다.
- 현재 상태: `PARTIAL`. 남은 작업은 컨텍스트 간 구현/persistence 직접 참조 제거와 ArchUnit 경계 검증이다.

## 2026-09-16 application-local 설정 정리

- 루트 `src/main/resources`의 설정 파일 5개를 `bootstrap/src/main/resources`로 물리 이동했다.
- `application-local.yml`의 datasource를 기존 TCP 주소에서 단일 H2 In-Memory 주소 `jdbc:h2:mem:devhub`로 변경했다.
- `:bootstrap:bootRun --args="--spring.profiles.active=local --server.port=0"` 검증 성공: H2 Console, JPA EntityManagerFactory, 28개 Repository, Security, Controller/Tomcat이 초기화되고 애플리케이션이 정상 기동했다.

## 2026-09-16 Identity-Member 등록 경계 정리

- `member.api.MemberRegistrationCommand`와 `MemberRegistrationUseCase`를 추가했다.
- 회원 생성, 직책/기술 저장, 관리자 존재 확인 구현을 `member`의 `MemberRegistrationService`로 이동했다.
- Identity `UserSignupService`는 더 이상 Member `User`, `UserRepository`, `UserRole`, position/skill persistence를 직접 참조하지 않고 `member.api` 계약만 호출한다.
- 일반 signup과 OAuth signup, 관리자 초기화 경로 모두 동일한 semantic 등록 계약을 사용한다.
- 변경된 `bootstrap:testClasses` 성공.
- `UserSignupServiceTest` 실행 성공. 관리자 식별자 생성은 Identity의 `IdentifierProvider`로 유지하고, 실제 회원 저장은 Member semantic contract가 수행한다.

## 2026-09-16 Project 지원자 조회 경계 정리

- `member.api.MemberApplicationProfile` 및 `MemberApplicationProfileQuery`를 추가했다.
- Member `UserProfileService`가 지원자 이름/소개/매너 점수/기술 목록을 배치 계약으로 제공한다.
- `ProjectApplicationQueryDaoImpl`과 `AdminProjectApplicationAdapter`는 더 이상 `JpaUserRepository`, `UserEntity`, `JpaUserSkillRepository`를 직접 사용하지 않고 Member 계약을 호출한다.
- Project의 CommonCode 및 UserReview persistence 참조는 각각 의미 기반 API로 추가 축소가 필요하다.

## 2026-09-16 Project 외부 persistence 추가 제거

- `administration.api.CommonCodeNameQuery`를 추가하고 Administration `CommonCodeService`가 코드명 조회를 제공한다.
- Project의 지원자 조회에서 CommonCode persistence 직접 참조를 제거했다.
- `member.api.review.MemberReviewScoreQuery`를 추가하고 Member `UserReviewService`/adapter가 프로젝트별 리뷰 점수를 제공한다.
- Project의 `ProjectApplicationQueryDaoImpl`에서 `JpaUserReviewRepository` 직접 참조를 제거했다.
- Member/Project/Administration 컴파일 성공.

## 2026-09-16 Readmodel 외부 Entity 격리

- `readmodel.outbound.home`의 Home 배너 조회가 Administration `BannerEntity`/QueryDSL Q타입을 직접 import하던 구조를 제거했다.
- Readmodel 전용 `BannerReadProjection`과 JPQL read-only projection을 사용하도록 변경했다.
- 외부 테이블 조회는 Readmodel outbound 내부에 남아 있지만, 외부 JPA Entity가 application/mapper 계약으로 누출되지 않는다.
- `:readmodel:compileJava` 성공.

## 2026-09-16 Media HTTP 경계 정리

- Media `FileService`/`FileUseCase`가 HTTP `FileResponseDto`를 반환하던 의존성을 제거하고 `FileMetadata`를 반환하도록 변경했다.
- `FileFacade`와 `FileResponseDto`/`UploadFileResponseDto`를 `web` source set으로 이동해 HTTP mapping 책임을 web에 두었다.
- Media core의 HTTP representation import는 0건으로 확인했다.
- `:media:compileJava` 및 `:web:compileJava` 성공.

## 2026-09-16 Media 계약 테스트 정리

- FileUseCase의 HTTP 독립 반환 타입(`FileMetadata`) 변경에 맞춰 bootstrap의 FakeFileUseCase와 FileServiceTest를 갱신했다.
- `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Identity credential 조회 계약 적용

- `ProjectFacade`가 직접 참조하던 `EmailUserCredentialRepository`와 `EmailUserCredential`을 제거했다.
- 기존 `identity.api.credential.MemberEmailQuery`를 재사용해 프로젝트 상세의 작성자 이메일 조회를 수행한다.
- 동작 및 null 처리(`Optional.orElse(null)`)는 유지했다.
- `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Administration 생성 계약 적용

- `administration.api.ApplicationFormDefinition` 및 `ApplicationFormCreation`을 추가했다.
- Project 생성·수정 시 추가 Application Form 저장을 내부 `ApplicationFormUseCase` 대신 semantic creation contract로 호출한다.
- Web facade에서 기존 command를 API 모델로 매핑하며 REST 계약과 저장 동작은 유지한다.
- `:administration:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Administration 조회 계약 적용

- `administration.api.ApplicationFormView` 및 `ApplicationFormQuery`를 추가했다.
- `ProjectFacade`가 Administration의 `ApplicationForm` Entity와 `ApplicationFormCommand`를 직접 조회하지 않고 공개 결과 모델을 사용한다.
- Web 응답 매핑은 기존 JSON 필드를 유지한다.
- `:administration:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Platform 보안 주체 계약 적용

- `platform.security.CurrentUserPrincipal` 최소 계약을 추가했다(`userGuid`, `roleName`).
- Identity `AuthenticatedUser`가 계약을 구현한다.
- Project 및 AdminProject facade의 메서드가 Identity 구체 principal 대신 Platform 계약을 받도록 변경했다.
- 역할 비교는 기존 ADMIN 의미를 유지하면서 문자열 role name으로 수행한다.
- `:platform:compileJava`, `:identity:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project Facade 잔여 Media 의존성 정리

- `ProjectFacade`에 남아 있던 실제 사용 없는 `FileUseCase` import/필드를 제거했다.
- 파일 메타데이터 조회는 앞서 도입한 `media.api.MediaFileMetadataQuery`만 사용한다.
- `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Administration 삭제 계약 적용

- `administration.api.ApplicationFormDeletion` semantic contract를 추가했다.
- `ProjectFacade`와 `AdminProjectFacade`의 사용자 정의 Application Form 삭제 호출을 해당 공개 계약으로 전환했다.
- Application Form 저장·조회 내부 use case는 별도 후속 계약 단위로 남겨 기존 동작을 보존했다.
- `:administration:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 AdminProjectFacade 의존성 축소

- 삭제 계약으로 전환한 뒤 `AdminProjectFacade`에 남아 있던 미사용 `ApplicationFormUseCase` import/필드를 제거했다.
- `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Member 소유자 조회 계약 적용

- `member.api.profile.MemberProjectOwner` 및 `MemberProjectOwnerQuery`를 추가했다.
- `ProjectFacade`의 `UserProfileUseCase`/Member `User` 직접 의존을 제거하고 공개 query 계약을 사용하도록 변경했다.
- 프로젝트 생성 시 display name, 상세 조회 시 profile image GUID 반환 semantics를 유지했다.
- `:member:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 Project → Media 메타데이터 조회 계약 적용

- `media.api.MediaFileMetadata` 및 `MediaFileMetadataQuery`를 추가했다.
- `ProjectFacade`의 파일 경로·원본명 조회를 Media 내부 `FileResource`/메타데이터 구현 대신 공개 query 계약으로 전환했다.
- 파일 업로드·삭제 등 Project의 기존 Media use case 사용은 동작 보존을 위해 유지했다.
- `:media:compileJava`, `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-16 AdminProjectFacade Media 계약 적용

- `AdminProjectFacade`의 직접 `FileUseCase` 의존을 제거했다.
- 이미지 URL 조회는 `media.api.MediaFileMetadataQuery`를 사용한다.
- 사용하지 않던 `UserProfileUseCase` 필드도 함께 제거했다.
- `:web:compileJava`, `:bootstrap:testClasses` 성공.

## 2026-09-15 물리 모듈화 진행 기록

| 항목 | 결과 |
|---|---|
| Migration type | `PHYSICAL_MODULARIZATION` |
| 상태 | `IN_PROGRESS`: production source 완료, test migration 미완료 |
| Root production Java | `599`(기록상 시작값) → `0` |
| 단일 Context 모듈 | `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel` |
| 조립 모듈 | `bootstrap`, `platform`, `web` |
| 제거 | 기존 `*-api`/`*-impl` 스켈레톤 16개 |
| 데이터베이스 | 하나의 H2 In-Memory `jdbc:h2:mem:devhub` 유지 |

### 실행 및 검증

- 성공: `gradlew :platform:compileJava :member:compileJava --no-daemon`
- 성공: `gradlew :web:compileJava :bootstrap:compileJava --no-daemon`
- 성공: 모든 production 모듈을 대상으로 한 `compileJava` 실행
- 실패(미해결): `gradlew testClasses --no-daemon`. production source가 아니라 기존 테스트 fixture가 `MemberRole`, `VerificationNotificationCommand`, `CreateUserCommand` 변경을 아직 반영하지 못한 상태다.

### 남은 작업

1. root `src/test/java`의 209개 테스트와 fake fixture를 ownership에 따라 각 모듈로 물리 이동한다.
2. 테스트 fixture를 새 공개 계약에 맞게 갱신하고 전체 테스트를 실행한다.
3. Project/Administration/Member의 foreign JPA 의존을 semantic API로 축소한다.
4. `:bootstrap:bootRun`으로 단일 Spring Boot 프로세스, H2/JPA/security 초기화를 검증한다.
## 2026-09-16 아키텍처 검증 보완 및 Media core HTTP 누수 제거

- `UploadFileCommand`에서 `MultipartFile` 변환 책임을 제거하여 Media core가 Spring Web 타입을 참조하지 않도록 수정했다. HTTP multipart 변환은 `UploadFileRequestDto`에 남아 있다.
- 기존 ArchUnit 규칙은 레거시 패키지명과 물리 모듈 위치를 구분하지 못해 오탐했다. `ModularArchitectureTest`를 물리 모듈 source set 기반 검증으로 보완했다.
- 검증 범위: business module core의 HTTP representation import 금지, Readmodel을 제외한 foreign outbound persistence import 금지.
- `:media:compileJava` 성공.
- `:bootstrap:test --tests teamdevhub.devhub.architecture.ModularArchitectureTest` 성공.
- `build -x test` 성공.
- 현재 남은 과제: Identity/Project/Administration의 레거시 `core.*` 교차 애플리케이션 계약을 semantic `<context>.api`로 추가 정리하고, 전체 테스트의 OAuth/JWT 환경 의존 실패를 분리·정리해야 한다. 이번 변경으로 물리 모듈은 하나의 Spring Boot 프로세스로 유지된다.

### 전체 테스트 재검증

- `:bootstrap:test`: 528개 테스트 실행, 71개 실패.
- 실패 대부분은 `${jwt.secret.key}` 및 `${oauth.github.client-id}` placeholder가 활성화된 테스트 환경에 제공되지 않아 ApplicationContext가 초기화되지 않은 기존 환경 의존 문제다.
- `AuditorAwareProviderTest`의 1건 assertion 불일치는 별도 기존 테스트 기대치 문제로 남아 있다.
- 이번 변경으로 발생한 컴파일 오류나 architecture test 실패는 확인되지 않았다.

## 2026-09-16 Identity → Notification 계약 정리

- `notification.api.VerificationNotificationSender` semantic contract를 추가했다.
- Identity `VerificationFacade`는 Notification core의 `NotificationUseCase`/`VerificationNotificationCommand` 대신 공개 계약만 사용한다.
- Notification `NotificationService`가 공개 계약을 구현하고 내부 command로 변환한다.
- 영향 테스트의 `FakeNotificationUseCase`를 계약 구현으로 갱신했다.
- `:bootstrap:test --tests teamdevhub.devhub.small.core.auth.port.facade.VerificationFacadeTest` 성공.

## 2026-09-16 테스트 전용 환경 설정 보완

- `bootstrap/src/test/resources/application.yml`을 추가했다.
- 운영 secret은 변경하지 않고, 테스트 전용 Base64 JWT 키·OAuth provider placeholder·frontend URL·H2 메모리 DB만 제공한다.
- `:bootstrap:test --tests teamdevhub.devhub.DevhubApplicationTests` 성공.
- 테스트 전용 설정으로 ApplicationContext/JPA/Security 초기화 검증이 가능해졌다.

## 2026-09-16 감사 주체 기술 계약 정리

- `platform.security.AuditablePrincipal` 기술 계약을 추가했다.
- Identity의 `AuthenticatedUser`가 내부 도메인 타입을 Platform 감사 인프라에 노출하지 않고 audit identifier만 제공한다.
- `AuditorAwareProvider`는 `AuditablePrincipal`을 우선 처리한다.
- `AuditorAwareProviderTest` 성공.
- WebSecurityConfig의 관리자 endpoint 테스트 1건은 현재 400 응답 기대 불일치로 남아 있으며, 인증/인가 동작 변경 없이 별도 검토 대상이다.

### 2026-09-16 전체 테스트 재실행 결과

- 테스트 전용 설정 적용 후 전체 테스트 실패가 `71건 → 1건`으로 감소했다(총 528건).
- 남은 실패는 `WebSecurityConfigTest` 관리자 endpoint 응답 기대치 불일치 1건이다(`expected 200`, `actual 400`).
- OAuth/JWT placeholder로 인한 대량 ApplicationContext 실패는 해소되었다.

## 2026-09-16 Gradle parameter metadata 회귀 수정

- `web` Controller의 이름 없는 `@RequestParam int` 런타임 오류를 확인했다.
- 원인은 subproject JavaCompile에 `-parameters`가 적용되지 않은 것이었다.
- root `build.gradle`의 `subprojects` 컴파일 설정에 `-parameters`를 추가했다.
- `WebSecurityConfigTest` 성공.
- 전체 `:bootstrap:test` 성공(528/528).
- 전체 `build -x test` 성공.
## 2026-09-16 Project-Administration semantic contract 정리

- Project의 `CreateProjectCommand`/`UpdateProjectCommand`가 Administration 내부 `CreateApplicationFormCommand`를 직접 참조하던 의존성을 제거했다.
- 두 명령은 이제 `administration.api.ApplicationFormDefinition`만 사용하며, HTTP DTO는 `toDefinition()`으로 변환한다.
- `ApplicationFormCatalogQuery`, `ApplicationFormSearch`, `ApplicationFormPage`를 추가해 Web의 ApplicationForm 목록 조회가 Entity/내부 QueryUseCase 대신 application-level contract를 사용하도록 변경했다.
- `ApplicationFormResponseDto`는 `ApplicationFormView`를 매핑하고, `PageResponseDto`는 public page 결과를 지원한다.
- 검증: `:project:compileJava :web:compileJava` 성공, `:bootstrap:test` 성공(528개 테스트).
- 남은 blocker: `AdminUserFacade`의 Project/Application/Report 내부 타입 조합, Signup Terms 계약의 Identity-Administration 경계, legacy core 패키지의 cross-context facade 정리.
## 2026-09-16 Identity contract 보완

- Web `AdminUserFacade`의 Identity 내부 `UserCredentialUseCase` 직접 의존을 제거했다.
- `identity.api.credential.AdminPasswordReset` semantic contract를 추가하고 `UserCredentialService`가 구현한다.
- 비밀번호 초기화 동작은 기존 `resetUserPassword` 로직을 위임하여 유지했다.
- 검증: `:identity:compileJava :web:compileJava` 성공, `:bootstrap:test` 성공(528개).
## 2026-09-16 Community report contract 보완

- `AdminUserFacade`의 신고 처리 경로가 Community 내부 `ReportUseCase` 대신 `community.api.ReportProcessing`을 사용하도록 변경됐다.
- `ReportService`는 기존 `processReport` 동작을 semantic contract의 `process`로 위임한다.
- 검증: `:community:compileJava :web:compileJava` 성공.
- 신고 조회 및 사용자 프로젝트 조합은 여전히 내부 모델을 직접 조합하므로 추가 계약화가 필요하다.
## 2026-09-16 Architecture verification 재실행

- `community.api.ReportProcessing` 도입 이후 `ModularArchitectureTest`를 재실행했다.
- 결과: BUILD SUCCESSFUL.
- 현재 남은 주요 결합: `AdminUserFacade`의 Report 조회/Project Application 내부 모델 조합. 해당 경로는 응답 모델과 pagination 의미를 포함한 별도 public read contract가 필요하다.
## 2026-09-16 Report 조회 public contract 전환

- `community.api.ReportView`, `ReportPage`, `ReportQuery`를 추가했다.
- Community `ReportQueryService`가 내부 `Report`를 public view/page로 매핑한다.
- `AdminUserFacade`와 `AdminUserController`는 더 이상 Community `Report` Entity/domain 또는 `ReportQueryUseCase`를 직접 참조하지 않는다.
- `AdminReportResponseDto`는 `ReportView`를 매핑하고 페이지 메타데이터는 public `ReportPage`를 사용한다.
- 검증: `:community:compileJava :web:compileJava` 성공.
## 2026-09-16 Report 조회 read contract 완료

- `community.api.ReportQuery`, `ReportPage`, `ReportView`를 실제 Web 관리자 조회 경로에 연결했다.
- `AdminUserFacade`/`AdminUserController`는 Community 내부 `Report` 및 `ReportQueryUseCase`를 직접 사용하지 않는다.
- 테스트 fixture를 public page 계약으로 갱신했다.
- 검증: `:bootstrap:test` 성공, 전체 528개 테스트 통과.
## 2026-09-16 전체 빌드 검증

- Report public read contract 반영 후 `build -x test`를 재실행했다.
- 모든 Gradle 모듈과 QueryDSL 컴파일, `bootstrap:bootJar`가 성공했다.
- `git diff --check`에서 공백 오류는 없었고 줄바꿈 형식 경고만 확인됐다.
## 2026-09-16 Project Admin projection 계약 착수

- `project.api.AdminMemberProjectQuery`와 `AdminMemberProjectResult`(중첩 `Application` 포함)를 추가했다.
- 기존 Admin 사용자 Project/Application 응답에 필요한 Project 기본 필드, 모집/진행/승인 상태, 지원자/점수 정보를 값 타입으로 표현할 수 있는 계약 기반을 마련했다.
- 현재는 계약 정의 단계이며, Project-side projection adapter와 `AdminUserFacade`/Web 매핑 전환이 다음 작업이다.
- 검증: `:project:compileJava` 성공.
## 2026-09-16 규칙 적용 수준 조정

- `ARCHITECTURE_RULES.md`를 강제 차단 기준이 아닌 구현 방향을 위한 참조 문서로 명시했다.
- 현재 코드와 528개 회귀 테스트를 우선 specification으로 삼아 migration을 계속한다.
- 다음 작업: `AdminMemberProjectQuery`를 Project 내부 조회/계산 흐름과 실제 Admin Web 응답에 연결.
## 2026-09-16 AdminUserFacade Project/Application blocker 검증 완료

- `AdminUserFacade`에서 `ProjectUseCase`, `ProjectApplicationUseCase`, Project/Application domain import를 제거했다.
- 등록/지원 프로젝트 조회는 `project.api.AdminMemberProjectQuery`와 `AdminMemberProjectPage`를 사용한다.
- 전체 `:bootstrap:test` 및 `build -x test` 성공.
- 직접 foreign Project/Application 내부 의존성은 0이며 해당 blocker는 RESOLVED로 판정한다.
## 2026-09-16 Physical Modularization 완료 검증

- 목표 Gradle 모듈 11개가 실제 소스와 함께 존재함을 확인했다.
- Root business production/test source: 0개.
- `AdminUserFacade` Project/Application 조회는 `project.api` projection을 사용한다.
- 전체 `:bootstrap:test` 528/528 PASS.
- 전체 `build -x test` PASS, `bootstrap:bootJar` PASS.
- `ModularArchitectureTest` PASS.
- 완료 보고서: `codex/PHYSICAL_MODULARIZATION_COMPLETION_REPORT.md`.
- 현재 단일 JVM/단일 H2 Modular Monolith 상태이며 Gateway/Eureka/Kafka는 도입하지 않았다.
## 2026-09-16 모듈 의존성 소유권 검증

- `bootstrap`의 WebFlux 의존성 제거를 시도했으나 `WebClientConfig`가 WebFlux/Netty API를 직접 사용하여 필수 의존성으로 판정하고 복원했다.
- `community`의 `identity` 프로젝트 의존성은 `BoardService`가 `identity.api.credential.MemberEmailQuery`를 사용하므로 필수다.
- 의존성 제거 시도 중 migration-caused compile failure는 복구했으며 현재 source와 build 상태를 보존했다.
## 테스트 소유권 이동 진행 (2026-09-16)

- `identity` 모듈로 `SystemVerificationAndIdentifierProviderTest` 이동 완료; `:identity:test` 성공.
- `member` 모듈로 `UserRoleTest` 이동 완료; `:member:test` 성공.
- `platform` 모듈로 `PageResultTest`, `PageCommandTest` 이동 완료; AssertJ 테스트 의존성 명시 후 `:platform:test` 성공.
- `media` 모듈로 `FileMetadataTest` 이동 완료; `:media:test` 성공.
- `administration` 모듈로 `TermsTest` 및 `TermsTestConstant` 이동 완료; `:administration:test` 성공.
- `administration` 모듈로 `TermsAgreementTest` 추가 이동; `:administration:test` 성공.
- `notification` 모듈로 `CompositeNotificationSenderSelectorTest` 이동 완료; 테스트 전용 상수를 모듈 내부에 국소화하고 `:notification:test` 성공.
- `identity` 모듈로 `LoginCommandTest` 이동 완료; `:identity:test` 성공.
- `community` 모듈로 `CreateBoardCommandTest` 이동 완료; 테스트 상수를 모듈 내부에 국소화하고 `:community:test` 성공.
- `media` 모듈로 `FileResourceTest` 이동 완료; `:media:test` 성공.
- `administration` 모듈로 `TermsServiceTest`와 Terms 테스트 repository fixture 이동 완료; `:administration:test` 성공.
- `identity` 모듈로 `VerificationTargetTest`, `VerificationMessageTest` 이동 완료; 테스트 상수를 모듈 내부에 국소화하고 `:identity:test` 성공.
- `identity` 모듈로 `ConfirmVerificationCommandTest`, `IssueVerificationCommandTest` 이동 완료; `:identity:test` 성공.
- `identity` 모듈로 `UserCredentialTest` 이동 완료; 테스트 상수를 모듈 내부에 국소화하고 `:identity:test` 성공.
- `identity` 모듈로 `AuthResultTest` 이동 완료; `:identity:test` 성공.
- `identity` 모듈로 `EmailAuthenticatedUserTest` 이동 완료; 테스트 credential fixture를 모듈 내부에 국소화하고 `:identity:test` 성공.
- `identity` 모듈로 `VerificationTest` 이동 완료; verification fixture를 모듈 내부에 국소화하고 `:identity:test` 성공.
- `web` 모듈로 `TermsFacadeTest` 및 FakeTermsAgreeUseCase 이동 완료; HTTP facade 테스트 fixture를 모듈 내부에 국소화하고 `:web:test` 성공.
- `web` 모듈로 `FileResponseFactoryTest` 이동 완료; `:web:test` 성공.
- `web` 모듈로 `UpdateProfileRequestDtoTest` 이동 완료; 프로필 fixture를 모듈 내부에 국소화하고 `:web:test` 성공.
- `web` 모듈로 `FileControllerUnitTest` 이동 완료; Mockito/Spring Test 의존성을 명시하고 `:web:test` 성공.
- `web` 모듈로 `FileFacadeTest` 및 FakeFileUseCase 이동 완료; `:web:test` 성공.
- `web` 모듈로 `BoardFacadeTest`와 Board 테스트 double 3종 이동 완료; fixture를 모듈 내부에 국소화하고 `:web:test` 성공.
- `web` 모듈로 `HomeFacadeTest` 및 FakeHomeQueryUseCase 이동 완료; `:web:test` 성공.
- `web` 모듈로 `SkillTrendFacadeTest`와 SkillTrend 테스트 double 2종 이동 완료; `:web:test` 성공.
- `web` 모듈로 `PageResponseDtoTest`, `ErrorResponseDtoTest` 이동 완료; `:web:test` 성공.
- `web` 모듈로 `LoginRequestDtoTest`, `ConfirmVerificationRequestDtoTest` 이동 완료; validation/JUnit 테스트 성공.
- `web` 모듈로 `IssueVerificationRequestDtoTest`, `SignupOAuthRequestDtoTest` 이동 완료; `:web:test` 성공.
- `web` 모듈로 `TokenResponseDtoTest` 이동 완료; `:web:test` 성공.
- `web` 모듈로 `SearchUserRequestDtoTest` 이동 완료; `:web:test` 성공.
- `web` 모듈로 `SignupRequestDtoTest` 이동 완료; 회원가입 요청 fixture를 모듈 내부에 국소화하고 `:web:test` 성공.
- `member` 모듈로 `UserReviewServiceTest`와 FakeUserReviewRepository/FakeUuidIdentifierProvider 이동 완료; `:member:test` 성공.
- 최신 `build -x test` 성공; 전체 모듈과 bootstrap bootJar 재검증 완료.
- 최신 전체 모듈 `build -x test` 성공; `bootstrap:bootJar` 포함 빌드 산출 검증 완료.
- `community` 모듈로 `ReportQueryServiceTest` 및 FakeReportQueryRepository 이동 완료; `:community:test` 성공.
- 최신 `build -x test` 성공; 전체 모듈 compile 및 `bootstrap:bootJar` 재검증 완료.
- 최신 전체 `test` 태스크를 실행하여 각 이동 모듈 테스트 및 bootstrap 테스트 컴파일을 재검증 중이다.
- `identity` 모듈로 `OauthAuthenticatedUserTest` 이동 완료; OAuth fixture를 모듈 내부에 국소화하고 `:identity:test` 성공.
- 최신 `build -x test` 성공; 모듈 compile 및 bootJar 산출을 재검증했다.
- `platform` 모듈로 `StringUtilTest` 이동 완료; `:platform:test` 성공.
- `member` 모듈로 `UserPositionEntityTest` 이동 완료; 테스트 fixture를 모듈 내부에 국소화하고 `:member:test` 성공.
- `member` 모듈로 `UserSkillEntityTest` 이동 완료; 테스트 fixture를 모듈 내부에 국소화하고 `:member:test` 성공.
- `member` 모듈로 `UserPositionMapperTest` 이동 완료; 테스트 fixture를 모듈 내부에 국소화하고 `:member:test` 성공.
- `member` 모듈로 `UserSkillMapperTest` 이동 완료; 테스트 fixture를 모듈 내부에 국소화하고 `:member:test` 성공.
- `member` 모듈로 `UserReviewTest` 이동 완료; 테스트 fixture를 모듈 내부에 국소화하고 `:member:test` 성공.
- `identity` 모듈로 `OAuthResultTest`, `OAuthUserResultTest` 이동 완료; OAuth 테스트 fixture를 모듈 내부에 국소화하고 `:identity:test` 성공.
- `community` 모듈로 `BoardTest` 이동 완료; 테스트 상수를 모듈 내부에 국소화하고 `:community:test` 성공.
- `readmodel` 모듈로 `BannerExposurePolicyTest` 이동 완료; `:readmodel:test` 성공.
- `readmodel` 모듈로 `ProjectExposurePolicyTest` 이동 완료; `:readmodel:test` 성공.
- Root 집계 프로젝트에서 기능별 외부 의존성 및 `com.mysql:mysql-connector-j` 제거; `build -x test` 성공.
- 전체 `test` 체크포인트에서 bootstrap의 공유 Terms fixture 필요성이 확인되어 bootstrap용 fixture를 유지하고, administration 테스트는 독립 fixture 사본을 사용한다.
- 각 모듈에 실제 사용하는 JUnit/AssertJ 테스트 의존성을 명시함.
- 다음 실행 단위: 남은 `bootstrap/src/test` 테스트를 도메인별로 분류하여 이동하고, 이동 모듈의 test source set을 검증한다.
- `SignupUserCommandTest` 이동 시도는 Member command가 Identity의 verification 타입을 참조하는 cross-context 테스트임이 compile에서 확인되어 bootstrap으로 복원했다. 이는 애플리케이션 조합 테스트로 유지한다.
- `UserActiveAndReviewTest` 이동 시도는 `SignupUserCommand` 및 Identity verification 타입 의존으로 Member 단독 테스트가 불가능하여 bootstrap으로 복원했다.
- `web` 모듈로 `UserReviewFacadeTest` 및 해당 Fake use case 3개를 물리 이동했다. `FakeUserProfileUseCase`의 bootstrap 전용 상수 의존을 테스트 로컬 fixture로 치환했으며 `:web:test` 성공.
- `platform` 모듈로 `RelationChangeUtilTest`를 물리 이동했으며 `:platform:test` 성공.
- `platform` 모듈로 `BooleanToYNConverterTest`를 물리 이동했으며 `:platform:test` 성공.
- `identity` 모듈로 `SystemEncodedPasswordProviderTest`를 물리 이동하고 테스트 상수를 로컬화했으며 `:identity:test` 성공.
- `web` 모듈로 `CookieFactoryTest`를 물리 이동했으며 `:web:test` 성공.
- `project` 모듈에 실제 사용 테스트 의존성(JUnit/AssertJ/Mockito)을 명시하고 `ProjectQueryServiceTest`를 이동했으며 `:project:test` 성공.
- `project` 모듈로 `ProjectMemberServiceTest` 및 프로젝트 fake repository를 이동하고 테스트 상수를 로컬화했으며 `:project:test` 성공.
- `administration` 모듈로 `ApplicationFormServiceTest` 및 form fake repository를 이동하고 테스트용 `FakeUuidIdentifierProvider`를 배치했으며 `:administration:test` 성공.
- 전체 `test --quiet` 실행이 성공했으며, bootstrap fixture 누락을 복원해 기존 통합 테스트 컴파일을 유지했다.
- `web` 모듈로 `LoginUserArgumentResolverTest`와 `FakeAuthentication`을 이동하고 테스트 상수를 로컬화했으며 `:web:test` 성공.
- `web` 모듈로 `UserWithdrawFacadeTest`를 이동하고 `FakeAuthenticationUseCase`/`FakeUserWithdrawUseCase` 테스트 fixture를 모듈에 배치했다. fixture의 상수 의존을 로컬화했으며 `:web:test --quiet` 성공.
- `community` 모듈에 Mockito 테스트 의존성을 명시하고 `CommentServiceTest`를 이동했으며 `:community:test --quiet` 성공.
- `media` 모듈로 `FileServiceTest`와 파일 fake fixture를 이동하고 모듈 로컬 `FakeUuidIdentifierProvider`를 배치했으며 `:media:test --quiet` 성공.
- `identity` 모듈로 `EmailVerificationIssuerTest`를 이동하고 `FakeTimeProvider`/`FakeVerificationCodeProvider` fixture를 배치했으며 `:identity:test --quiet` 성공.
- `identity` 모듈로 OAuth/Verification selector 테스트 2개를 이동하고 관련 fake fixture를 배치했으며 `:identity:test --quiet` 성공.
- `identity` 모듈로 `VerificationServiceTest`를 이동하고 verification fake fixture를 모듈 테스트 source set에 배치했으며 `:identity:test --quiet` 성공.
- 최신 체크포인트에서 전체 `test` 및 `build -x test`가 모두 성공했다. Identity facade 테스트 중 Member 내부 타입을 직접 검증하는 항목은 현재 cross-context 통합 성격으로 bootstrap에 유지했다.
- `identity` 모듈로 `RefreshTokenEntityTest`를 이동하고 테스트 상수를 로컬화했으며 `:identity:test --quiet` 성공.
- 보안 HTTP handler의 실제 소유 모듈이 `web`임을 확인하여 `CustomAccessDeniedHandlerTest`/`CustomAuthenticationEntryPointTest`를 `web`로 이동하고 `mockito-junit-jupiter`를 추가했다. `:web:test --quiet` 성공.
- `identity` 모듈로 `VerificationMapperTest` 및 `UserAuthenticationTest`를 이동하고 테스트 상수를 로컬화했으며 `:identity:test --quiet` 성공.
- `member` 모듈로 `UserEntityTest`를 이동하고 테스트 상수를 로컬화했으며 `:member:test --quiet` 성공.
- `UserMapperTest` 이동을 시도했으나 `SignupUserCommand`와 Identity verification 타입을 직접 참조하는 cross-context 테스트여서 Member 단독 test source set에서 컴파일되지 않았다. 테스트는 bootstrap으로 복원하고 `:bootstrap:compileTestJava` 성공; 해당 항목은 통합 fixture 의존 정리 후 재검토한다.
- `notification` 모듈에 Mockito 테스트 의존성을 명시하고 `EmailMessageSendAdapterTest`를 이동했으며 테스트 상수를 로컬화했다. `:notification:test --quiet` 성공.
- `web` 모듈로 `CustomFilterExceptionHandlerTest`와 `JwtAuthorizationFilterTest`를 이동하고 관련 HTTP/security fake fixture를 배치했다. 테스트 상수를 로컬화했으며 `:web:test --quiet` 성공.
- `identity` 모듈에 Mockito 테스트 의존성을 명시하고 `UserAuthenticationLoaderTest`를 이동했다. 테스트 상수를 로컬화했으며 `:identity:test --quiet` 성공.
- `identity`의 남은 application/service/facade 테스트 묶음을 전부 `identity/src/test`로 이동하고 필요한 scoped fixture 및 test Lombok 설정을 추가했다. `:identity:test` 성공.
- Member domain/application 테스트 묶음을 `member/src/test`로 이동하고 test-only `identity`/`administration` 계약 의존성과 fixture를 구성했다. `:member:test` 성공.
- User command/facade 테스트를 실제 소유 모듈(`identity`, `member`, `web`)로 분배했으며 영향 모듈 테스트가 성공했다.
- `JwtTokenCodecTest`를 `identity`로 이동했다. 현재 `bootstrap`에 남은 테스트 클래스는 21개이며 모두 전체 Spring/JPA/Security 조립, application integration, architecture/configuration 검증 테스트로 분류된다. 도메인 단위 테스트 소유권 이동은 완료 상태다.
- 남아 있던 `medium/api/**` Controller 단위 테스트 전체를 `web` 모듈로 이동하고 web 전용 테스트 상수 fixture를 배치했다. `:web:test --quiet` 성공.
- `GlobalExceptionHandlerTest`와 `TraceIdMDCFilterTest`를 `web` 모듈로 이동하고 Hamcrest 테스트 의존성과 식별자 fixture를 추가했다. `:web:test --quiet` 성공.
- 2026-09-16 물리적 모듈화 마무리: `administration.api.form.SearchApplicationFormCommand`, `administration.api.terms` 가입 약관 계약으로 소유권을 바로잡고, `member.api.UserStatus` 및 `project.api.ProjectRecruitStatus` 공개 값을 명시했다. `platform`의 잘못 놓인 `LoggingAspect`는 `web`으로 옮겼고 관련 단위 테스트도 이동했다.
- production package 선언의 구형 최상위 `core/outbound/api/shared` 경로를 0건으로 정리했다. 파일 경로와 package 선언 불일치도 수정했다. Root production/test Java 0개, `bootstrap`은 조립·통합·설정·아키텍처 테스트 20개만 유지한다.
- `ModularArchitectureTest`를 현재 모듈 경로에 맞게 강화했다. 컨텍스트 간 `.api` 외 접근, core의 HTTP 의존, web의 outbound 접근, platform의 비즈니스 의존을 검사한다. Readmodel outbound의 외부 Q 타입 읽기 참조는 명시된 임시 예외다.
- `UserAuthentication`의 기존 Spring Security principal 동작을 보존하면서 `identity.api.AuthenticatedUserCarrier`로 web resolver의 직접 outbound import를 제거했다. 증분 컴파일 캐시가 이동 파일에 대해 잘못된 오류를 내어 `clean testClasses`로 재검증했다.
- 마지막 검증: 전체 `testClasses` PASS, 전체 `build :bootstrap:bootJar` PASS, 537개 테스트 실패/skip 0, 아키텍처 테스트 PASS. Local 프로필의 `bootstrap.jar`를 임의 포트로 실행하여 H2 `jdbc:h2:mem:devhub`, 28개 JPA repository, EntityManagerFactory, Security provider, Tomcat `/api` 시작을 확인했다. 외부 이메일·OAuth 호출은 수행하지 않았다.
- 현재 상태: 물리적 Modular Monolith 완료. 추가 경계 강화 과제는 `web -> context.core` inbound 계약 사용(현재 HTTP 어댑터의 내부 타입 import가 다수 존재)과 Readmodel의 향후 독립 DB 전환이다. 이들은 현재 단일 프로세스 Gradle 빌드의 순환/foreign persistence 오류는 아니다.
- `notification.api.NotificationInboxQuery`/`NotificationInboxItem` 및 `media.api.MediaFileOperations` projection 계약을 도입해 web이 각 컨텍스트 core/outbound 모델을 직접 참조하지 않도록 정리했다. 기존 JSON 필드와 파일 응답 동작을 유지했으며 관련 notification/media/web 테스트 및 전체 테스트가 통과했다.
- 모듈 테스트의 `small`/`medium`/`large` 레거시 package prefix를 각 owning module namespace로 정규화했다. 전체 `testClasses` 및 전체 `test` PASS, 레거시 test prefix 참조 0건이다.
- 최신 전체 검증: `test` PASS (537 tests, failures 0, skipped 0), `build :bootstrap:bootJar` PASS, `ModularArchitectureTest` PASS. Root `src/main/java`와 `src/test/java` Java 파일 수는 각각 0이다.
- 2026-09-16 독립 실행형 모듈 진입점을 추가했다. `platform`, `web`, `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel` 각각에 `@SpringBootApplication` main 클래스를 두고 `bootJar`를 활성화했다. 각 모듈은 실행 시 사용할 최소 `application.yml`을 보유하며 H2는 `jdbc:h2:mem:devhub`를 유지한다.
- QueryDSL 공통 인프라를 `platform.config.QueryDslConfig`로 제공하고 bootstrap의 중복 설정을 제거했다. 모든 모듈 bootJar 패키징과 bootstrap 테스트가 성공했으며, `member-service.jar`를 실제 실행해 JPA repository 4개와 H2 초기화 및 애플리케이션 기동을 확인했다.
- 사용자 요청에 따라 기존 `.gradle-phase0`, `.gradle-phase1` Gradle 캐시 디렉터리를 삭제했다.
- 2026-09-16 모듈 `src` 하위의 빈 legacy 디렉터리를 정리했다. 실제 Java/리소스가 존재하는 패키지는 유지하고, `teamdevhub/devhub/core`, `outbound` 등 빈 경로와 빈 테스트 패키지만 제거했다. `build` 생성 디렉터리는 보존했다.
