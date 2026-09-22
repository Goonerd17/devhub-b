# MSA 전환 진행 상황

## 2026-09-21 — HTTP 서비스 디스커버리 및 Gateway 라우팅

- `auth-server`, `member-server`, `project-server`, `community-server`, `admin-server`, `notification-server`, `query-server`에 Spring Cloud Config Client와 Eureka Client를 적용했다.
- 각 서비스의 로컬 기본 포트와 H2 데이터베이스 이름을 분리했다. 포트는 환경 변수로 재정의할 수 있다.
- Gateway에 인증, 회원, 프로젝트, 커뮤니티, 관리자, 알림, 통합 조회 API 라우트를 추가했다. `/api/user/signup`은 auth로 먼저 라우팅하고 나머지 `/api/user/**`는 member로 라우팅한다.
- 전체 `test`와 전체 `bootJar`가 성공했다.
- HTTP 라우팅은 준비됐지만 서비스 간 Java 프로젝트 의존성은 아직 남아 있으므로 상태는 `IN_PROGRESS`다.

## 2026-09-21 — Community → Member/Auth 동기 호출 경계 분리

- Community가 Member의 Java API 타입과 구현 모듈을 직접 참조하던 의존성을 제거했다.
- 공개 프로필·커뮤니티 프로필 조회 계약을 `common-module`에 두고 Community outbound HTTP client가 Member 내부 API를 호출하도록 변경했다.
- 게시글 상세의 이메일 조회도 동일한 outbound client를 통해 Auth 내부 API로 호출하도록 변경했다.
- Member와 Auth에는 `/internal/**` 전용 조회 endpoint를 추가했으며 Gateway에는 노출하지 않았다.
- Community의 `member-server` Gradle 의존성을 제거했고 Community source의 `teamdevhub.devhub.member` 참조가 0건임을 확인했다.
- 샌드박스 밖에서 Gradle 캐시 접근을 허용한 뒤 `community-server`, `member-server`, `auth-server` 관련 테스트가 성공했다.

## 2026-09-21 — Auth → Notification 동기 호출 경계 분리

- Auth의 인증 코드 발송 계약을 `common-module`로 옮기고 Notification 구현 모듈에 대한 production 의존성을 제거했다.
- Auth outbound HTTP client가 Notification의 `/internal/notifications/verifications` endpoint를 호출하도록 변경했다.
- 내부 endpoint는 Gateway 공개 라우트에 포함하지 않았다.
- 기존 테스트 fixture 때문에 `notification-server`는 Auth의 `testImplementation`에만 남겼다.
- `auth-server:test`와 `notification-server:test`가 성공했다.
- 두 경계 변경을 포함한 전체 `test bootJar` 검증이 성공했다(74 tasks, 실패 0).

## 2026-09-21 — Project → Media/Auth 조회 경계 분리

- Project의 파일 메타데이터 조회를 Media 내부 HTTP API로 전환하고 `media-server` production 의존성을 제거했다.
- 파일 경로를 포함하는 내부 전용 `/internal/media/{fileGuid}/metadata` endpoint를 추가했으며 Gateway에는 노출하지 않았다.
- Project의 프로젝트 작성자 이메일 조회를 Auth 내부 HTTP API로 전환하고 `auth-server` production 의존성을 제거했다.
- `project-server:test`, `media-server:test`가 성공했다.

## 2026-09-22 — Project → Member 조회 경계 분리

- 프로젝트 소유자, 지원자 공개 프로필, 프로젝트별 사용자 평점 조회를 Member 내부 HTTP API로 전환했다.
- Project에서 Member 역할 enum을 제거하고 인증 principal의 안정된 역할 이름(`ADMIN`)만 비교하도록 변경했다.
- `project-server` production source의 Member package 참조와 `member-server` Gradle 의존성을 제거했다.
- Member 내부 API는 Gateway 외부 라우트에 포함하지 않았다.
- `project-server:test`와 `member-server:test`가 성공했다.
- Project에 남은 다른 비즈니스 서버 production 의존성은 `admin-server` 하나다.
- 변경 후 전체 `test bootJar`가 성공했다(73 tasks, 실패 0).

## 2026-09-22 — Project → Administration 신청서 경계 분리

- 신청서 검색·표준/커스텀 조회·생성·삭제 계약을 `common-module`의 서비스 중립 계약으로 정리했다.
- Project outbound HTTP client가 Administration의 `/internal/application-forms/**` API를 호출하도록 변경했다.
- Project HTTP 응답 모델이 Administration HTTP DTO를 직접 참조하던 결합도 Project 소유 DTO로 교체했다.
- `project-server` production source의 Administration package 참조와 `admin-server` Gradle 의존성을 제거했다.
- Project는 이제 `common-module` 외 다른 비즈니스 서버 Gradle project를 production 의존성으로 갖지 않는다.
- `project-server:test`, `admin-server:test`, 두 서비스 `bootJar`가 성공했다.

## 2026-09-22 — Administration → Community 신고 경계 분리

- 관리자 신고 목록·사용자별 신고·처리 완료 계약을 `common-module`의 서비스 중립 계약으로 정리했다.
- Administration outbound HTTP client가 Community의 `/internal/reports/**` API를 호출하도록 변경했다.
- `admin-server` production source의 Community package 참조와 `community-server` Gradle 의존성을 제거했다.
- 내부 신고 API는 Gateway 공개 라우트에 포함하지 않았다.
- `admin-server:test`와 `community-server:test`가 성공했다.
- Administration에 남은 다른 비즈니스 서버 production 의존성은 `member-server` 하나다.

## 2026-09-22 — Administration → Member/Auth 관리 경계 분리

- 관리자 회원 검색·상세·수정·차단·차단 해제 계약을 `common-module`에 정의하고 Member 내부 HTTP API로 전환했다.
- Admin HTTP 응답과 요청 모델에서 Member domain·command·enum 직접 참조를 제거했다.
- 관리자 비밀번호 초기화도 Auth의 내부 HTTP API로 전환해 Admin 독립 실행 시 로컬 Auth bean을 요구하지 않도록 했다.
- `admin-server` production source의 Member package 참조와 `member-server` Gradle 의존성을 제거했다.
- Admin은 이제 `common-module` 외 다른 비즈니스 서버 Gradle project를 production 의존성으로 갖지 않는다.
- Admin·Member·Auth 테스트와 `admin-server:bootJar`가 성공했다.

## 2026-09-22 — Auth → Administration 약관 동의 경계 분리

- 가입 약관 동의 항목과 저장 command를 `common-module`의 서비스 중립 계약으로 이동했다.
- Auth의 이메일/OAuth 가입 workflow가 Administration 내부 HTTP API로 약관 동의를 저장하도록 변경했다.
- `auth-server` production source의 Administration package 참조와 `admin-server` Gradle 의존성을 제거했다.
- 내부 약관 동의 API는 Gateway 공개 라우트에 포함하지 않았다.
- `auth-server:test`와 `admin-server:test`가 성공했다.
- Auth에 남은 다른 비즈니스 서버 production 의존성은 `member-server` 하나다.

## 2026-09-22 — Auth → Member 인증·가입 경계 분리

- 회원 역할 enum을 `common-module` 보안 계약으로 이동했다.
- 회원 등록, 최초 관리자 존재 확인, 로그인 가능 검증, 로그인 시각 갱신, 현재 역할 조회를 Member 내부 HTTP API로 전환했다.
- Auth production 코드의 Member package 참조와 `member-server` Gradle production 의존성을 제거했다.
- 기존 교차 모듈 테스트 fixture가 사용하는 Member 의존성은 `testImplementation`으로만 격리했다.
- Auth는 이제 `common-module` 외 다른 비즈니스 서버 Gradle project를 production 의존성으로 갖지 않는다.
- `auth-server:test`, `member-server:test`, `auth-server:bootJar`가 성공했다.
- Auth 독립화 이후 전체 `test bootJar`가 성공했다(73 tasks, 실패 0).

## 2026-09-22 — Query → Administration 홈 배너 경계 분리

- Query가 Administration의 `BannerEntity`를 JPQL 문자열로 직접 조회하던 숨은 공유 DB 결합을 제거했다.
- Administration이 노출 가능한 홈 배너 projection을 `/internal/home/banners`로 제공하고 Query가 HTTP로 조회하도록 변경했다.
- `query-server`의 `admin-server` Gradle production 의존성을 제거했다.
- `query-server:test`, `admin-server:test`, `query-server:bootJar`가 성공했다.
- Query에는 Member·Community·Project 데이터에 대한 공유 Entity/DB 조회 결합이 남아 있다.

## 2026-09-22 — Query → Community 홈 게시글 경계 분리

- 인기 게시글·좋아요 집계 QueryDSL 조회를 Community 내부 API로 이동했다.
- 작성자 이름은 Community가 이미 보유한 Member HTTP gateway를 통해 조합하도록 변경했다.
- Query의 Community Q Entity 직접 참조와 `community-server` Gradle production 의존성을 제거했다.
- `query-server:test`, `community-server:test`, `query-server:bootJar`가 성공했다.
- Query에는 Member·Project 데이터에 대한 공유 Entity/DB 조회 결합이 남아 있다.

## 2026-09-22 — Query 홈 프로젝트 조회 경계 분리

- 홈 프로젝트 QueryDSL 조회와 모집 상태 계산을 Project 내부 API로 이동했다.
- Query의 `QProjectEntity`와 `ProjectRecruitStatus` 직접 참조를 제거했다.
- Query·Project 테스트와 `query-server:bootJar`가 성공했다.
- `project-server` Gradle 의존성은 기술 동향 통계가 Project Entity를 JPQL로 직접 조회하는 결합 때문에 아직 유지한다.

## 2026-09-22 — Query → Project/Member 분석 경계 분리

- 프로젝트 수, 신규 기술, 수요 기술, 인기 포지션, 월별 프로젝트 통계는 Project 내부 API가 소유하도록 이동했다.
- 활성 회원 수, 평균 매너 온도, 보유 기술, 포지션별 기술 통계는 Member 내부 API가 소유하도록 이동했다.
- Query는 공유 계약과 HTTP 클라이언트로 두 서비스의 결과를 조합하며, Project/Member 엔티티를 직접 조회하지 않는다.
- `query-server`의 `project-server`, `member-server` Gradle production 의존성을 제거했다.
- 모든 비즈니스 서비스 간 production Gradle 의존성이 제거되었고 `common-module`만 공통 계약 모듈로 남았다.
- 집중 검증 23 tasks와 전체 `test bootJar` 71 tasks가 모두 성공했다.

## 2026-09-22 — 서비스 디스커버리 기반 내부 호출

- Auth, Project, Community, Admin, Query의 내부 `RestClient`에 Spring Cloud LoadBalancer를 적용했다.
- 내부 호출의 기본 주소를 고정 `localhost` 포트에서 Eureka 서비스 ID(`member-server`, `project-server` 등)로 변경했다.
- `services.*.base-url` 속성 재정의는 유지해 로컬·테스트 환경에서 직접 주소를 사용할 수 있다.
- 전체 `test bootJar` 71 tasks가 성공했다.
- 루트 `verifyServiceBoundaries` 검증을 추가해 비즈니스 서비스 간 production Gradle 의존성 재유입을 차단했다.

## 2026-09-22 — 내부 API 서비스 인증

- 모든 내부 HTTP 호출은 `X-Internal-Api-Key` 헤더를 자동 전송한다.
- Auth, Member, Project, Community, Admin, Media, Notification의 `/internal/**` 경로는 전용 Spring Security 체인에서 키를 검증한다.
- 내부 키 비교는 타이밍 공격 노출을 줄이기 위해 상수 시간 비교를 사용하고, 성공 요청에만 `ROLE_INTERNAL_SERVICE` 권한을 부여한다.
- 공통 설정은 `INTERNAL_API_KEY` 환경 변수로 주입하며 로컬 개발 기본값만 제공한다. 운영 환경에서는 반드시 별도 비밀값으로 재정의해야 한다.
- 공통 인증 필터 단위 테스트 3개와 전체 `test bootJar verifyServiceBoundaries` 72 tasks가 성공했다.
- Member 실제 기동 검증에서 키 없는 내부 요청은 HTTP 401, 올바른 키 요청은 HTTP 200을 반환했다.

## 2026-09-22 — 서비스별 CI/CD 이미지 분리

- 루트 Dockerfile은 `SERVICE` build argument로 각 서비스의 실행 JAR만 복사하는 공통 이미지 템플릿으로 변경했다.
- 컨테이너 런타임은 JDK 이미지에서 Java 17 JRE 이미지로 축소했다.
- 컨테이너 프로세스는 root가 아닌 전용 `app` 사용자로 실행한다.
- CI는 테스트를 생략하지 않고 `test bootJar verifyServiceBoundaries`를 먼저 수행한다.
- Auth, Member, Project, Community, Admin, Media, Notification, Query, Discovery, Config, Gateway의 11개 이미지를 matrix로 병렬 빌드·푸시한다.
- 더 이상 생성되지 않는 루트 JAR와 단일 `devhub-b` 배포 매니페스트 자동 갱신을 제거했다. 서비스별 인프라 매니페스트 연결은 인프라 저장소 작업으로 분리한다.

## 2026-09-22 — 전체 서비스 로컬 Compose 구성

- 11개 실행 서비스를 서비스별 이미지로 빌드하고 한 네트워크에서 기동하는 `compose.yml`을 추가했다.
- 컨테이너 내부 포트는 8080으로 통일하고 기존 개발자용 호스트 포트 8080~8088, 8761, 8888 매핑을 유지했다.
- Config Server와 Eureka가 준비된 뒤 업무 서비스가 시작되며, Gateway는 모든 라우팅 대상 컨테이너 시작 이후 기동한다.
- Config/Eureka 주소, 내부 API 키, JWT 키를 컨테이너 환경 변수로 주입한다.
- `.env.example`을 추가하고 운영 비밀값 재사용 금지를 명시했다.
- CI 검증 단계에서 `docker compose config --quiet`를 실행해 Compose 문법과 병합 결과를 검사한다.
- 현재 작업 환경에는 Docker CLI가 없어 `docker compose config/up` 실검증은 수행하지 못했다. 서비스/JAR 목록과 Dockerfile 입력 경로는 정적으로 검증했다.

## 2026-09-22 — Community/Identity/Project 기동 오류 수정

- Identity 단독 기동 실패 원인인 필수 `app.frontend.base-url` 기본값 누락을 `FRONTEND_BASE_URL` 환경변수 기반 설정으로 보완했다.
- Community의 관리자 게시글 조회에서 Member 소유 `UserEntity`를 JPQL로 조인하던 코드를 제거했다.
- Community는 Member 내부 API로 회원 상태별 GUID와 페이지 회원 프로필을 조회하고, 게시글/신고 데이터만 자체 DB에서 조회한다.
- Project 관리자 지원자 조회에서 잔존하던 `UserEntity` 조인을 제거했다.
- 수정 후 Identity, Community, Project 모두 독립 포트에서 `Started ...Application` 로그를 확인했다.
- 전체 `test bootJar verifyServiceBoundaries` 72 tasks가 성공했다.

## 2026-09-22 — 잔여 서비스 기동 점검

- Query, Admin, Notification, Media를 독립 설정으로 기동해 모두 `Started ...Application`을 확인했다.
- Query/Notification/Media에서 발견된 Bean Validation provider 누락을 각 서비스의 `spring-boot-starter-validation` 의존성으로 보완했다.
- Notification의 Thymeleaf 템플릿 경고는 기동 실패가 아니며, 실제 템플릿 사용 여부를 별도 기능 검증 대상으로 남겼다.

## 상태 기준

- `NOT_STARTED`: 서비스 경계 이전 전
- `IN_PROGRESS`: 독립 실행 또는 API 이전 중이며 Java 호출 경계가 남음
- `EXTRACTED`: 코드, 설정, 데이터베이스와 네트워크 경계 이전 완료
- `VERIFIED`: 독립 기동과 Gateway 경유 동작 확인

## 서비스별 상태

| 서비스 | 상태 | API 소유권 | 독립 빌드·설정·H2 | Eureka | Config Server | Gateway | 서비스 간 Java 의존성 | 기동 |
|---|---|---|---|---|---|---|---|---|---|
| auth-server | IN_PROGRESS | 인증·OAuth | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 남음 | 확인 |
| member-server | IN_PROGRESS | 회원 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 테스트 의존성 일부 남음 | 확인 |
| project-server | IN_PROGRESS | 프로젝트·지원서 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 남음 | 확인 |
| community-server | IN_PROGRESS | 게시판·댓글·신고 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 남음 | 확인 |
| admin-server | IN_PROGRESS | 관리자·약관·배너 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 없음 | 확인 |
| media-server | IN_PROGRESS | 파일 | `test`, `bootJar`, H2 `jdbc:h2:mem:media` 확인 | 등록 확인 | 수신 확인 | `/api/files/**` 확인 | 다른 비즈니스 모듈 메인 의존성 없음 | 확인 |
| notification-server | IN_PROGRESS | 알림 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 없음 | 확인 |
| query-server | IN_PROGRESS | 통합 조회·홈·트렌드 | `testClasses`, `bootJar`, H2 기동 확인 | 후속 적용 | 후속 적용 | 라우트 후속 적용 | 남음 | 확인 |

## 인프라

| 앱 | 상태 | 확인 |
|---|---|---|
| discovery-server | VERIFIED | 독립 `bootJar`, 기동, Eureka HTTP 200 |
| config-server | VERIFIED | 독립 `bootJar`, 기동, `/media-server/default`와 `/gateway-server/default` HTTP 200 |
| gateway-server | VERIFIED | 독립 `bootJar`, 기동, Config Server 라우트 수신, Eureka 경유 media-server 요청 확인 |

## 모듈 이름

`identity → auth-server`, `administration → admin-server`, `readmodel → query-server`, `platform → common-module`로 변경했다. Java 패키지도 `identity → auth`, `administration → admin`, `readmodel → query`, `platform → shared`로 변경했다. `common-module`은 배포 서비스가 아니므로 `bootJar`와 `bootRun`을 비활성화했다. 레거시 `web-server`, `bootstrap-server`, `DevhubApplication`은 제거했다.

검증용 `.gradle-phase1`, `.gradle-verify` 디렉터리도 제거하고 기본 Gradle 캐시만 사용한다.

## 현재 Gradle 서비스 의존성

- `auth-server → member-server, admin-server, notification-server`
- `project-server → member-server, admin-server`
- `community-server → member-server, auth-server`
- `query-server → admin-server, member-server, community-server, project-server`
- `media-server`, `member-server`, `notification-server`, `admin-server`는 다른 비즈니스 모듈에 대한 메인 소스 Gradle 의존성이 없다.
- 모든 비즈니스 모듈은 현재 `common-module`에 의존한다. 이를 별도 배포 앱으로 만들지 않았다.

다른 비즈니스 모듈 의존성이 없는 `media-server`를 첫 추출 대상으로 선택했다.

## 파일 API 소유권

| 기존 외부 URL | 대상 서비스 내부 URL | Gateway 처리 |
|---|---|---|
| `POST /api/files` | `POST /files` | `/api` 제거 후 `lb://media-server` |
| `GET /api/files/{fileGuid}/meta` | `GET /files/{fileGuid}/meta` | 동일 |
| `GET /api/files/{fileGuid}` | `GET /files/{fileGuid}` | 동일 |
| `GET /api/files/{fileGuid}/download` | `GET /files/{fileGuid}/download` | 동일 |
| `DELETE /api/files/{fileGuid}` | `DELETE /files/{fileGuid}` | 동일 |

파일 Controller, DTO, 퍼사드와 관련 단위 테스트를 media-server로 옮겼다. 공통 API 응답 형식과 성공 코드는 common-module에 배치했다.

## 보안 결정

- 인증과 JWT 발급은 향후 `auth-server`가 소유한다.
- media-server는 JWT HS256 서명, 만료, `token_type=ACCESS`를 자체 검증한다. 파일 GET은 공개하고 업로드·삭제는 인증을 요구한다.
- 익명 업로드는 `AUTH_INVALID`, 잘못된 Bearer 토큰은 `TOKEN_INVALID` 오류 코드로 응답한다.
- gateway-server는 현재 라우팅만 담당한다. 이후 공통 인증 정책을 검토하되 서비스의 자체 검증은 유지한다.
- 개발용 JWT 기본값은 운영 비밀값이 아니다. 운영 값은 환경 변수로 주입해야 한다.

## 검증 기록

- 현재 Gradle `projects`에서 12개 모듈을 인식한다. `clean testClasses bootJar`가 성공했고 `common-module:bootJar`는 의도대로 건너뛴다.
- config-server, discovery-server, gateway-server와 8개 비즈니스 서버의 실행 JAR를 동시에 기동해 모두 `Started ...Application` 로그를 확인했다.
- media-server는 Config Server의 설정을 받고 H2 `jdbc:h2:mem:media`에 접속했다. Eureka에 `MEDIA-SERVER`로 8086 포트 등록 확인.
- Gateway 경유 익명 파일 업로드는 401, 유효한 개발용 ACCESS JWT 업로드는 200, 공개 메타 조회는 200이었다. 파일 없음 응답은 기존 오류 코드 `ERR.DVH.0064`를 유지했다.
- 이전 단계에서 `:media:bootRun` 기동과 Gateway 경유 다운로드 200을 확인했다. 모듈명 변경 후 JAR 기동·라우팅을 다시 확인했다.
- 임시 포트 `0`으로 재기동할 때 Eureka가 종료된 인스턴스의 포트를 잠시 제공했다. 로컬 기본 포트를 8086으로 정하고 `MEDIA_SERVER_PORT`로 변경할 수 있게 했다.

## 다음 실행 단위

1. 각 서비스의 Controller와 DTO를 해당 Bounded Context로 이전하고 Gateway 라우트를 추가한다.
2. 서비스 간 Gradle Java 의존성을 HTTP 계약으로 교체한다.
3. 각 서비스의 Config Client, Eureka 등록, 서비스별 H2 DB를 적용한다.
4. Gateway 경유 API를 검증한 뒤 서비스 상태를 `VERIFIED`로 올린다.
