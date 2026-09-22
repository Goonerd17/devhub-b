# MSA 마이그레이션 현황 및 후속 계획

작성일: 2026-09-22

## 한 줄 결론

현재 마이그레이션은 **코드와 빌드 기준의 물리적 서비스 분리(Phase 3)를 완료하고, 실제 분산 런타임 검증(Phase 4)으로 넘어가는 단계**입니다. 서비스 경계와 서비스 간 HTTP 호출은 구현되었지만, 운영용 데이터베이스·배포·관측성·장애 대응까지 끝난 상태는 아니므로 아직 “운영 준비 완료”로 보기는 어렵습니다.

## 현재 단계

| 영역 | 상태 | 근거 및 설명 |
|---|---|---|
| 서비스 분리 | 완료 | `auth`, `member`, `project`, `community`, `admin`, `media`, `notification`, `query`와 `config`, `discovery`, `gateway`로 구성된 12개 Gradle 모듈이 존재합니다. |
| 비즈니스 모듈 간 빌드 결합 제거 | 완료 | 운영 코드의 서비스 간 Gradle 의존성은 제거하고 `shared-kernel`의 계약·기술 타입만 공유하도록 정리했습니다. 루트 `verifyServiceBoundaries`로 금지 의존성을 검사합니다. |
| 서비스 간 통신 | 대부분 완료 | 주요 호출을 내부 HTTP/RestClient로 전환했고 Eureka 서비스 ID와 LoadBalancer를 사용합니다. Query의 홈/분석 조회, Project·Community의 Member/Auth/Media 연동이 포함됩니다. |
| 이벤트 기반 전환 기반 | Kafka 1차 연결 | Project 생성·수정·마감 시 동일 트랜잭션으로 outbox 이벤트를 저장하고 Kafka publisher가 전달하도록 구성했습니다. Query projection과 Project 마감 알림 consumer까지 연결했습니다. 실제 Kafka Compose 기동 검증은 남아 있습니다. |
| 내부 API 인증 | 개발 단계 완료 | `X-Internal-Api-Key`와 `INTERNAL_API_KEY`를 도입했습니다. 키가 없으면 401, 올바른 키면 내부 API가 호출되는 실제 Member HTTP 스모크 검증을 완료했습니다. |
| 서비스 독립 기동 | 완료 | Identity(Auth), Community, Project의 이전 기동 오류를 수정했고 Query/Admin/Notification/Media도 독립 기동을 확인했습니다. 로그에 각 `*Application`의 `Started`가 기록되었습니다. |
| CI/빌드 산출물 | 완료 | `test bootJar verifyServiceBoundaries`가 통과했으며, 서비스별 Docker 이미지 매트릭스와 Compose 설정을 추가했습니다. |
| Compose/배포 실행 검증 | 부분 완료 | Compose 정적 설정과 CI 검증 단계는 추가했지만, 현재 개발 환경에는 Docker CLI가 없어 전체 Compose 기동 및 네트워크 호출은 아직 실행하지 못했습니다. |
| 데이터 소유권 | 부분 완료 | 서비스별 코드 경계는 분리했지만 현재 기본 저장소는 H2 메모리 DB 중심입니다. 운영 DB·스키마 마이그레이션·백업 전략은 후속 작업입니다. |
| 운영 보안/관측성/복원력 | 미완료 | JWT 정책 통일, 비밀키 관리·교체, timeout/retry/circuit breaker, 분산 추적·메트릭·알람은 운영 기준으로 보강해야 합니다. |

따라서 현재 위치는 다음과 같습니다.

```text
Phase 1 분석/경계 정의       완료
Phase 2 논리적 모듈화        완료
Phase 3 물리적 서비스 분리   완료
Phase 4 분산 런타임 검증     진행 중  ← 현재
Phase 5 운영 인프라/데이터   예정
Phase 6 운영 전환/안정화     예정
```

## 이번 단계에서 해결한 주요 오류

- Identity/Auth: `app.frontend.base-url` 누락으로 인한 기동 실패를 기본값이 포함된 설정으로 수정했습니다.
- Community: `UserEntity`를 직접 참조하는 JPQL을 제거하고 Member의 moderation profile/status 내부 API와 HTTP 클라이언트로 전환했습니다.
- Project: `JpaAdminProjectApplicationRepository`의 `UserEntity` 조인을 제거해 서비스 경계를 침범하지 않도록 수정했습니다.
- Query/Notification/Media: Bean Validation provider가 없다는 기동 경고를 `spring-boot-starter-validation` 추가로 해결했습니다.
- Query: Home/Analytics 조회에서 다른 서비스의 Entity·Persistence를 직접 참조하지 않고 내부 API 계약을 사용하도록 변경했습니다.

## 검증 결과

- 전체 Gradle 검증: `test bootJar verifyServiceBoundaries` 통과
- 실행 태스크: 72개 actionable tasks 성공
- `git diff --check`: 오류 없음
- 서비스 목록·Compose 포트·CI 이미지 매트릭스의 정적 목록 대조 완료
- Project 이벤트 outbox/Kafka publisher 수직 슬라이스 추가: `project.created`, `project.updated`, `project.closed`
- Query Project projection Kafka consumer 추가 및 중복 이벤트 방지 키(`lastEventId`) 적용
- Notification Project 마감 이벤트 consumer 추가 및 처리 이벤트 inbox로 중복 알림 방지
- Project 지원서 제출 이벤트(`project.application.submitted`)를 동일 outbox/Kafka 경로에 추가
- Project 지원서 승인·반려 이벤트(`project.application.status-changed`) 추가
- Notification이 지원서 승인·반려 이벤트를 소비해 지원자 알림 생성
- 지원자 취소 이벤트(`project.application.cancelled`) 추가
- Query 지원서 projection consumer와 별도 consumer group 추가
- Kafka 이벤트 계약 목록을 Project/지원서 lifecycle 기준으로 확장
- Query/Notification Kafka consumer에 1초 간격 3회 재시도 및 원본 topic의 `.DLT` 발행 정책 추가
- Query/Notification DLT publisher에 String serializer와 `acks=all` 명시
- Kafka publisher가 `eventId`, `eventType`, `correlationId`, `causationId` 헤더를 전달하도록 추가
- Project HTTP 요청의 `X-Correlation-Id`를 outbox와 Kafka 이벤트까지 전파
- Query/Notification consumer에 `schemaVersion=1` 계약 검증 추가
- Project outbox 발행 완료 이벤트 7일 보존 및 주기적 정리 정책 추가
- Project DLT topic 보존 기간 14일 설정 추가
- Docker CLI 부재로 실제 `docker compose up`은 미실행

## 앞으로 진행할 단계

### Phase 4 — 분산 런타임 검증

1. Docker가 가능한 환경에서 `docker compose config --quiet`와 전체 `docker compose up`을 실행합니다.
2. Config Server → Eureka → Gateway → 각 서비스의 등록·헬스체크·라우팅을 검증합니다.
3. Gateway를 통한 로그인/회원/프로젝트/커뮤니티/관리자/Query 주요 시나리오를 시드 데이터로 스모크 테스트합니다.
4. 내부 API 키 누락·오류·서비스 다운·응답 지연 상황에서 401/5xx/timeout 응답과 로그를 확인합니다.
5. HTTP DTO와 상태 코드가 실제 소비자 기대와 일치하는지 계약 테스트를 추가합니다.
6. Project outbox 이벤트를 Query/Notification consumer에 연결하고, Kafka consumer의 멱등성·재처리를 검증합니다.

완료 기준: Compose 전체 기동, Eureka 등록, 핵심 사용자 시나리오 통과, 내부 호출 실패 시 원인 추적 가능한 로그 확보.

### Phase 5 — 운영 데이터와 플랫폼 준비

1. 서비스별 운영 DB와 스키마를 확정하고 Flyway 또는 Liquibase 마이그레이션을 추가합니다.
2. H2 전용 설정을 제거하고 환경별 DB 접속·풀·트랜잭션 정책을 분리합니다.
3. Media 파일을 영속 오브젝트 스토리지로 이전하고 백업/보존 정책을 정합니다.
4. Kubernetes/Helm 또는 현행 배포 플랫폼에 서비스별 이미지, Secret, Config, readiness/liveness probe를 추가합니다.
5. timeout, retry, circuit breaker, 멱등성, 오류 코드 변환을 서비스 간 호출에 적용합니다.

완료 기준: 한 서비스 재배포가 다른 서비스 데이터와 배포에 영향을 주지 않고, 데이터 복구와 롤백 절차를 재현할 수 있음.

### Phase 6 — 보안·관측성·운영 전환

1. JWT resource-server 정책과 공개/보호 endpoint 목록을 모든 서비스에서 통일합니다.
2. 내부 API 키를 Secret Manager로 관리하고 교체 절차를 마련합니다.
3. trace ID 전파, 구조화 로그, 메트릭, 대시보드, 알람, 서비스별 SLO를 추가합니다.
4. OpenAPI/consumer-driven contract test와 Testcontainers 기반 통합 테스트를 CI에 포함합니다.
5. 이미지 취약점·SBOM·의존성 스캔과 배포 승인 단계를 CI에 추가합니다.
6. 기존 마이그레이션 문서의 서비스 상태 표를 실제 상태와 맞추고, 임시 검증 산출물을 정리합니다.

완료 기준: 장애 탐지·원인 파악·롤백이 가능하고, 보안/품질 게이트를 통과한 서비스별 독립 배포가 가능함.

## 현재 판단 및 주의사항

- “빌드 성공”은 서비스 경계와 컴파일 가능성을 증명하지만, 네트워크·인증·데이터 일관성까지 증명하지는 않습니다.
- 현재 가장 큰 다음 검증 포인트는 Docker Compose 전체 기동과 Gateway/Eureka를 포함한 종단 간 호출입니다.
- 운영 전환 전까지는 H2 메모리 DB와 개발용 내부 키를 운영 환경에 사용하면 안 됩니다.
- `shared-kernel`은 계약·공통 보안/기술 타입만 보유해야 하며, 특정 도메인의 Entity나 Repository가 들어가지 않도록 계속 검사해야 합니다.

## 관련 문서

- [마이그레이션 진행 로그](MSA_MIGRATION_PROGRESS.md)
- [도메인 소유권](DOMAIN_OWNERSHIP.md)
- [아키텍처 규칙](ARCHITECTURE_RULES.md)
- [물리적 모듈화 완료 보고서](PHYSICAL_MODULARIZATION_COMPLETION_REPORT.md)
- [Compose 실행 정의](../compose.yml)
- [CI 워크플로](../.github/workflows/main.yml)
