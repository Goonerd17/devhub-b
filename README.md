# DevHub Backend

개발자 프로젝트 매칭 및 커뮤니티 서비스의 백엔드입니다. Java 17, Spring Boot 3.5.7, Gradle을 사용합니다. 이 저장소는 기존 단일 애플리케이션을 물리적 모듈로 분리한 뒤 MSA로 전환하는 중입니다. **현재 전체 비즈니스 기능이 Gateway를 통해 동작하는 완성된 MSA는 아닙니다.**

## 현재 구조 (2026-09-18)

`settings.gradle`에는 12개 하위 프로젝트가 등록되어 있습니다.

| 구분 | 모듈 | 책임 |
|---|---|---|
| 공통 라이브러리 | `common-module` | 공통 계약과 기술 구성. 독립 배포 대상이 아님 |
| 비즈니스 서버 | `auth-server`, `member-server`, `project-server`, `community-server`, `admin-server`, `media-server`, `notification-server`, `query-server` | 인증, 회원, 프로젝트, 커뮤니티, 관리, 파일, 알림, 통합 조회 |
| 인프라 서버 | `config-server`, `discovery-server`, `gateway-server` | 중앙 설정, Eureka, 외부 요청 라우팅 |

각 비즈니스 모듈은 실행 진입점과 JAR을 갖습니다. HTTP 계층은 `media-server`의 파일 API, `auth-server`의 인증·회원가입 API, `member-server`의 사용자 프로필 API, `notification-server`의 알림 API, `admin-server`의 배너·공통코드·지원서 양식·약관·사용자 관리·비밀번호 초기화·신고 관리 API, `community-server`의 게시판·댓글·신고·관리자 게시판 API, `project-server`의 프로젝트 생성·조회·수정·삭제·지원서·사용자 프로젝트 목록 API, `query-server`의 홈·스킬 트렌드 API까지 이관되어 있습니다. 따라서 현재 브랜치는 기능 API가 완성된 MSA가 아니라 **도메인 코드 물리 분리와 HTTP API 추출이 진행된 중간 상태**입니다.

또한 `auth-server`, `project-server`, `community-server`, `query-server`에는 다른 비즈니스 모듈에 대한 Gradle 구현 의존성과 직접 Java 호출이 남아 있습니다. `member-server`에는 다른 비즈니스 모듈에 대한 테스트 의존성도 남아 있습니다. 따라서 **독립 실행 가능**과 **서비스 경계 분리 완료**를 구분해야 합니다. `common-module`은 여러 서버가 함께 사용합니다.

`media-server`는 첫 추출 대상으로 파일 Controller·DTO·퍼사드를 소유하고, 자체 H2 DB(`jdbc:h2:mem:media`)와 JWT 검증을 사용합니다. Config Server 설정 수신, Eureka 등록, Gateway의 `/api/files/**` 라우팅이 검증되었습니다. 나머지 비즈니스 서버는 기본 설정에서 `jdbc:h2:mem:devhub`와 임의 포트(`server.port: 0`)를 사용하며, 현재 Gateway 라우트와 Config Client/Eureka 연동은 적용되지 않았습니다. 같은 H2 URL 문자열도 프로세스 간 데이터 공유를 뜻하지 않습니다.

## 지금까지 진행한 작업

1. 단일 애플리케이션의 도메인 소유권과 교차 의존성을 분석하고, `api`/도메인 구현 경계를 정리했습니다. `codex/`의 모듈화 문서에는 당시 판단과 검증 내역이 기록되어 있습니다.
2. 기존 `bootstrap`/도메인 구조를 Gradle 물리 모듈로 분리하고 테스트 소유권과 아키텍처 검증을 정리했습니다. **2026-09-16 당시 구조**에서 전체 빌드와 537건의 테스트 통과가 기록되었습니다. 이 수치는 현재 커밋의 테스트 결과로 간주하지 않습니다.
3. 모듈명을 현재의 `*-server` 체계로 변경하고 기존 `bootstrap-server`와 `web-server`를 제거했습니다. 이후 파일·인증·회원가입·알림·관리자 일부·커뮤니티·회원·프로젝트·홈·스킬 트렌드 HTTP 계층을 각 서버로 복구·이관했습니다.
4. 프로젝트·지원서·사용자 프로젝트 목록, 회원 프로필, 관리자 사용자·신고 API를 추가 이관하고 전체 `compileJava`를 통과시켰습니다.
4. Config Server, Eureka, Gateway를 추가했습니다. `media-server`의 빌드·기동 및 Gateway 경유 파일 요청을 확인했습니다. 익명 업로드 401, 유효한 개발용 ACCESS JWT 업로드 200, 공개 메타데이터 조회 200이 기록되어 있습니다.

위 이력은 [MSA 전환 진행 상황](codex/MSA_MIGRATION_PROGRESS.md)과 [물리 모듈화 완료 보고서](codex/PHYSICAL_MODULARIZATION_COMPLETION_REPORT.md)에 자세히 있습니다. [이전 IDE 실행 가이드](codex/MSA_NEXT_STEPS_AND_IDE_RUN_GUIDE.md)는 **모듈명 변경 전** 문서이므로 현재 실행 명령으로 그대로 사용하면 안 됩니다.

## 로컬 빌드와 실행

JDK 17과 Gradle Wrapper가 필요합니다. Windows PowerShell 기준:

```powershell
.\gradlew.bat projects
.\gradlew.bat test
.\gradlew.bat build
```

전체 서비스를 컨테이너로 실행하려면 먼저 실행 JAR을 만든 뒤 Compose를 사용합니다.

```powershell
.\gradlew.bat test bootJar verifyServiceBoundaries
Copy-Item .env.example .env
docker compose up --build
```

로컬 진입점은 Gateway `http://localhost:8080`, Eureka `http://localhost:8761`, Config Server `http://localhost:8888`입니다. `.env`의 내부 API 키와 JWT 키는 로컬 기본 예시값을 실제 운영 환경에 사용하면 안 됩니다.

파일 API 경로를 확인하려면 다음 순서로 각각 별도 터미널에서 실행합니다.

```powershell
.\gradlew.bat :config-server:bootRun
.\gradlew.bat :discovery-server:bootRun
.\gradlew.bat :media-server:bootRun
.\gradlew.bat :gateway-server:bootRun
```

기본 포트는 Config Server `8888`, Eureka `8761`, Gateway `8080`, Media `8086`입니다. 외부 파일 경로는 `http://localhost:8080/api/files`이고 Gateway가 `/api`를 제거해 Media의 `/files`로 전달합니다. Media의 로컬 파일 저장소 기본값은 `./build/devhub-media`입니다. 다른 비즈니스 서버는 현재 임의 포트로 실행되므로 로그에서 실제 포트를 확인해야 합니다.

로컬 `application.yml`의 OAuth/JWT 값은 개발용 자리표시자입니다. 실제 운영 비밀값은 환경 변수 또는 안전한 설정 경로로 주입해야 합니다.

## 다음 작업

1. **현재 기준선 재검증**: 서비스명 변경 후 전체 `test`/`build`, 각 `bootJar`, 핵심 서버 기동을 다시 확인합니다. 오래된 테스트 수와 커버리지 수치를 현재 결과로 교체합니다.
2. **HTTP API 복구 및 소유권 이전**: Git의 `d89a8cf~1`에 있던 `web` Controller와 HTTP DTO·퍼사드를 복구합니다. 홈·스킬 트렌드는 `query-server`, 사용자 프로필은 `member-server`, 프로젝트·지원서·사용자 프로젝트 목록 API는 `project-server`, 사용자 관리·비밀번호 초기화·신고 관리 API는 `admin-server`로 이전했습니다. 비밀번호 로그인 가능 여부도 `common-module` 계약으로 연결했습니다. 공개 URL, 인증 정책, Swagger 경로를 확인한 다음 Gateway에 라우트를 추가합니다. 현재 인증·회원가입·사용자 프로필·알림·파일·관리자 일부·커뮤니티·프로젝트·지원서 양식·홈·스킬 트렌드 Controller는 각 서버에서 컴파일 검증되었습니다.
3. **서비스 간 Java 의존성 제거**: `auth-server`, `project-server`, `community-server`, `query-server`의 다른 비즈니스 모듈 직접 호출을 명시적인 HTTP 계약으로 교체합니다. 동기 호출의 실패·타임아웃 처리와 서비스 인증도 함께 정의합니다.
4. **데이터 소유권 분리**: 서비스별 데이터 소유권을 확정하고 외부 Entity/Repository 접근과 교차 DB 조인을 제거합니다. 특히 통합 조회용 `query-server`의 데이터 공급 방식을 설계한 뒤 서비스별 DB를 분리합니다. 현재 H2 메모리 DB는 재시작 시 데이터가 사라지므로 운영 저장소와 마이그레이션 전략도 필요합니다.
5. **설정과 운영 검증**: 나머지 서비스에 Config Client·Eureka 등록을 적용하고 Gateway 경유 시나리오를 검증합니다. 비밀값, OAuth callback, JWT 검증 책임, CORS 및 관측성 구성을 서버별로 점검합니다.
6. **서비스별 CI/CD**: [GitHub Actions](.github/workflows/main.yml)는 전체 테스트·실행 JAR·서비스 경계를 검증한 뒤 11개 실행 서비스를 각각의 컨테이너 이미지로 병렬 빌드합니다. 루트 [Dockerfile](Dockerfile)은 `SERVICE` build argument로 대상 서비스 JAR을 선택합니다. 서비스별 Kubernetes 매니페스트 연결은 인프라 저장소에서 별도로 구성해야 합니다.

세부 계약과 이전 배경은 [도메인 소유권](codex/DOMAIN_OWNERSHIP.md), [아키텍처 규칙](codex/ARCHITECTURE_RULES.md), [모놀리스→MSA 분석](codex/MONOLITH_TO_MSA_ANALYSIS.md)을 참고하세요. 이 문서들은 작성 시점의 구조를 설명하므로 최종 구현 여부는 현재 코드와 `settings.gradle`을 기준으로 판단합니다.
