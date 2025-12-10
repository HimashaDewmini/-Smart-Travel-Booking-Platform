# 🌍 Smart Travel Booking Platform

### ITS 4243 – Microservices and Cloud Computing | Assignment 02
### University of Sri Jayewardenepura – Faculty of Technology – Department of ICT

# 📌 1. Project Introduction

A distributed **microservices-based travel booking system** developed using **Spring Boot 3+, Java 17, H2 database, WebClient, and Feign Client**.  
This platform demonstrates **service orchestration**, **inter-service communication**, **REST APIs**, **payment callbacks**, and **clean separation of microservices**, meeting the assignment requirements.

---

## 🏗 Architecture Overview

The system contains **6 independent microservices**, each running on a dedicated port:
Booking Service (8084) is the orchestrator.

User Service (8081) → via WebClient.

Flight Service (8082) → via Feign Client.

Hotel Service (8083) → via Feign Client.

Notification Service (8086) → via WebClient to send booking confirmations.

Payment Service (8085) → calls Booking Service (8084) via WebClient to update booking status after payment.

```text
                     +------------------+
                     |  User Service    |
                     |    (8081)        |
                     +------------------+
                            ^
                            | WebClient
                            |
+------------------+    +------------------+    +------------------+
| Payment Service  |--->| Booking Service  |--->| Flight Service   |
|     (8085)       |    |     (8084)       |    |     (8082)       |
+------------------+    +------------------+    +------------------+
                            | Feign
                            v
                     +------------------+
                     |  Hotel Service   |
                     |     (8083)       |
                     +------------------+
                            |
                            | WebClient
                            v
                     +------------------+
                     | Notification     |
                     | Service (8086)   |
                     +------------------+

```
## 📁 Project Structure – Smart Travel Booking Platform

```text
Smart-Travel-Booking-Platform/
│
├── user-service/              (8081)
├── flight-service/            (8082)
├── hotel-service/             (8083)
├── booking-service/           (8084)
├── payment-service/           (8085)
├── notification-service/      (8086)

```
## Each service includes:
- **controller** 
- **service** 
- **repository** 
- **dto** 
- **entity** 
- **config**  
- **exception** 
---
## 📦 Microservices Summary

### 1️⃣ **User Service – Port 8081**
Manages user information.

**Core Endpoints**
- `POST /users`
- `GET /users/{id}`
- `GET /users`

Used by Booking Service → **WebClient**

---

### 2️⃣ **Flight Service – Port 8082**
Manages flights and availability.

**Endpoints**
- `POST /flights`
- `GET /flights/{id}`
- `GET /flights/{id}/availability`

Used by Booking Service → **Feign Client**

---

### 3️⃣ **Hotel Service – Port 8083**
Manages hotel info and room availability.

**Endpoints**
- `POST /hotels`
- `GET /hotels/{id}`
- `GET /hotels/{id}/availability`

Used by Booking Service → **Feign Client**

---

### 4️⃣ **Booking Service – Port 8084**
Main orchestrator coordinating the entire booking workflow.

**Responsibilities**
- Validate user (WebClient)
- Fetch flight details (Feign)
- Fetch hotel details (Feign)
- Compute total cost
- Save booking (PENDING → CONFIRMED)
- Call Payment Service (WebClient)
- Send notifications (WebClient)
- Handle payment callbacks

**Endpoints**
- `POST /bookings`
- `GET /bookings`
- `GET /bookings/{id}`
- `POST /bookings/update-status`

---

### 5️⃣ **Payment Service – Port 8085**
Processes payment and notifies the booking service.

**Endpoints**
- `POST /payments`


---

### 6️⃣ **Notification Service – Port 8086**
Sends fake notifications (console output).

**Endpoint**
- `POST /notify`

---

## 🔗 Communication Pattern

| From | To | Method | Purpose |
|------|----|--------|---------|
| Booking | User | WebClient | Validate user |
| Booking | Flight | Feign | Fetch flight details |
| Booking | Hotel | Feign | Fetch hotel details |
| Booking | Payment | WebClient | Process payment |
| Payment | Booking | WebClient | Send payment callback |
| Booking | Notification | WebClient | Notify user |

---

## 🔄 Booking Flow (End-to-End)

1. Client sends a booking request → `/bookings`
2. Booking Service:
    - WebClient → User Service (validate user)
    - Feign → Flight Service (check flight)
    - Feign → Hotel Service (check hotel)
3. Total cost = flight.price + hotel.price
4. Saved as **PENDING**
5. WebClient → Payment Service (process payment)
6. Payment Service → sends callback to Booking
7. Booking Service updates status → **CONFIRMED**
8. WebClient → Notification Service
9. Response returned to client

---

## 🧪 API Testing (Postman)

### Example: Create Booking
POST http://localhost:8084/bookings

Content-Type: application/json

Body:
```json
{
  "userId": 2,
  "flightId": 1,
  "hotelId": 2,
  "travelDate": "2025-01-10"
}
```

💾 Databases

Each service uses its own H2 in-memory database, loaded at startup.

| Service      | DB Name         |
| ------------ | ---------       |
| User         | userdb          |
| Flight       | flightdb        |
| Hotel        | hoteldb         |
| Booking      | bookingdb       |
| Payment      | paymentdb       |
| Notification | notificationdb  |

Access H2 console:
```
http://localhost:<port>/h2-console
```

## 🛠 Technology Stack

- **Java 17**
- **Spring Boot 3.2**
- **Spring Web**
- **Spring Data JPA**
- **Spring Cloud OpenFeign**
- **WebClient**
- **H2 Database**
- **Maven**

## ▶️ How to Run the Platform

Run each service in separate terminals or using IntelliJ Run Configurations:

### User Service (8081)
```bash
cd user-service
mvn spring-boot:run
```
### Flight Service (8082)
```bash
cd flight-service
mvn spring-boot:run
```
### Hotel Service (8083)
```bash
cd hotel-service
mvn spring-boot:run
```

### Booking Service (8084)
```bash
cd booking-service
mvn spring-boot:run
```
### Payment Service (8085)
```bash
cd payment-service
mvn spring-boot:run
```
###Notification Service (8086)
```bash
cd notification-service
mvn spring-boot:run
```
## 📜 API Documentation (Swagger)
| Service      | Swagger URL                       |
|-------------|-----------------------------------|
| User        | `http://localhost:8081/swagger-ui/index.html` |
| Booking     | `http://localhost:8084/swagger-ui/index.html` |
| Payment     | `http://localhost:8085/swagger-ui/index.html` |
| Notification| `http://localhost:8086/swagger-ui/index.html` |
| Flight      | `http://localhost:8082/swagger-ui/index.html` |
| Hotel       | `http://localhost:8083/swagger-ui/index.html` |
