# RideLink - Driver & Vehicle Service

## Overview

Driver & Vehicle Service is a microservice of the RideLink ride-sharing platform.

This service is responsible for:

- Driver management
- Vehicle management
- Driver availability management
- Service area management
- Current location management
- Retrieving available drivers
- JWT validation and authorization

---

## Technologies Used

- Java 17
- Spring Boot 3.5.6
- Spring Security
- Spring Data MongoDB
- MongoDB Atlas
- Swagger / OpenAPI
- Maven
- JWT Authentication

---

## Project Structure

src/main/java/com/ridelink/driver_vehicle_service

- controller
- service
- repository
- model
- dto
- exception
- config
- security

---

## Features

### Driver Management

- Create Driver
- Get All Drivers
- Get Driver By ID
- Update Driver
- Get Available Drivers

### Vehicle Management

- Create Vehicle
- Get All Vehicles
- Get Vehicle By ID
- Update Vehicle

### Security

- JWT Validation
- Spring Security
- Role-Based Authorization
- Unauthorized Request Protection

---

## Database

MongoDB Atlas

Database:

DriverVehicleDB

Collections:

- drivers
- vehicles

---

## Running the Service

### Clone Repository

```bash
git clone https://github.com/shaleena2004/RideLink-IT3130.git
```

### Navigate to Service

```bash
cd driver-vehicle-service
```

### Build Project

```bash
./mvnw clean install
```

### Run Project

```bash
./mvnw spring-boot:run
```

---

## Configuration

application.properties

```properties
server.port=8080

spring.application.name=driver-vehicle-service

spring.data.mongodb.uri=YOUR_MONGODB_URI

spring.data.mongodb.database=DriverVehicleDB
```

---

## Swagger Documentation

Open:

```text
http://localhost:8080/swagger-ui.html
```

or

```text
http://localhost:8080/swagger-ui/index.html
```

---

## API Endpoints

### Driver APIs

| Method | Endpoint |
|----------|-----------|
| POST | /api/drivers |
| GET | /api/drivers |
| GET | /api/drivers/{id} |
| PUT | /api/drivers/{id} |
| GET | /api/drivers/available |

### Vehicle APIs

| Method | Endpoint |
|----------|-----------|
| POST | /api/vehicles |
| GET | /api/vehicles |
| GET | /api/vehicles/{id} |
| PUT | /api/vehicles/{id} |

---

## Security

This service uses JWT validation.

Protected APIs require:

```text
Authorization: Bearer <JWT_TOKEN>
```

Requests without valid JWT tokens return:

```text
401 Unauthorized
```

JWT tokens are issued by the Account Service.

---

## Testing

### Successful Scenarios

- Create Driver
- Create Vehicle
- Retrieve Drivers
- Retrieve Vehicles
- Update Drivers
- Update Vehicles

### Negative Scenarios

- Unauthorized
