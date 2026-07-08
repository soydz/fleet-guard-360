# Fleet Guard 360

Real-time fleet monitoring system with satellite tracking.  
Microservices architecture powered by Java 21, Spring Boot, GraphQL, WebSocket, and RabbitMQ.  
Containerized with Docker and served through an Nginx reverse proxy.

---

## Architecture

```
                                     ┌───────────────────────────┐
                                     │         INTERNET          │
                                     └────────────┬──────────────┘
                                                  │ :3000 (HTTPS/HTTP)
                                     ┌────────────▼──────────────┐
                                     │       Nginx Proxy :80     │
                                     │  Frontend (React SPA)     │
                                     │  ───────────────────────  │
                                     │  /api/*  -> API Gateway   │
                                     │  /notifications/* ->      │
                                     │     API Gateway (WS)      │
                                     │  /* -> index.html (SPA)   │
                                     └────────────┬──────────────┘
                                                  │ frontend-net (Docker)
                                     ┌────────────▼──────────────┐
                                     │       API Gateway         │
                                     │  Spring Cloud Gateway     │
                                     │  ───────────────────────  │
                                     │  JWT Validation           │
                                     │  Route: /auth/**          │
                                     │  Route: /alerts/**        │
                                     │  Route: /panel/**         │
                                     │  Route: /notifications/** │
                                     └──┬───────┬───────┬────────┘
                                        │       │       │    
                            ┌───────────┘ ┌─────┘       │    
                            │             │             │         
                     ┌──────▼────┐  ┌─────▼─────┐ ┌─────▼─────┐
                     │ Auth      │  │ Alerts    │ │ Panel     │
                     │ :8080     │  │ :8080     │ │ :8080     │
                     │ Spring    │  │ GraphQL   │ │ GraphQL+  │
                     │ Security  │  │ MapStruct │ │ REST      │
                     │ JWT/BCrypt│  │           │ │ RabbitMQ  │
                     └────┬──────┘  └───┬───────┘ └──┬──────┬─┘
                          │             │            │      │
                     ┌────▼─────┐   ┌───▼──────┐     │    ┌─▼────────┐
                     │ Postgres │   │ Postgres │     │    │ Postgres │
                     │  auth    │   │  alerts  │     │    │  panel   │
                     └──────────┘   └──────────┘     │    └──────────┘
                                                     │
                                                     │
                                             ┌───────▼───┐
                                             │ RabbitMQ  │
                                             │ :5672     │
                                             └─────┬─────┘
                                                   │
                                             ┌─────▼──────────┐
                                             │ Notifications  │
                                             │   :8080        │
                                             │ ────────────   │
                                             │ WebSocket Push │
                                             │ Email (SMTP)   │
                                             └────────────────┘
```

All backend services run on port `:8080` internally and communicate over two isolated Docker networks:
- **backend-net** — only backend services, databases, and RabbitMQ (no external access)
- **frontend-net** — only frontend and API Gateway (exposes port `3000` to host)

Only the frontend container exposes a port (`3000:80`) to the host.  
No backend ports are publicly accessible.

---

## Tech Stack

### Backend
![Java](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen.svg?logo=springboot&logoColor=white)
![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud%20Gateway-5.0-blue.svg?logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0-green.svg?logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6.0-blue.svg?logo=spring&logoColor=white)
![GraphQL](https://img.shields.io/badge/GraphQL-E10098?logo=graphql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?logo=rabbitmq&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-4E8CFF?logo=websocket&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?logo=JSON%20web%20tokens&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-red.svg?logo=lombok&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-yellowgreen.svg?logo=mapstruct&logoColor=white)
![Caffeine](https://img.shields.io/badge/Caffeine-000000?logo=caffeine&logoColor=white)
![OpenCSV](https://img.shields.io/badge/OpenCSV-0078D4?logo=apache&logoColor=white)

### Frontend
![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-06B6D4?logo=tailwindcss&logoColor=white)
![shadcn/ui](https://img.shields.io/badge/shadcn%2Fui-000000?logo=vercel&logoColor=white)
![SockJS](https://img.shields.io/badge/SockJS-gray?logo=javascript&logoColor=white)
![STOMPjs](https://img.shields.io/badge/STOMP.js-gray?logo=javascript&logoColor=white)

### DevOps
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white)
![GHCR](https://img.shields.io/badge/GHCR-24292E?logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-267BFF?logo=githubactions&logoColor=white)

---

## Microservices

| Service | Role | Highlights | Database |
|---------|------|------------|----------|
| **apiGateway** | Unified entry point, routing, JWT validation, CORS | Spring Cloud Gateway, per-route AuthenticationFilter | — |
| **auth-service** | Authentication, authorization, role & permission management | Spring Security, BCrypt, JWT HMAC256, Swagger/OpenAPI | PostgreSQL |
| **alerts-service** | Alert type & priority level CRUD | GraphQL, MapStruct, Clean Architecture layers | PostgreSQL |
| **panel-service** | Alert history querying, CSV export | GraphQL + REST, RabbitMQ consumer, **Caffeine token cache**, **OpenCSV export** | PostgreSQL |
| **notifications-service** | Real-time push notifications & email alerts | STOMP/SockJS WebSocket, Thymeleaf email templates, JavaMail, RabbitMQ consumer, **prueba-webSocket.html (testing tool)** | — |

All services run as stateless containers and can be scaled independently.

---

## Frontend

The React SPA communicates exclusively through Nginx, which proxies API and WebSocket traffic to the API Gateway. This eliminates CORS issues in production.

| Route | Page | Features |
|-------|------|----------|
| `/` | Login | JWT authentication, "Remember me", session expiry detection |
| `/dashboard` | Alert Configuration | Full CRUD for alert types, priority selector (dynamic), responsible area assignment |
| `/panel-de-alertas` | Alert History | Read-only alert feed with filters, real-time updates via WebSocket |
| `/estadisticas` | Analytics & Export | Embedded Power BI dashboard, CSV export with secure token |

### Real-Time Notifications

The frontend connects to the notifications WebSocket via STOMP over SockJS.  
When a new alert is created, it is pushed instantly to all connected clients — no polling required.

![Frontend login page](images/Frontend%20login%20page.png)
![Dashboard alerts](images/Dashboard%20alerts.png)
![Dashboard table and create modal](images/Dashboard%20table%20and%20create%20modal.png)
![Push notification](images/Push%20notification.png)

---

## API Gateway Routes

| Method | Route | Auth | Target | Description |
|--------|-------|------|--------|-------------|
| `POST` | `/auth/login` | — | auth-service | Authenticate user, receive JWT |
| `POST` | `/alerts/graphql` | JWT | alerts-service | Query / mutate alert types & priorities |
| `POST` | `/panel/graphql` | JWT | panel-service | Query alert history |
| `GET` | `/export/alerts.csv?token=` | Token | panel-service | Download alerts as CSV |
| `WS` | `/notifications/ws-alerts` | — | notifications-service | STOMP WebSocket endpoint |

All GraphQL endpoints accept queries and mutations. The alerts-service exposes operations for both `TipoAlerta` and `NivelPrioridad` entities.

---

## Backend Capabilities Not Yet in the Frontend

The backend exposes several features that are ready to consume but currently lack a frontend implementation:

### Priority Level Management

The alerts-service supports full CRUD for `NivelPrioridad` via GraphQL mutations (`createNivelPrioridad`, `updateNivelPrioridad`, `deleteNivelPrioridad`). Currently, priority levels must be created through an external GraphQL client — there is no administration UI.

### Individual Alert Detail

The panel-service exposes `getAlertById(id: ID!)`, enabling drill-down into a specific alert's full data. No detail view exists in the frontend yet.

### Email Notifications

When an alert is created (via RabbitMQ), the notifications service sends an HTML email through SMTP to the configured recipients. This operates entirely server-side — there is no frontend indicator of email delivery status.

### Individual Alert Type Query

The `tipoAlerta(id: Int!)` query enables fetching a single alert type by ID, useful for prefetching or validation. The frontend currently fetches the full list and filters client-side.

---

## Security

- **JWT (HMAC256)** — signed tokens with configurable expiration (default 15 minutes)
- **BCrypt** — password hashing at rest
- **Stateless** — no HTTP sessions, no CSRF tokens
- **Route-level protection** — API Gateway validates JWT before proxying to protected routes
- **Role-based authorization** — three roles: `ADMINISTRADOR` (full access), `OPERADOR` (read + update), `CONDUCTOR` (read only)
- **Internal network isolation** — backend services are unreachable from outside the Docker network

---

## Data Flow — Alert Lifecycle

```
Alert Type Defined ──► Alert Created (by external system)
                          │
                          ▼
                  ┌───────────────────┐
                  │      RabbitMQ     │
                  └───┬───────────┬───┘
                      │           │
            ┌─────────▼───┐  ┌────▼─────────────┐
            │ panel-      │  │ notifications-   │
            │ service     │  │ service          │
            │             │  │                  │
            │ Persists    │  │ Push via         │
            │ to          │  │ WebSocket        │
            │ PostgreSQL  │  │                  │
            │             │  │ Send HTML        │
            │             │  │ email (SMTP)     │
            └─────────────┘  └──────────────────┘
```

---

## Infrastructure

### Docker

Each service has a multi-stage Dockerfile:
- **Stage 1 (deps)** — install dependencies with `npm ci` / Gradle
- **Stage 2 (build)** — compile the application
- **Stage 3 (runtime)** — minimal runtime image (Nginx for frontend, Eclipse Temurin JRE for backends)

### Docker Compose

The `fleetGuard360-compose.yaml` defines:
- 5 application services (auth, alerts, panel, notifications, apiGateway)
- 3 PostgreSQL databases (db-auth, db-alerts, db-panel)
- 1 RabbitMQ with management plugin
- **Two isolated bridge networks:**
  - `backend-net` — all backend services, databases, RabbitMQ (no host access)
  - `frontend-net` — frontend and API Gateway only
- Environment variables per service injected via `.env` files
- Only frontend exposes a port to host (`3000:80`)

![Containers running](images/Containers%20running.png)

### Nginx

The frontend image includes an Nginx configuration that:
- Serves the compiled React SPA
- Proxies `/api/*` requests to the API Gateway (with path rewrite)
- Proxies `/notifications/*` with WebSocket upgrade headers
- Falls back to `index.html` for all other routes (SPA routing)

```nginx
# Nginx configuration
server {
    listen 80;
    location /api/ {
        proxy_pass http://apiGateway:8080/;
    }
    location /notifications/ {
        proxy_pass http://apiGateway:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### CI/CD

GitHub Actions builds and publishes every service to GHCR on each push:

1. Gradle build for each Java service
2. `docker build` for every service
3. `docker push` to `ghcr.io/soydz/fleet-guard-360/*`

![GitHub Container Registry](images/GitHub%20Container%20Registry.png)
![GitHub Actions workflow](images/GitHub%20Actions%20workflow.png)

On the VPS, updating the stack is a single command:

```bash
docker compose -f fleetGuard360-compose.yaml pull && docker compose -f fleetGuard360-compose.yaml up -d
```

---

## Getting Started

**Prerequisites:** Docker and Docker Compose.

1. Clone the repository
2. Copy `.env-sample` to `.env` for each service:
   ```bash
   for d in auth-service apiGateway-service alerts-service panel-service notifications-service; do
     cp $d/.env-sample $d/.env
   done
   ```
3. Start all services:
   ```bash
   docker compose -f fleetGuard360-compose.yaml up -d
   ```
4. Access the frontend at `http://localhost:3000`

A `dataTest.sql` script in `auth-service` seeds three test users:
- `lucas.ramirez@example.com` / `lucas` (ADMINISTRADOR)
- `maria.lopez@example.com` / `maria` (CONDUCTOR)
- `juan.perez@example.com` / `juan` (OPERADOR)

---

## Development Tools

- **`dataTest.sql`** — seeds test users in auth-service PostgreSQL
- **`prueba-webSocket.html`** (in `notifications-service/`) — standalone WebSocket test page. Open directly in browser while services are running to inspect real-time push notifications without the full frontend.
