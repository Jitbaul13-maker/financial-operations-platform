# Financial Operations Platform

A backend-focused financial operations platform designed to model reliable transaction processing and gradually evolve toward a production-grade distributed financial system.

The project is being developed incrementally, with an emphasis on correctness, explicit state management, testing, observability, and production-oriented engineering practices.

> **Status:** 🚧 Under active development — Stage 2 completed.

## Tech Stack

### Currently Implemented

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* Docker / Docker Compose
* JUnit

Additional infrastructure and distributed-system components will be introduced as the project progresses.

## Current Architecture

The application currently follows a **modular monolith** architecture.

The initial stages intentionally focus on building a reliable transaction core before introducing asynchronous processing, caching, security, observability, and distributed infrastructure.

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

Valid transitions are explicitly controlled by the domain rather than allowing arbitrary status changes.

Invalid transitions are rejected, protecting transaction lifecycle integrity.

State transition behavior is covered by automated tests.

## Transaction States

| State        | Description                          |
| ------------ | ------------------------------------ |
| `INITIATED`  | Transaction has been created         |
| `PROCESSING` | Transaction is being processed       |
| `COMPLETED`  | Transaction completed successfully   |
| `FAILED`     | Transaction processing failed        |
| `REVERSED`   | A completed transaction was reversed |

## Development Roadmap

The platform is being built incrementally.

```text
Stage 0  ✅  Project Bootstrap
Stage 1  ✅  Transaction Ledger
Stage 2  ✅  Transaction State Machine
Stage 3  ⏳  Idempotency & Concurrency
Stage 4+ ⏳  Planned
```

Future stages will introduce concepts including:

* Idempotent transaction processing
* Concurrency control
* Redis
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

**Current milestone: Stage 2 completed.**

The next milestone focuses on **idempotency and concurrency control**.

---

This README will evolve alongside the platform as new architectural components and production capabilities are implemented.
