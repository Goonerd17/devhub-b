# DevHub 모듈러 모놀리스 아키텍처 규칙

## 1. 컨텍스트 경계

최종 Gradle 모듈은 `identity`, `member`, `project`, `community`, `administration`, `media`, `notification`, `readmodel`, `platform`, `web`, `bootstrap`이다. 한 business context 안에는 `api`, `core`, `outbound` package를 둔다.

- 허용: `contextA → contextB.api`
- 금지: `contextA → contextB.core`, `contextA → contextB.outbound`
- 금지: foreign Entity, Repository, QueryDSL DAO, mapper, adapter import

예: Community는 `member.api.MemberPublicProfileQuery`를 사용할 수 있지만 Member `UserRepository`, `User`, `UserEntity`를 사용할 수 없다.

## 2. Hexagonal 규칙

- `core`의 domain/application/port는 HTTP DTO, controller, Spring MVC type, JPA Entity, Spring Data repository, QueryDSL implementation, adapter implementation을 import하지 않는다.
- application service는 자신의 outbound port에만 의존한다.
- `outbound` adapter만 자신의 port를 구현하고 JPA/QueryDSL/external infrastructure를 사용한다.
- HTTP request/response, response wrapper, validation, resolver, exception translation은 `web`에 둔다.
- `api`는 의미 있는 cross-context command/query/result만 둔다. HTTP DTO와 persistence type은 둘 수 없다.

## 3. Persistence 소유권

- Entity, repository, QueryDSL DAO, persistence adapter의 owner는 하나의 context다.
- 다른 context는 같은 H2 database를 물리적으로 공유하더라도 owner persistence를 접근할 수 없다.
- Readmodel만 `outbound.home`/`outbound.skilltrend` 안에서 문서화된 foreign table/entity의 **읽기 전용** 조인을 할 수 있다. Readmodel은 foreign state를 변경할 수 없고 write context는 Readmodel에 의존할 수 없다.

## 4. Platform, Web, Bootstrap

- Platform은 ID/time/관측성처럼 business-neutral capability만 소유하고 business context에 의존하지 않는다.
- Web은 public application contract만 호출하며 persistence/outbound를 직접 호출하지 않는다.
- Bootstrap은 하나의 Spring Boot process를 조립할 수 있지만 business rule을 소유하지 않는다.

## 5. Transaction 및 workflow

- aggregate state 변경 transaction은 state owner context가 소유한다.
- 여러 context의 정보를 조합할 때는 owner의 `.api` query/command를 사용한다.
- 즉시 일관성이 필요한 workflow owner는 `DOMAIN_OWNERSHIP.md`에 명시한다. owner가 확정되지 않은 workflow는 module physical migration 전에 결정한다.
- Kafka, HTTP, Saga, distributed transaction은 이 규칙의 해결책이 아니다.

## 6. 자동 검증 목표

물리 모듈화 후 ArchUnit은 다음을 검증한다.

1. `..core..`는 `..web..`/HTTP DTO에 의존하지 않는다.
2. 모든 context는 foreign `..core..`/`..outbound..`에 의존하지 않는다.
3. `web`은 `..outbound..`에 의존하지 않는다.
4. `platform`은 business context package에 의존하지 않는다.
5. Readmodel foreign persistence exception은 `readmodel.outbound.home`과 `readmodel.outbound.skilltrend`로 한정한다.

현재 Root source set은 이 규칙을 Gradle로 강제할 수 없으므로, source cleanup과 단일-context module migration이 끝난 뒤 ArchUnit을 도입한다. 현재 위반을 무시하도록 예외를 넓히는 테스트는 만들지 않는다.
\n+## 적용 수준 안내 (2026-09-16)

본 문서는 현재 마이그레이션에서 설계 방향을 참고하기 위한 문서다. 기존 코드와 회귀 테스트가 확인하는 외부 동작을 우선하며, 규칙 자체가 안전한 구현을 차단하는 경우에는 코드·테스트를 근거로 가장 작은 변경을 선택한다. 규칙 위반 여부만으로 작업을 중단하지 않는다.
