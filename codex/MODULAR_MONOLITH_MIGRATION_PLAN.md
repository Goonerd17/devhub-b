All architecture analysis, migration plans, decisions, reviews, and progress documents for this migration are stored under the `codex/` directory.

Read:

- `codex/MONOLITH_TO_MSA_ANALYSIS.md`

Then inspect the current repository again.

Create:

`codex/MODULAR_MONOLITH_MIGRATION_PLAN.md`

The long-term objective is to evolve the current Spring Boot monolith toward Microservices Architecture.

However, we are NOT creating Microservices yet.

The immediate objective is to transform the current application into a well-structured Gradle-based Modular Monolith while preserving existing behavior.

## Required migration sequence

Design an incremental migration covering:

1. Baseline protection
2. Gradle multi-module structure
3. Hexagonal Architecture dependency cleanup
4. Bounded Context module migration
5. Cross-context persistence dependency removal
6. Explicit Public Application Contracts
7. Workflow and transaction boundary cleanup
8. Read Model isolation
9. Event/outbox preparation
10. Modular Monolith architecture verification
11. Preparation for the first Microservice extraction

Do NOT propose moving the entire repository at once.

Break every phase into independently executable tasks.

Each task should ideally be small enough to become one Git commit.

## For every task specify

- Objective
- Current State
- Concrete classes/packages involved
- Files likely to change
- Exact Changes
- Must Not Change
- Dependency Impact
- Transaction Impact
- API Impact
- Database Impact
- Test Strategy
- Completion Criteria
- Risk
- Rollback Strategy
- Prerequisites

## Target architecture

Validate a possible structure such as:

```text
bootstrap
platform
web
identity
member
project
community
administration
media
notification
readmodel
```

Do not blindly accept these module names.

Derive the final structure from the repository.

In particular, re-evaluate but initially preserve the existing recommendation that Project Marketplace and Recruitment Application remain in the same module/deployment boundary.

Also evaluate whether Gradle modules should use:

- one module per Bounded Context with package-level API/implementation separation

or

- separate `*-api` and `*-impl` Gradle modules.

Choose one based on the current project's size and future extraction goals.

## Architectural principles

The target must eventually guarantee:

- Each Bounded Context has explicit ownership.
- A module cannot access another module's Repository directly.
- A module cannot import another module's JPA Entity.
- Cross-module communication uses explicit Public Application Contracts.
- HTTP DTOs remain in inbound/web boundaries.
- Infrastructure types do not leak into application ports.
- Module dependencies are acyclic.
- Logical data ownership is explicit.
- Existing REST behavior remains compatible.
- The application remains ONE deployable Spring Boot application.

## Restrictions

Do NOT:

- modify production code
- modify Gradle
- modify configuration
- modify tests
- modify database schemas
- introduce Kafka
- introduce Spring Cloud Gateway
- introduce Eureka
- introduce Kubernetes changes
- introduce Istio
- split the database
- create Microservices

This task is planning only.

The ONLY file you may create or modify is:

`codex/MODULAR_MONOLITH_MIGRATION_PLAN.md`

## Language

The final document MUST be written in Korean.

Preserve technical identifiers exactly as they appear in source code.

The final plan must be detailed enough that another Codex session can execute ONE task at a time without redesigning the architecture.