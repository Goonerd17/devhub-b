# MSA 전환 진행 상황

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

`identity → auth-server`, `administration → admin-server`, `readmodel → query-server`, `platform → shared-kernel`로 변경했다. Java 패키지도 `identity → auth`, `administration → admin`, `readmodel → query`, `platform → shared`로 변경했다. `shared-kernel`은 배포 서비스가 아니므로 `bootJar`와 `bootRun`을 비활성화했다. 레거시 `web-server`, `bootstrap-server`, `DevhubApplication`은 제거했다.

검증용 `.gradle-phase1`, `.gradle-verify` 디렉터리도 제거하고 기본 Gradle 캐시만 사용한다.

## 현재 Gradle 서비스 의존성

- `auth-server → member-server, admin-server, notification-server`
- `project-server → member-server, admin-server`
- `community-server → member-server, auth-server`
- `query-server → admin-server, member-server, community-server, project-server`
- `media-server`, `member-server`, `notification-server`, `admin-server`는 다른 비즈니스 모듈에 대한 메인 소스 Gradle 의존성이 없다.
- 모든 비즈니스 모듈은 현재 `shared-kernel`에 의존한다. 이를 별도 배포 앱으로 만들지 않았다.

다른 비즈니스 모듈 의존성이 없는 `media-server`를 첫 추출 대상으로 선택했다.

## 파일 API 소유권

| 기존 외부 URL | 대상 서비스 내부 URL | Gateway 처리 |
|---|---|---|
| `POST /api/files` | `POST /files` | `/api` 제거 후 `lb://media-server` |
| `GET /api/files/{fileGuid}/meta` | `GET /files/{fileGuid}/meta` | 동일 |
| `GET /api/files/{fileGuid}` | `GET /files/{fileGuid}` | 동일 |
| `GET /api/files/{fileGuid}/download` | `GET /files/{fileGuid}/download` | 동일 |
| `DELETE /api/files/{fileGuid}` | `DELETE /files/{fileGuid}` | 동일 |

파일 Controller, DTO, 퍼사드와 관련 단위 테스트를 media-server로 옮겼다. 공통 API 응답 형식과 성공 코드는 shared-kernel에 배치했다.

## 보안 결정

- 인증과 JWT 발급은 향후 `auth-server`가 소유한다.
- media-server는 JWT HS256 서명, 만료, `token_type=ACCESS`를 자체 검증한다. 파일 GET은 공개하고 업로드·삭제는 인증을 요구한다.
- 익명 업로드는 `AUTH_INVALID`, 잘못된 Bearer 토큰은 `TOKEN_INVALID` 오류 코드로 응답한다.
- gateway-server는 현재 라우팅만 담당한다. 이후 공통 인증 정책을 검토하되 서비스의 자체 검증은 유지한다.
- 개발용 JWT 기본값은 운영 비밀값이 아니다. 운영 값은 환경 변수로 주입해야 한다.

## 검증 기록

- 현재 Gradle `projects`에서 12개 모듈을 인식한다. `clean testClasses bootJar`가 성공했고 `shared-kernel:bootJar`는 의도대로 건너뛴다.
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
