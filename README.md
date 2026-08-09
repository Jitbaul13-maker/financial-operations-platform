# Financial Operations Platform

A backend-focused financial operations platform designed to model reliable transaction processing and gradually evolve toward a production-grade distributed financial system.

The project is being developed incrementally, with an emphasis on correctness, explicit state management, testing, observability, and production-oriented engineering practices.

> Status: 🚧 Under active development — Stage 4 completed.

## Tech Stack

### Currently Implemented

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Redis
* Maven
* Docker / Docker Compose
* JUnit

Additional infrastructure and distributed-system components will be introduced as the project progresses.

## Current Architecture

The application currently follows a modular monolith architecture.

The transaction core now provides:

```
Transaction Request
       │
       ▼
Idempotency Check
       │
       ├── Duplicate → Return existing transaction
       │
       ▼
Transaction Processing
       │
       ▼
Optimistic Locking
       │
       ├── Version Conflict → Reject update
       │
       ▼
State + Audit Update
```

These mechanisms provide the foundation for safely handling retries and concurrent requests as the platform evolves toward asynchronous and distributed processing.

## Implemented Features

### Stage 0 — Project Bootstrap ✅

* Spring Boot application setup
* PostgreSQL integration
* Docker-based local development environment
* Environment-based configuration
* Database migration foundation
* Health/readiness verification

### Stage 1 — Transaction Ledger ✅

Implemented the initial transaction domain and REST API.

Current capabilities include:

* Create transactions
* Retrieve transactions
* Paginated transaction listing
* Persistent transaction storage
* Transaction status tracking
* Database-backed transaction ledger

### Stage 2 — Transaction State Machine ✅

Introduced explicit lifecycle management for transactions.

Current lifecycle:

```text
INITIATED
    ↓
PROCESSING
   ↙     ↘
COMPLETED FAILED
    ↓
REVERSED
```

### Stage 3 — Immutable Audit Trail ✅

Every transaction state change is recorded in an append-only audit log.

Current capabilities include:

- Immutable audit records
- Previous and new transaction status tracking
- Actor and reason recording
- Timestamped audit events
- Atomic transaction + audit persistence
- Transaction audit history endpoint

Valid transitions are explicitly controlled by the domain rather than allowing arbitrary status changes.

Invalid transitions are rejected, protecting transaction lifecycle integrity.

State transition behavior is covered by automated tests.

### Stage 4 — Idempotent Ingestion & Concurrency Control ✅

The transaction ingestion flow now protects against duplicate requests and concurrent modifications.

Current capabilities include:

* Idempotent transaction creation using an idempotency key
* Duplicate request detection
* Database-level uniqueness enforcement for idempotency keys
* Concurrent transaction update protection
* Optimistic locking using a version field
* Conflict detection for stale updates
* Tests covering duplicate requests and concurrent update scenarios

## Transaction States

| State        | Description                          |
| ------------ | ------------------------------------ |
| `INITIATED`  | Transaction has been created         |
| `PROCESSING` | Transaction is being processed       |
| `COMPLETED`  | Transaction completed successfully   |
| `FAILED`     | Transaction processing failed        |
| `REVERSED`   | A completed transaction was reversed |

## Audit Guarantees

The platform maintains an append-only audit trail for every transaction state change.

Every audit record captures:

- Previous status
- New status
- Actor
- Reason
- Timestamp

Transaction state updates and audit persistence execute within the same database transaction, ensuring they either both succeed or both roll back.

## Development Roadmap

The platform is being built incrementally.

```text
Stage 0  ✅ Project Bootstrap
Stage 1  ✅ Transaction Ledger
Stage 2  ✅ Transaction State Machine
Stage 3  ✅ Immutable Audit Trail
Stage 4 ✅ Idempotent Ingestion & Concurrency
Stage 5 ⏳ Redis Caching Stage
6+ ⏳ Planned
```

Future stages will introduce concepts including:

* Kafka and asynchronous workflows
* Reconciliation
* Authentication and authorization
* Observability and metrics
* CI/CD
* Cloud deployment
* Container orchestration
* Resilience and production hardening

These components will be documented as they are implemented.

## Running Locally

### Prerequisites

Ensure the following are installed:

* Java 21
* Docker
* Docker Compose
* Git

### Clone the Repository

```bash
git clone https://github.com/Jitbaul13-maker/financial-operations-platform
cd financial-operations-platform
```

### Configure Environment

Create the required local environment configuration without committing secrets to the repository.

### Start Infrastructure

```bash
docker compose up -d
```

### Run the Application

```bash
./mvnw spring-boot:run
```

## Engineering Goals

This project is intended to explore the engineering challenges involved in building financial backend systems, including:

* Transaction correctness
* Explicit lifecycle management
* Idempotency
* Concurrent updates
* Event-driven processing
* Failure recovery
* Reconciliation
* Security
* Observability
* Resilience
* Deployment and operational concerns

The architecture will evolve as these requirements are introduced.

## Project Status

**Current milestone: Stage 4 completed.**

The next milestone focuses on introducing **Redis caching** into the transaction platform.

---

This README will evolve alongside the platform as new architectural components and production capabilities are implemented.
