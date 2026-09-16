# DevHub 물리적 모듈화 준비성 감사 보고서

## 1. 요약

현재 저장소는 `bootstrap`, `platform`, `web`, `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel` Gradle 프로젝트로 구성된 단일 Spring Boot 애플리케이션이다. Root의 business production Java source는 0개이며 실제 소스는 각 모듈에 존재한다. 전체 테스트와 build는 통과한다.

## 2. 현재 Repository 구조

`settings.gradle`에 위 11개 프로젝트가 등록되어 있다. `bootstrap`이 실행 조립 및 리소스를 소유하고, `web`이 HTTP adapter를, 각 도메인 모듈이 domain/application/outbound를 소유한다. H2는 `jdbc:h2:mem:devhub` 하나를 공유한다.

## 3. Module 현황

| Module | Production | Test | 역할 |
|---|---:|---:|---|
| bootstrap | 7 | 209 | Spring Boot 조립/통합 테스트 |
| platform | 21 | 0 | 기술 공통 기능 |
| web | 146 | 0 | Controller/HTTP DTO |
| identity | 112 | 0 | 인증/JWT/OAuth |
| member | 60 | 0 | Member/User |
| project | 83 | 0 | Project/Recruitment/Application |
| community | 47 | 0 | Board/Comment/Report |
| administration | 64 | 0 | Admin/Form/Terms/Code |
| media | 16 | 0 | File/metadata |
| notification | 20 | 0 | Notification/Email |
| readmodel | 38 | 0 | Home/SkillTrend 조회 |

총 테스트는 528개이며 Root production source는 0개다.

## 4. Gradle Module 의존성

`web`은 모든 도메인 모듈에 의존한다. `project`는 `member`, `administration`, `platform`을 사용하고 `identity`는 `member`, `administration`, `notification`, `platform`을 사용한다. `bootstrap`은 실행에 필요한 모듈을 조립한다. 모듈은 물리적으로 분리되어 있으나 단일 context 내부 package visibility까지 완전히 강제하지는 않는다.

## 5. Cross-Context 감사

Core의 HTTP representation import는 0개다. Report 조회는 `community.api.ReportQuery`, `ReportPage`, `ReportView`를 사용하며, 비밀번호 초기화는 `identity.api.credential.AdminPasswordReset`을 사용한다. ApplicationForm은 `administration.api` 계약으로 전환되었다. Readmodel의 외부 read-only QueryDSL 접근은 승인된 예외다.

## 6. Public/Semantic Contract

주요 계약은 `member.api.MemberPublicProfileQuery`, `MemberProjectOwnerQuery`, `MemberRegistrationUseCase`, `identity.api.PasswordLoginAvailabilityQuery`, `identity.api.credential.AdminPasswordReset`, `administration.api.ApplicationFormQuery`, `ApplicationFormCreation`, `ApplicationFormDeletion`, `community.api.ReportQuery`, `project.api.AdminMemberProjectQuery`다. 계약은 Entity/Repository/QueryDSL 타입을 노출하지 않는다.

## 7. `AdminUserFacade` 상태

등록/지원 프로젝트 조회는 `project.api.AdminMemberProjectQuery` projection을 사용하고 `UserProjectResponseDto.fromProjection`에서 Web DTO로 변환한다. Report 조회도 public projection을 사용한다. 다만 페이지 총계 확보를 위해 `ProjectUseCase` 및 `ProjectApplicationUseCase`를 baseline 조회에 사용하므로 Web이 Project 내부 application contract에 직접 의존하는 잔여 결합이 있다.

## 8. Configuration / Resource

`bootstrap/src/main/resources`에 `application.yml`, `application-local.yml`, `application-prd.yml`이 있다. 테스트 전용 설정은 `bootstrap/src/test/resources/application.yml`에 있으며 OAuth placeholder와 H2 memory DB를 사용한다. 실제 secret은 소스나 Gradle에 하드코딩하지 않았다.

## 9. 검증 결과

- `:bootstrap:test`: 성공, 528/528
- `build -x test`: 성공
- `bootstrap:bootJar`: 성공
- `ModularArchitectureTest`: 성공
- QueryDSL/JPA/H2/Security 초기화: 테스트에서 성공

## 10. 실제 Blocker

### BLOCKER

1. 해결됨: `web.AdminUserFacade`는 `project.api.AdminMemberProjectQuery`/`AdminMemberProjectPage`를 사용하며 Project/Application use case를 직접 참조하지 않는다. `UserProjectResponseDto`는 Web 응답 모델로서 CLEANUP 대상이다.

### CLEANUP

- legacy `core.*` package 명칭 정리
- Web 테스트를 `web` source set으로 추가 이동
- 각 module의 explicit component/entity scan 정리
- `*-api`/`*-impl` 잔여 문서 및 명칭 정리

### LATER

- 서비스별 database 분리
- service-to-service HTTP
- Gateway/Eureka
- 비동기 messaging 및 eventual consistency

## 11. 현재 단계 평가

논리적 계약 정리와 물리적 Gradle source 분리는 상당 부분 완료되었다. 그러나 Web–Project 간 pagination baseline 내부 use case 의존이 남아 있어 완전한 compile-time bounded-context 경계는 아직 아니다.

## 12. 최종 판정

`READY_FOR_PHYSICAL_MODULARIZATION`

## 2026-09-16 최신 검증 부록

이 부록은 위의 초기 감사 수치를 현재 소스 기준으로 갱신한다. 비즈니스 모듈은 `bootstrap`, `platform`, `web`, `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel`로 물리적으로 분리되어 있으며 Root business production/test Java source는 0개다.

- 컨텍스트 간 foreign Repository/Entity/Aggregate/internal import: 0건
- 비즈니스 core의 HTTP representation import: 0건
- `web`의 컨텍스트 outbound import: 0건
- `platform`의 비즈니스 컨텍스트 import: 0건
- `readmodel.outbound`의 외부 QueryDSL Q 타입 참조: 단일 H2 단계에서 허용된 READ-ONLY 예외
- 전체 테스트: 537개 실행, 실패 0, skip 0
- 전체 `build` 및 `bootstrap:bootJar`: PASS
- 강화된 `ModularArchitectureTest`: PASS
- local 실행: H2 `jdbc:h2:mem:devhub`, 28개 JPA repository, Security, Tomcat `/api` 초기화 PASS

### 현재 분류

**BLOCKER**: 없음. 현재 물리적 모듈화와 빌드를 막는 실제 구조적 blocker는 확인되지 않는다.

**CLEANUP**: `web`이 일부 컨텍스트 inbound use case 타입을 `.core`에서 참조하는 잔여 경계 강화, 테스트의 `small`/`medium` 레거시 package 명칭 정리.

**LATER**: Readmodel 외부 Q 타입 제거(독립 DB/서비스 추출 시), 외부 OAuth·메일 credential의 환경 변수화, Gateway/Service Discovery 등 MSA 인프라.

최신 상태 판정: `READY_FOR_PHYSICAL_MODULARIZATION` (물리적 모듈화 완료 후 검증됨)

## 13. 권장 다음 작업

`UserProjectResponseDto`의 물리적 Web 패키지 이동과 module별 테스트 소유권 정리를 수행한 뒤, 물리적 모듈 경계를 강화한다.
