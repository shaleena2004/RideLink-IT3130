# RideLink Microservices Architecture

RideLink is a distributed ride-sharing backend system built with Spring Boot.

## Branch Strategy

This repository uses feature-branch isolation for each microservice:

```
├── main (Base repository structure)
├── feature/account-service (User account management, JWT authentication & profile handling)
├── feature/driver-vehicle-service (Driver profiles & vehicle management)
├── feature/ride-management-service (Ride requests, booking, matching & tracking)
└── feature/fare-payment-service (Fare calculation, payment processing & invoices)
```

## Services Overview

| Service | Feature Branch | Default Port | Description |
|---|---|---|---|
| **Account Service** | `feature/account-service` | `8081` | Authentication (JWT), user registration, profile management |
| **Driver & Vehicle Service** | `feature/driver-vehicle-service` | `8082` | Driver onboarding, vehicle registration, status |
| **Ride Management Service** | `feature/ride-management-service` | `8083` | Ride booking, lifecycle management, dispatch |
| **Fare & Payment Service** | `feature/fare-payment-service` | `8084` | Pricing calculation, payments, transaction logs |
