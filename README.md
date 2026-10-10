# RideLink Microservices

RideLink is a ride-sharing backend built as four independently runnable Spring Boot services. The services expose REST APIs documented with OpenAPI/Swagger and communicate over HTTP where needed.

## Components

| Service                                         |   Port | Responsibilities                                                                                      | Collections                         |
| ----------------------------------------------- | -----: | ----------------------------------------------------------------------------------------------------- | ----------------------------------- |
| [Account Service](account-service/)             | `8081` | Account registration, login, profile retrieval and updates; issues JWTs used by protected services.   | `user_accounts`                     |
| [Driver & Vehicle Service](driver-service/)     | `8080` | Driver and vehicle records, driver availability, service areas and current locations; validates JWTs. | `drivers`, `vehicles`               |
| [Ride Service](ride-service/)                   | `8082` | Ride creation and retrieval, fare estimates, and ride accept/start/complete/cancel lifecycle.         | `rides`                             |
| [Fare & Payment Service](fare-payment-service/) | `8083` | Fare records, payment creation and processing/status, and receipt generation.                        | `fares`, `payments`, `receipts`     |

Each service has its own Maven project and can be built and run separately. The driver service hosts an aggregated Swagger UI with a definition selector for all four services.

All four services share the MongoDB database `ridelink`; the collections in the table are separated by service.

## Requirements

- Java 21 or newer
- Maven
- MongoDB access configured through the root `.env` file

## Run the Services

Open a separate terminal for each service from the repository root:

```bash
mvn -f account-service/pom.xml spring-boot:run
```

```bash
mvn -f driver-service/pom.xml spring-boot:run
```

```bash
mvn -f ride-service/pom.xml spring-boot:run
```

```bash
mvn -f fare-payment-service/pom.xml spring-boot:run
```

Wait for each service to report that it has started before using its APIs. To stop a service, press `Ctrl+C` in that service's terminal.

### Environment Configuration

Set `MONGODB_URI` in the root `.env` file to the connection string for your MongoDB deployment, and set `MONGODB_DATABASE` to `ridelink`. All services use that single database and keep their records in separate MongoDB collections. Use `.env.example` as a reference; never commit `.env` or put database credentials in source files.

Each service imports the root `.env` whether launched from the repository root or its own service directory. If `.env` is absent, services fall back to `mongodb://localhost:27017`.

All services use JWT validation for protected operations. Use the Account Service to register or log in and obtain a token. Set `RIDELINK_JWT_SECRET` in the root `.env`; services that validate tokens must use the same strong secret.

## API Documentation

The aggregated Swagger UI is served by the Driver & Vehicle Service:

- [Aggregated Swagger UI](http://localhost:8080/swagger-ui/index.html)
- Driver & Vehicle Service OpenAPI: <http://localhost:8080/v3/api-docs>
- Account Service Swagger UI: <http://localhost:8081/swagger-ui/index.html>
- Account Service OpenAPI: <http://localhost:8081/v3/api-docs>
- Ride Service Swagger UI: <http://localhost:8082/swagger-ui/index.html>
- Ride Service OpenAPI: <http://localhost:8082/v3/api-docs>
- Fare & Payment Service Swagger UI: <http://localhost:8083/swagger-ui/index.html>
- Fare & Payment Service OpenAPI: <http://localhost:8083/v3/api-docs>

The aggregated UI loads definitions from the other local ports, so start all four services to browse every definition there. The Ride and Fare services allow local cross-origin requests for this Swagger setup.

## API Areas

### Account Service (`8081`)

- `POST /api/auth/register` and `POST /api/auth/login`
- `GET /api/accounts/me`
- `GET /api/accounts/{id}`
- `PATCH /api/accounts/{id}`

### Driver & Vehicle Service (`8080`)

- Driver operations under `/api/drivers`: create, list, retrieve, update, and find available drivers
- Vehicle operations under `/api/vehicles`: create, list, retrieve, and update

### Ride Service (`8082`)

- `POST /api/rides` and `GET /api/rides`
- `GET /api/rides/{id}`
- Ride actions under `/api/rides/{id}`: fare estimate, accept, start, complete, and cancel

### Fare & Payment Service (`8083`)

- Fare operations under `/api/fares`: create, list, and retrieve
- Payment operations under `/api/payments`: create, list, retrieve, process, and mark failed
- Receipt operations under `/api/receipts`: create for a payment, list, and retrieve

## Authentication

Protected endpoints expect a bearer token in the `Authorization` header:

```text
Authorization: Bearer <JWT_TOKEN>
```

Use the Account Service's login endpoint to obtain a token. Swagger's **Authorize** control can be used to supply it when testing protected operations. Requests without a valid token may return `401 Unauthorized` or `403 Forbidden`, depending on the endpoint's authorization rules.
