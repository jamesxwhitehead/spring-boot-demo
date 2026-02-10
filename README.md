# Spring Boot Demo (Kotlin) — Portfolio Application (DDD-lite & Pragmatic Spring)

A compact, production-style application that demonstrates **Spring Boot + Kotlin + Spring Data JPA** with an emphasis on:

- **DDD-lite modeling**: domain invariants exist and are enforced, explicit lifecycle rules.
- **Clean transactional boundaries**: use-case driven.
- **Hexagonal-ish structure (pragmatic)**: domain and use-cases are distinguishable from delivery (HTTP) and persistence concerns.
- **REST API best practices**: HTTP semantics, validation, consistent response structure.
- **Test methodology**: high-signal tests that create confidence without slowing teams down, reusable fixtures.
- **Operational sensibility**: transactional consistency, controlled error behavior, test strategy aligned to risk.

This repository is intentionally small but **opinionated—optimized** for technical reviewers (recruiters, senior/principal/staff engineers) who want to quickly assess engineering judgment rather than “feature volume.”

---

## Domain: Posts & Tags with a lifecycle

The app models a simple blog post publishing workflow:
- **Post** has a lifecycle (e.g., draft → published → archived) with **explicit transition rules**
- **Tags** can be associated/disassociated with posts (many-to-many)

---

## Framing: DDD/Hexagonal *and* Pragmatic Spring

This repo intentionally balances two realities:

### DDD / Hexagonal principles (kept lightweight)
- **Domain model** expresses the core rules (e.g., lifecycle/state transitions)
- **Use-cases** sit behind an application/service boundary
- **Adapters** exist at the edges:
    - HTTP API as an inbound adapter
    - JPA repositories as outbound adapters

### Pragmatic Spring Boot delivery
- Spring MVC controllers remain **thin**
- Spring Data JPA repositories are used directly (no ceremony for its own sake)
- Transactions are applied at the service/use-case layer (where the unit of work is clear)

---

## Architecture choices that scale

- **Layered structure with clear responsibilities**
    - **Controllers**: thin HTTP boundary (routing + status codes)
    - **Domain model**: lifecycle rules and invariants
    - **Service layer**: application use-cases + transactional boundaries
    - **Repositories**: persistence access via Spring Data JPA
- **Failure modes are explicit**
    - Invalid lifecycle operations return a meaningful API error
    - Not-found behavior maps cleanly to HTTP semantics
- **Deterministic demo data**
    - Seeded sample dataset for repeatable local review

### Kotlin & JPA done pragmatically
- Kotlin modeling that keeps **mutability controlled**
- JPA mappings that are easy to reason about for typical backend work
- Minimal “framework noise” in the core domain logic

The result is a codebase that feels natural to Spring teams, while still maintaining boundary discipline.

---

## Kotlin-specific best practices
- Kotlin-first Spring Boot setup
- Idiomatic Kotlin modeling (properties, visibility, encapsulation of mutability)
- Minimal “framework leakage” into domain logic where practical

---

## API surface (high level)

Typical interactions:
- Create and list posts
- Retrieve a post by ID
- Publish/archive posts (state transitions)
- Add/remove tags

Endpoints are intentionally straightforward and designed to make it easy to reason about:
- `GET /posts`
- `POST /posts`
- `GET /posts/{id}`
- `PATCH /posts/{id}/publish`
- `PATCH /posts/{id}/archive`
- `PUT /posts/{id}/tags/{name}`
- `DELETE /posts/{id}/tags/{name}`

### API design callouts
- **Creation uses proper REST semantics**: `POST /posts` returns `201 Created` with a `Location` header.
- **Validation is enforced at the boundary**: invalid request bodies and invalid field values return `400 Bad Request` with a structured Problem response.
- **Lifecycle violations return `422 Unprocessable Content`** (business rule failure, not a transport failure).

---

## Tagging: service boundary + event-driven cleanup

Tag operations are intentionally routed through a dedicated use-case service to keep controllers thin and to centralize rules and persistence interactions.

When a tag is removed from a post, the system emits a domain event and performs **orphan cleanup** (removing tags with no remaining associations). This demonstrates:
- decoupling write orchestration from secondary concerns,
- clean separation of “command” behavior and cleanup logic,
- transaction-aware event handling.

---

## OpenAPI / API discovery

The project includes **SpringDoc OpenAPI** support, making it easy for reviewers to discover and exercise the API via generated documentation/UI once the app is running.

(Typically `http://localhost:8080/swagger-ui/index.html`)

---

## Tech stack

- **Java 21**
- **Kotlin (2.2 API level)**
- **Spring Boot** (Spring MVC / WebMVC)
- **Spring Data JPA / Hibernate**
- **Jakarta Persistence** (`jakarta.*`)
- **H2** (runtime DB for local execution)
- **Gradle (Kotlin DSL)**
- **SpringDoc OpenAPI** (API docs)
- **Test Fixtures** (Gradle `java-test-fixtures`) for reusable test data builders

---

## Project structure (for quick navigation)

```plain text
src/main/kotlin/com/example/demo
  config/        -> application wiring (e.g., deterministic Faker bean)
  controller/    -> REST endpoints + shared controller utilities
  controller/advice/ -> exception mapping to Problem Details responses
  entity/        -> JPA entities + lifecycle rules
  service/       -> transactional use-cases (publish/archive, tagging)
  repository/    -> Spring Data JPA repositories
  event/         -> domain events
  listener/      -> event listeners (e.g., cleanup)

src/test/kotlin/com/example/demo
  controller/    -> web-layer tests
  repository/    -> persistence tests
  service/       -> use-case tests
  DemoApplicationTest -> context smoke test

src/testFixtures/kotlin
  ```            -> reusable fixtures/builders for tests
```

---

## Testing strategy (what to look for)

This project aims to demonstrate *how* tests are chosen:

- **Controller tests**
    - Validate HTTP contracts, status codes, and Problem Details responses
- **Service tests**
    - Validate use-case behavior and transactional outcomes
- **Repository tests**
    - Validate persistence behaviors (including custom operations)
- **Test fixtures**
    - Reduce duplication and improve readability across test suites

The overall intent: **high signal, low brittleness**—tests focus on boundaries and business behavior rather than internal implementation details.

---

## Running locally

### Prerequisites
- JDK **21**
- No external services required (runs as a typical Spring Boot app)

### Build
```shell script
./gradlew build
```

### Run
```shell script
./gradlew bootRun
```

### Test
```shell script
./gradlew test
```

After startup, the app auto-populates a small set of deterministic sample data so reviewers can immediately exercise the API.

---

## License

See `LICENSE`.

---
