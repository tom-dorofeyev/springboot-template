# Superheroes CRUD — Spring Boot Clean Architecture Template

A Java Spring Boot 3.5 template demonstrating Clean Architecture with swappable persistence backends, dual authentication, and structured tracing.

## Quick Start

```bash
./mvnw spring-boot:run    # in-memory, no database needed
```

### Docker variants

```bash
docker compose -f docker-compose-inmemory.yml up
docker compose -f docker-compose-sql.yml up
docker compose -f docker-compose-mongo.yml up
```

## API

Public:

```
POST /api/v1/auth/login   {"username":"admin","password":"admin"}
POST /api/v1/auth/logout
```

Authenticated (USER for GET, ADMIN for write):

```
GET    /api/v1/superheroes         GET    /api/v1/villains
GET    /api/v1/superheroes/{id}    GET    /api/v1/villains/{id}
POST   /api/v1/superheroes         POST   /api/v1/villains
PUT    /api/v1/superheroes/{id}    PUT    /api/v1/villains/{id}
DELETE /api/v1/superheroes/{id}    DELETE /api/v1/villains/{id}
```

Demo users: `admin`/`admin` (ADMIN), `user`/`user` (USER).
Browser clients receive httpOnly session cookies. API clients use `Authorization: Bearer <jwt>` from the login response.

## Architecture

```
com/example/superheroes/
├── shared/          ← cross-cutting: auth, logging, tracing, security
├── superheroes/     ← domain module (Service pattern)
│   ├── core/        ←   domain model, repository port (zero framework imports)
│   ├── application/ ←   use cases, DTOs (zero framework imports)
│   └── infrastructure/ ← controllers, persistence adapters, config
└── villains/        ← domain module (UseCase pattern)
    ├── core/
    ├── application/
    └── infrastructure/
```

**Dependency rule:** infrastructure → application → core. No framework imports in core or application.

**Repository swappability:** Each adapter (`@Profile("inmemory"|"sql"|"mongo")`) implements the same port in core. Switching databases changes `spring.profiles.active` — zero code changes.

**Two patterns side by side:**

| | Superheroes | Villains |
|---|---|---|
| Pattern | `SuperheroService` (consolidated) | `CreateVillainUseCase`, `GetVillainUseCase`, etc. (one per operation) |
| When | Simple CRUD | Operations that benefit from independent evolution |

**Tracing:** `X-Trace-Id` header propagated. Structured JSON logs with `traceId`, `userId`.

## Tests

```bash
./mvnw test -Dtest="!*Sql*RepositoryAdapter*"   # skip Testcontainers (needs Docker)
```
