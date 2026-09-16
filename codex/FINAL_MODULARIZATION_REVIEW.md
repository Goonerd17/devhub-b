# 최종 물리 모듈화 검토 (2026-09-16)

## 결과

상태: **PARTIAL**

생산 코드 이동은 완료되어 루트 `src/main/java`의 Java 파일은 0개이다. `bootstrap`이 단일 Spring Boot 조립 모듈이며 `bootJar` 생성도 성공했다. 테스트 209개는 `bootstrap/src/test/java`로 이동했다. 전체 테스트는 526개 실행, 71개 실패로 환경 설정 및 기존 기대값 문제를 포함한다.

## 모듈별 생산 소스

| 모듈 | Java 수 | 책임 |
|---|---:|---|
| `bootstrap` | 7 | `DevhubApplication`, Spring 조립 |
| `platform` | 21 | 기술 중립 인프라 |
| `web` | 146 | Controller, HTTP DTO, resolver, 보안 HTTP adapter |
| `identity` | 112 | 인증, JWT, OAuth, credential, verification |
| `member` | 60 | 회원 및 프로필 |
| `project` | 83 | Project Marketplace + Recruitment Application |
| `community` | 47 | Board, Comment, Report |
| `administration` | 64 | Admin, Terms, Application Form, Banner |
| `media` | 16 | 파일 메타데이터/저장 |
| `notification` | 20 | 알림/메일 |
| `readmodel` | 38 | Home, SkillTrend 조회 모델 |

기존 `*-api`/`*-impl` 프로젝트는 제거되었고, 모든 모듈은 하나의 H2 `jdbc:h2:mem:devhub`를 공유한다.

## 검증 결과

- 전체 생산 컴파일 및 `build -x test`: 성공
- QueryDSL annotation processing: 성공
- `:bootstrap:bootJar`: 성공
- `bootRun` (local profile): `jdbc:h2:mem:devhub`로 H2 Console, JPA EntityManagerFactory, 28개 Repository, Security, Controller/Tomcat 초기화 및 정상 기동을 확인했다.
- 전체 테스트: 526개 중 455개 성공, 71개 실패. 주된 원인은 `${oauth.github.client-id}`/`jwt.secret.key` 미설정이며 `AuditorAwareProviderTest` 1건은 기존 principal 기대 불일치다.

## 남은 부채

Identity의 회원 등록 흐름은 `member.api.MemberRegistrationUseCase` 계약으로 전환되었다. 다만 Project의 관리자 지원자 조회는 여전히 User/Admin persistence 타입을 직접 참조하고, `readmodel`은 승인된 READ-ONLY 외부 테이블 조회 예외를 가진다. 단일 모듈 내부 `core`/`outbound` 경계의 ArchUnit 자동 검증도 아직 없다.

따라서 최종 판정은 **PARTIAL**이다.
