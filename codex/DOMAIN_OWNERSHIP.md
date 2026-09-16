# DevHub 도메인 소유권 명세

## 적용 원칙

이 문서는 현재 구현과 `MONOLITH_TO_MSA_ANALYSIS.md`를 근거로 한 목표 모듈러 모놀리스의 소유권 기준이다. 최종 Gradle 경계는 컨텍스트당 하나(`identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel`)이며, 공개 교류는 `<context>.api`만 사용한다. Entity, repository, QueryDSL DAO, mapper, adapter는 owner의 `outbound`에 비공개로 둔다.

## Identity

- 책임: 로그인, OAuth, token/refresh token, verification, credential와 Spring Security adapter.
- 소유: `core/auth/**`, `outbound/auth/**`, `outbound/security/**`; `EmailCredentialEntity`, `OAuthCredentialEntity`, `RefreshTokenEntity`, `VerificationEntity`; `JpaEmailCredentialRepository`, `JpaOAuthCredentialRepository`, `JpaRefreshTokenRepository`, `JpaVerificationRepository`.
- 공개 후보: 현재 멤버의 role/status 조회에 필요한 최소 result, password login 가능 여부 query, credential 생성/삭제 command, verification delivery request.
- 비공개: credential Entity/Repository, JWT codec/claims, OAuth provider adapter, security filter.
- 소비자: Member(가입/탈퇴), Community(작성자 공개 연락처가 실제로 필요할 때만), Project(현재 credential 조회 사용 제거 후 최소 query), Notification(전달 요청).
- workflow: token issuance/refresh와 verification state는 Identity transaction이 소유한다.

## Member

- 책임: 사용자 profile, role/status, skill/position, review, 가입과 탈퇴의 member state.
- 소유: `core/user/**`, `outbound/user/**`; `UserEntity`, `UserPositionEntity`, `UserSkillEntity`, `UserReviewEntity`; `JpaUserRepository`, `JpaUserPositionRepository`, `JpaUserSkillRepository`, `JpaUserReviewRepository`, `UserQueryDao`.
- 공개 API: `MemberPublicProfileQuery`/`MemberPublicProfile`은 유지한다. 추가 후보는 현재 role query, member eligibility/status query, review command다.
- 비공개: `User`, `UserRole`, user repository/entity/mapper/query DAO.
- 소비자: Identity, Project, Community, Administration, Readmodel.
- workflow: member profile 변경과 member review state는 Member transaction이 소유한다. 가입 흐름의 전체 owner는 아래 미결 결정이다.

## Project

- 책임: Marketplace project와 Recruitment Application을 하나의 lifecycle로 관리한다.
- 소유: `core/project/**`, `core/application/**`, `outbound/project/**`, `outbound/application/**`; `ProjectEntity`, `ProjectLikeEntity`, `ProjectRequirementEntity`, `ProjectSkillEntity`, `ProjectApplicationEntity`, `ProjectApplicationAnswerEntity`, `ProjectApplicationFormEntity` 및 해당 JPA repository/QueryDSL DAO.
- 공개 후보: project summary/detail read, project application eligibility/command, approved participant/review eligibility.
- 비공개: 모든 project/application Entity, repository, mapper, requirement/application internal model.
- 소비자: Member(리뷰/관리 조회), Administration(form 연계), Community/Readmodel(읽기 projection).
- workflow: project 생성/수정, 지원/승인/취소는 Project가 state owner다. form/member/media와의 조합 책임은 아래 미결 결정이다.

## Community

- 책임: board, comment, board like, report와 moderation 대상 정보.
- 소유: `core/board/**`, `core/report/**`, `outbound/board/**`, `outbound/report/**`; `BoardEntity`, `CommentEntity`, `BoardLikeEntity`, `ReportEntity` 및 해당 repository/adapter.
- 공개 후보: board/comment/report summary 및 moderation projection.
- 비공개: board/comment/report Entity와 repository.
- 소비자: Member administration, Readmodel.
- workflow: 게시물/댓글/신고 변경은 Community transaction이 소유하며 작성자 표시는 Member public profile query만 사용한다.

## Administration

- 책임: banner, common code, application form template, terms/agreement.
- 소유: `core/admin/**`, `core/terms/**`, `outbound/admin/**`, `outbound/terms/**`; Banner/CommonCode/ApplicationForm/ApplicationFormItem/Terms/TermsAgreement Entity와 repository/QueryDSL DAO.
- 공개 후보: form template command/query, terms agreement command/query, banner/code read model.
- 비공개: form/terms Entity와 repository.
- 소비자: Member(가입 약관), Project(지원 양식), Readmodel(banner).
- workflow: form/terms 변경은 Administration transaction이 소유한다.

## Media

- 책임: file metadata와 storage I/O.
- 소유: `core/file/**`, `outbound/file/**`; `FileEntity`, `JpaFileRepository`, `LocalFileStorage`, mapper/adapter.
- 공개 후보: `fileGuid` 기준 metadata read와 upload/delete command.
- 비공개: file Entity/repository/storage implementation.
- 소비자: Member, Project, Community(필요한 file reference만).

## Notification

- 책임: inbox notification persistence와 이메일 전달 adapter.
- 소유: `core/notification/**`, `outbound/notification/**`; `NotificationEntity`, `JpaNotificationRepository`, email adapter/template.
- 공개 후보: notification delivery command, inbox query.
- 비공개: email template/adapter/entity/repository.
- 소비자: Identity verification workflow와 각 context의 notification 요청.
- workflow: delivery 요청은 Notification이 처리하지만 verification의 business state는 Identity가 소유한다.

## Readmodel

- 책임: home composition과 skill trend analytics의 읽기 전용 projection.
- 소유: `core/home/**`, `core/skilltrend/**`, `outbound/home/**`, `outbound/skilltrend/**` 및 home/analytics DAO.
- 공개 후보: home/analytics query result.
- 예외: `HomeBoardQueryDaoImpl`의 User/Board/BoardLike QueryDSL join과 `SkillTrendQueryDaoImpl`의 Project/User JPQL은 shared H2에 대한 **READ ONLY 임시 예외**다. Readmodel은 foreign state를 쓰지 않으며 write context는 Readmodel에 의존하지 않는다.

## Platform 및 Web/Bootstrap

- Platform: ID/time, tracing/logging 같은 business-neutral 기술만 소유한다. `IdentifierProvider`, `SystemIdentifierProvider`, `TimeProvider`, `SystemTimeProvider`는 이미 Platform 소유다.
- Web: controller, HTTP request/response DTO, resolver, validator, response wrapper, exception translation을 소유한다. business core는 Web 타입을 import하지 않는다.
- Bootstrap: `DevhubApplication`과 Spring bean composition을 소유한다. 하나의 Spring Boot process와 하나의 H2 database를 조립한다.

## 승인된 cross-context workflow

### 가입 및 OAuth 가입

- Owner: Identity.
- Identity는 local Spring transaction 안에서 Member 생성, Identity credential 생성, 필수 Terms Agreement 기록을 orchestration한다.
- 필수 단계 하나라도 실패하면 전체 workflow를 rollback한다.
- Identity는 Member repository/entity/domain internal을 사용하지 않고 `member.api` semantic command/query를 사용한다. Terms는 `administration.api` contract로 처리한다.

### Project 생성 및 수정

- Owner: Project.
- Project는 local Spring transaction 안에서 필요한 Member 권한/정보, Application Form, Media metadata capability를 orchestration한다.
- 필수 작업 실패 시 Project 생성/수정을 rollback한다.
- Project는 `member.api`, `administration.api`, `media.api`의 최소 semantic contract만 사용한다.

### Member administration

- Project/Application/Report 정보를 조합하는 member administration은 Administration의 cross-context **READ workflow**다.
- Administration은 `member.api`, `project.api`, `community.api`만 사용한다.
- 이 조회에는 하나의 강한 transaction을 요구하지 않으며 HTTP DTO가 아닌 application result를 반환한다.
