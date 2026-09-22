# Kafka 이벤트 카탈로그

작성일: 2026-09-22

## 토픽

| 토픽 | 발행 주체 | 주요 소비자 | 목적 |
|---|---|---|---|
| `devhub.project.events` | Project | Query, Notification | Project와 지원서의 상태 변경 전파 |
| `devhub.project.events.DLT` | Query/Notification consumer error handler | 운영 재처리 도구 | 반복 실패 이벤트 보관 |

## 이벤트 목록

| 이벤트 | 발행 시점 | Query | Notification |
|---|---|---|---|
| `project.created` | Project 생성 트랜잭션 완료 | Project projection 생성 | 무시 |
| `project.updated` | Project 수정 트랜잭션 완료 | Project projection 갱신 | 무시 |
| `project.closed` | Project 마감 트랜잭션 완료 | Project lifecycle 갱신 | 소유자 마감 알림 |
| `project.application.submitted` | 지원서와 답변 저장 완료 | 지원서 projection 생성 | 무시 |
| `project.application.status-changed` | 승인/반려 상태 저장 완료 | 지원서 상태 갱신 | 지원자 승인/반려 알림 |
| `project.application.cancelled` | 지원서 취소 완료 | 지원서 상태를 `CANCELLED`로 변경 | 무시 |

## 공통 envelope

모든 이벤트는 다음 필드를 포함합니다.

```json
{
  "eventId": "uuid",
  "eventType": "project.created",
  "schemaVersion": 1,
  "occurredAt": "2026-09-22T00:00:00Z",
  "aggregateType": "project",
  "aggregateId": "PROJECT_GUID",
  "correlationId": "request-correlation-id",
  "causationId": null,
  "payload": {}
}
```

Kafka header에도 `eventId`, `eventType`, `correlationId`, `causationId`를 함께 전달합니다.

## 처리 보장

- 발행: Project DB Transactional Outbox
- 소비: consumer group별 독립 projection
- 중복 방지: `lastEventId` 또는 inbox 테이블
- 실패 처리: 1초 간격 3회 재시도 후 `.DLT` 전송
- 보존: Outbox 발행 완료 이벤트 7일, DLT 14일
- 호환성: 지원하지 않는 `schemaVersion`은 DLT 대상으로 처리

## 운영 전환 전 남은 작업

1. Docker Compose에서 Kafka와 모든 consumer의 실제 기동 검증
2. DLT 조회·재처리 명령 또는 운영 도구 추가
3. 이벤트 payload에 민감정보가 포함되지 않는지 자동 검사
4. Testcontainers 기반 producer-consumer 계약 테스트 추가
5. schemaVersion 2 도입 시 호환 기간과 소비자 업그레이드 순서 정의
