# AGENTS.md — Superheroes CRUD Template

## Architecture Rules (hard constraints)

1. **core/** — Zero framework imports. No Spring, JPA, Mongo, Jackson. Pure Java only.
2. **application/** — Zero framework imports. Depends only on core ports. No `@Service`.
3. **infrastructure/** — All framework coupling lives here. Controllers, adapters, config, security.
4. **Dependency direction:** infrastructure → application → core. File imports must reflect this.

## Conventions

**Module patterns:**
- `superheroes/application/service/` — consolidated `SuperheroService` interface + `SuperheroServiceImpl`
- `villains/application/usecase/` — separate `CreateVillainUseCase`, `GetVillainUseCase`, etc.

**Repository adapters:** Implement port from core. `@Profile("inmemory"|"sql"|"mongo")`. Map domain ↔ persistence object inside the adapter only.

**Controllers:**
- Class: `@WebAdapter` (our annotation, meta-annotates `@RestController`)
- Methods: `@Get`, `@Post`, `@Put`, `@Delete` (our annotations)
- Parameters: `@PathVariable`, `@RequestBody` (Spring — cannot meta-annotate)
- Response: `return HttpResponse.created(body, location).toResponseEntity()` (explicit conversion)
- Error handling: none in controllers — `GlobalExceptionHandler` (shared) catches domain exceptions

**Spring wiring:** Each domain has an infrastructure config class (`SuperheroConfig`, `VillainConfig`) with `@Bean` methods. Application layer classes are plain Java — never annotated with `@Service` or `@Component`.

## Cross-Cutting

| Concern | Location |
|---|---|
| Tracing | `shared/logging/TraceFilter` — MDC: `traceId`, `userId`, `method`, `path`, `statusCode` |
| Auth | `shared/security/SecurityConfig` — dual JWT + session. `DemoUserDetailsService` |
| Error handling | `shared/web/GlobalExceptionHandler` |
| Validation errors | `shared/web/ValidationExceptionHandler` |

## Adding a Domain Module

1. Create `newmodule/core/` — model, port interface, exceptions (zero framework imports).
2. Create `newmodule/application/` — DTOs (records), services or use cases (zero framework imports).
3. Create `newmodule/infrastructure/` — controller, persistence adapters, `NewModuleConfig.java` (`@Bean` wiring).
4. Add endpoint rules to `shared/security/SecurityConfig.java`.

Spring Boot's default component scan covers all sub-packages. No explicit scan needed.

## Running

```bash
./mvnw test -Dtest="!*Sql*RepositoryAdapter*"   # skip Testcontainers
./mvnw spring-boot:run                           # default: inmemory
```
