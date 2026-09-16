# 모듈화 차단 요인 해소 검토

## 검토 결과

사용자가 가입/OAuth 가입(Identity owner), Project 생성/수정(Project owner), Member administration(Administration read workflow)의 owner와 local transaction 정책을 승인했다. 따라서 기존의 workflow decision blocker는 해소됐고, 다음 단계는 해당 결정을 semantic contract와 controller/application mapping에 실제 반영하는 코드 리팩터링이다. physical module migration은 아직 수행하지 않는다.

## 차단 요인별 상태

| 원래 차단 요인 | 상태 | 현재 근거 | 필요한 결과 의존성 |
|---|---|---|---|
| Core → HTTP DTO | PARTIAL | 14개 core 파일이 `teamdevhub.devhub.api.*` import. `ProjectApplicationFacade`는 `CreateApplicationRequestDto`, `DataApiResponseDto`, `PageResponseDto`와 여러 response DTO를 직접 사용 | `web controller → context command/query → context result → web response mapper` |
| Member → Identity credential repository | BLOCKED | `UserProfileFacade → EmailUserCredentialRepository` | `member → identity.api.PasswordLoginAvailabilityQuery` |
| Identity → Member repository/domain | BLOCKED | `AuthenticationService → UserRepository`, `User`; credential/auth facade도 Member internal type을 사용 | `identity → member.api.CurrentMemberRoleQuery` 및 가입 command/query |
| Community → Member/Identity persistence | BLOCKED | `BoardService`와 `BoardQueryService`가 `UserRepository`, `EmailUserCredentialRepository`, `EmailUserCredential`를 사용 | `community → member.api.MemberPublicProfileQuery`; Identity 공개 연락처/가용성 query는 privacy policy 확정 후 최소화 |
| Project → Member/Identity/Administration/Media internal model | BLOCKED | `ProjectFacade`, `AdminProjectFacade`가 User/UserRole, form model/use case, FileUseCase, credential repository를 혼합 | Project 생성/수정 workflow owner와 실패/즉시일관성 policy 결정 후 각 `.api` contract |
| Member administration → Project/Community | BLOCKED | `AdminUserFacade`가 Project/Application/Report internal use case/model을 직접 사용 | Member administration read workflow 또는 Administration read workflow owner 결정 |
| Readmodel foreign persistence | RESOLVED (문서화) | `HomeBoardQueryDaoImpl`, `SkillTrendQueryDaoImpl`가 foreign QueryDSL/JPQL entity를 read-only 사용 | Readmodel outbound 내부의 temporary read-only exception; write context 역의존 금지 |
| Platform shared code | PARTIAL | identifier/time은 Platform으로 이동. audit, JPA base/converter, logging 등은 owner 판단 전 Root에 남음 | business-neutral인 항목만 Platform으로 이동 |
| 자동 architecture verification | BLOCKED | 현재 source set과 package가 목표 context package가 아니며 위반이 존재 | code cleanup 후 strict ArchUnit; 현재 위반을 허용하는 superficial rule 금지 |

## 이미 재사용 가능한 계약

`member.api.MemberPublicProfileQuery`와 `MemberPublicProfile`은 유지한다. `CommentService`는 이미 이 계약으로 Member repository 직접 접근을 제거했다. 이 계약은 게시물/댓글 작성자 이름과 profile display라는 같은 의미의 query에만 재사용할 수 있으며, role, credential, status, review eligibility를 generic user service로 확장하면 안 된다.

## 승인된 workflow 결정

다음 세 가지는 사용자 승인으로 확정됐다.

1. **가입/OAuth 가입**: Member user 생성, Identity credential 생성, Administration terms agreement가 하나의 local transaction에서 모두 성공해야 하는가? 그렇다면 owner는 Member인가 Identity인가?
2. **Project 생성/수정**: Member role/profile, Administration application form, Media file metadata가 실패할 때 Project creation/update를 전부 취소해야 하는가? Project가 workflow owner인가?
3. **Member administration**: `AdminUserFacade`의 Project/Application/Report 조회는 Member 기능인가, Administration의 cross-context read workflow인가?

이제 contract는 이 owner와 rollback 정책을 보존하도록 구현해야 한다.

## 검증

- 현재 소스에서 core→HTTP import 파일 14개를 확인했다.
- 실제 direct foreign repository/domain import를 `UserProfileFacade`, `AuthenticationService`, `BoardService`, `BoardQueryService`, `ProjectFacade`, `AdminProjectFacade`에서 확인했다.
- Readmodel의 foreign Entity QueryDSL/JPQL 접근을 `HomeBoardQueryDaoImpl`, `SkillTrendQueryDaoImpl`에서 확인했다.
- 이번 검토에서는 production/test/Gradle을 변경하지 않았으므로 build를 다시 실행하지 않았다.
- 기존 최신 build 성공 기록과 별개로, startup은 `${oauth.github.client-id}` 누락이 PRE_EXISTING 문제로 남아 있다.

NOT_READY_FOR_PHYSICAL_MODULARIZATION

## 실제 코드 refactoring 진행 업데이트 (2026-09-15)

이번 변경에서는 Identity→Member 역할 의존성 체인을 실제 production code에서 정리했다.

| 항목 | Before | After | 상태 |
|---|---:|---:|---|
| Core → HTTP representation | 14 | 0 (package 기반 재검색) | PARTIAL — facade의 물리적 web 이동 및 HTTP-independent result 분리는 남음 |
| Identity → Member `UserRepository`/`User` | 1/1 | 0/0 (`AuthenticationService`) | RESOLVED |
| Identity/Security → Member 내부 `UserRole` | 다수 | 0 | RESOLVED — `member.api.MemberRole` 사용 |
| Community → foreign repository/entity | 3개 이상 | `BoardService`, `BoardQueryService` 직접 접근 제거 | RESOLVED — `member.api`/`identity.api` semantic query 사용 |
| Project → foreign context internal/persistence | 다수 | 미변경 | BLOCKED |

### 이번에 추가·재사용한 계약

- `member-api: teamdevhub.devhub.member.api.MemberRole`
- `member-api: teamdevhub.devhub.member.api.CurrentMemberRoleQuery`
- Member 구현: `CurrentMemberRoleService`

`CurrentMemberRoleService`만 Member 내부 `UserRole`과 `UserRepository`를 사용하고 외부에는 `MemberRole`을 반환한다. Identity의 JWT/Security 모델은 계약 타입으로만 역할을 표현하며 기존 claim/authority 문자열을 유지한다.

### 남은 blocker

`UserCredentialService`와 `AuthFacade`의 `SignupUserCommand`, `UpdatePasswordCommand`, `UserLoginUseCase` 등 Member 내부 application 계약 참조, `BoardService`/`BoardQueryService`의 foreign persistence 접근, `ProjectFacade`/`AdminProjectFacade`의 Member·Administration·Media·Identity 내부 타입 접근은 아직 제거되지 않았다. 따라서 최종 판정은 계속 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이다.

Community 변경 후 `BoardService`는 `MemberCommunityProfileQuery`, `MemberEmailQuery`를 사용하고 `BoardQueryService`는 기존 `MemberPublicProfileQuery`를 사용한다. Community production source의 Member/Identity Repository·Entity 직접 import는 0건이다. 단, 통합 테스트의 Spring wiring 재검증은 Root 컴파일 경합 해소 후 수행해야 한다.
## 2026-09-16 최신 코드 검증

- `UploadFileCommand`의 `MultipartFile` 의존성을 제거했다. multipart → command 변환은 `web`의 `UploadFileRequestDto`가 담당한다.
- 물리 모듈 source set을 기준으로 한 `ModularArchitectureTest`를 추가/보완했다.
- 아키텍처 테스트: 통과.
- 전체 빌드(`build -x test`): 통과.
- 전체 테스트: 528개 중 71개 실패. `${jwt.secret.key}`, `${oauth.github.client-id}` 미설정에 따른 ApplicationContext 실패가 대부분이며, `AuditorAwareProviderTest` 기대치 불일치 1건이 남아 있다.
- 교차 애플리케이션 계약(Identity–Terms/Notification, Project–Administration Form 등)은 아직 레거시 `core.*` 타입을 참조하는 부분이 있어 최종 물리 모듈화 전 추가 정리가 필요하다.

현재 판정: `NOT_READY_FOR_PHYSICAL_MODULARIZATION`
## 최신 코드 검증 보완 (2026-09-16)

Project가 ApplicationForm 생성/조회에 Administration 내부 command/entity를 직접 사용하던 일부 경로를 semantic API로 전환했다. `CreateProjectCommand`와 `UpdateProjectCommand`는 `administration.api.ApplicationFormDefinition`을 사용하고, Web의 목록 조회는 `ApplicationFormCatalogQuery`가 반환하는 `ApplicationFormPage`/`ApplicationFormView`를 사용한다. `:project:compileJava`, `:web:compileJava`, `:bootstrap:test`(528개) 모두 성공했다.

현재 최종 판정은 여전히 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이다. `AdminUserFacade`의 다중 context 내부 모델 조합과 Signup Terms 계약은 추가 semantic contract 설계가 필요하다.
## Identity 관리자 비밀번호 계약 (2026-09-16)

`AdminUserFacade → UserCredentialUseCase` 직접 내부 의존을 `identity.api.credential.AdminPasswordReset`으로 교체했다. 계약은 `reset(userGuid, newPassword)` 하나만 노출하며 Identity 구현체가 기존 로직을 위임한다. 컴파일 및 전체 528개 테스트가 성공했다. 최종 상태는 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이며 AdminUserFacade의 Project/Application/Report 조합과 Signup Terms 경계는 남아 있다.
## Community 신고 처리 계약 (2026-09-16)

`AdminUserFacade → ReportUseCase` 직접 의존 중 명령 경로를 `community.api.ReportProcessing`으로 교체했다. 조회 경로(`ReportQueryUseCase`, `Report` 모델)는 아직 Administration cross-context read contract로 완전히 분리되지 않아 최종 상태는 `NOT_READY_FOR_PHYSICAL_MODULARIZATION`이다.
## Report 조회 계약 전환 (2026-09-16)

Admin 사용자 관리의 신고 조회를 `community.api.ReportQuery`/`ReportPage`/`ReportView` 기반으로 전환했다. Web 계층에서 Community 내부 `Report`와 `ReportQueryUseCase`를 직접 참조하던 의존성을 제거했으며, 기존 REST 응답 필드와 페이지 메타데이터는 유지한다. 컴파일 검증 성공. 사용자 프로젝트/Application 조합 및 Signup Terms 경계는 여전히 남은 blocker다.
## Report 조회 blocker 갱신 (2026-09-16)

Report 조회 경로는 `community.api.ReportQuery` 기반으로 전환되어 RESOLVED로 분류할 수 있다. Report Entity와 내부 QueryUseCase는 Community 내부에 남아 있으며, Web에는 `ReportView`/`ReportPage`만 노출된다. 전체 528개 테스트가 통과했다. AdminUserFacade의 Project/Application 조합 및 Signup Terms 경계는 여전히 PARTIAL이다.
## 현재 수치 (2026-09-16)

| 항목 | 현재 상태 |
|---|---:|
| Core → HTTP representation | 0 |
| Identity → Member 내부 의존 | 0 (검색 기준) |
| AdminUserFacade → Community Report 내부 의존 | 0 |
| Project → Administration 내부 ApplicationForm command | 0 |
| 전체 테스트 | 528/528 통과 |
| 최종 준비 상태 | NOT_READY_FOR_PHYSICAL_MODULARIZATION |

남은 내부 조합은 `AdminUserFacade`의 Project/Application 조회이며, 결과 모델의 책임과 공개 범위를 추가로 설계해야 한다.
