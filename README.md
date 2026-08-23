# Financial Operations Platform

A backend-focused financial operations platform designed to model reliable transaction processing and gradually evolve toward a production-grade distributed financial system.

The project is being developed incrementally, with an emphasis on correctness, explicit state management, testing, observability, and production-oriented engineering practices.

> **Status:** 🚧 Under active development — Stage 8 completed.

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

##  Current Architecture

The application continues to follow a **modular monolith architecture**.

The transaction flow now consists of an internal ledger, configurable business rules, an independent provider ledger, and an audit trail.

```text

                         Transaction Request
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   Idempotency   │
                         │   Check         │
                         └────────┬────────┘
                                  │
                         Business Rule Engine
                                  │
                         ┌────────┴────────┐
                         │                 │
                      Rejected          Accepted
                         │                 │
                         ▼                 ▼
                      Reject     Transaction Processing
                                           │
                                           ▼
                                      State Machine
                                           │
                                  ┌────────┴────────┐
                                  │                 │
                              Invalid            Valid
                                  │                 │
                                  ▼                 ▼
                               Reject       Optimistic Lock
                                                   │
                                                   ▼
                                              State + Audit
                                                   │
                                                   |
                                                   |
                                                   │
                                                   │
                              ┌────────────────────┘
                              │
                              │       Independent Source
                              │              │
                              │              ▼
                              │      ┌─────────────────┐
                              │      │ Provider Ledger │
                              │      │                 │
                              │      │    Razorpay     │
                              │      │     PayPal      │
                              │      │    Wallet       │
                              │      └────────┬────────┘
                              │               │
                              ▼               ▼
                        Internal Ledger   Provider Ledger
                              │               │
                              └───────┬───────┘
                                      │
                                      ▼
                             ┌──────────────────┐
                             │  Reconciliation  │
                             │      Engine      │
                             └────────┬─────────┘
                                      │
                        ┌─────────────┼─────────────┐
                        │             │             │
                        ▼             ▼             ▼
                    MATCHED       MISMATCHED     MISSING
                                      │
                                      ▼
                             Reconciliation Result
                                      │
                                      ▼
                             Persist / Investigate
```

The key architectural boundary is:

Internal Ledger ≠ Provider Ledger

They are independent sources of financial truth for their respective systems. Reconciliation will determine where they agree, where they disagree, and how those discrepancies should be classified.

### Business Rules

Transaction acceptance is governed by configurable policies including:

* Transaction amount limits
* Transaction velocity limits
* Provider-specific rules

These rules are evaluated before transaction processing and can be changed through configuration without modifying the core transaction workflow.

### Consistency Guarantees

The transaction core currently provides:

* **Idempotency** — duplicate requests do not create duplicate transactions.
* **Optimistic locking** — concurrent updates are protected from stale writes.
* **State-machine enforcement** — only valid transaction state transitions are permitted.
* **Immutable auditing** — state changes are recorded in an append-only audit trail.
* **Business-rule enforcement** — invalid transactions are rejected before processing.
* **Configurable policies** — financial rules can be adjusted without changing the transaction processing flow.
* **Atomic persistence** — transaction state changes and their corresponding audit records are persisted together.

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

### Stage 5 — Business Rule Enforcement ✅

Introduced domain-level financial business rules to prevent invalid transaction operations.

Current capabilities include:

- Transaction amount validation
- Currency validation
- Business-rule validation before state changes
- Prevention of invalid transaction operations
- Centralized domain/business rule enforcement
- Tests covering valid and invalid business scenarios

### Stage 6 — Configurable Business Rules ✅

Introduced configurable business rules for transaction validation, allowing financial policies to be changed without modifying the core transaction processing flow.

Current rules include:

- Configurable transaction amount limits
- Transaction velocity limits
- Provider-specific rules
- Centralized business rule evaluation
- Rule-driven transaction acceptance/rejection
- Configuration-based policy enforcement
- Tests covering rule evaluation and rejection scenarios

### Stage 7 — External Provider Ledger ✅

Introduced an independently sourced provider ledger to represent external financial records.

The provider ledger is intentionally independent from the internal transaction ledger and can contain differences that must later be detected and reconciled.

Current capabilities include:

- Separate provider_transaction ledger
- Provider statement import/parsing
- Mock provider data for Razorpay, PayPal, and wallet sources
- Provider-specific status normalization
- Independent provider transaction records
- Support for intentionally inconsistent provider data
- Missing records
- Amount mismatches
- Status mismatches
- Timestamp differences
- Duplicate provider records

The provider ledger is not generated from the internal ledger. Both ledgers can be populated independently, allowing the system to model real-world financial discrepancies.

### Stage 8 — Reconciliation Engine ✅

Implemented the reconciliation engine that compares the internal transaction ledger against the independent provider ledger.

The engine identifies and classifies discrepancies between the two sources without assuming that either ledger is automatically correct.

Current capabilities include:

- Internal vs. provider ledger comparison
- Transaction matching
- Missing internal transactions
- Missing provider transactions
- Amount mismatches
- Status mismatches
- Timestamp differences
- Duplicate provider records
- Reconciliation result classification
- Persistence of reconciliation results
- Reconciliation history and reporting

The provider ledger remains an independent source and is intentionally allowed to disagree with the internal ledger. Reconciliation surfaces these differences for investigation rather than silently modifying either source.

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
Stage 4  ✅ Idempotent Ingestion & Concurrency
Stage 5  ✅ Business Rule Enforcement
Stage 6  ✅ Configurable Business Rules
Stage 7  ✅ External Provider Ledger
Stage 8  ✅ Reconciliation Engine
Stage 9  ⏳ Reconciliation Scheduling & Alerts
Stage 10+ ⏳ Planned
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
* Configurable financial policies
* Rule-driven transaction validation
* Provider-specific business constraints
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

**Current milestone: Stage 8 completed.**

The next milestone focuses on introducing **Reconciliation Scheduler** into the transaction platform.

---

This README will evolve alongside the platform as new architectural components and production capabilities are implemented.
