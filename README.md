
# Smart Travel Booking Platform

A distributed **microservices-based travel booking system** developed using **Spring Boot 3+, Java 17, H2 database, WebClient, and Feign Client**.  
This platform demonstrates **service orchestration**, **inter-service communication**, **REST APIs**, **payment callbacks**, and **clean separation of microservices**, meeting the assignment requirements.

---

## 🏗 Architecture Overview

The system contains **6 independent microservices**, each running on a dedicated port:

<<<<<<< HEAD
## 🏛️ Booking Service Communication Flow (8084)

```text
BOOKING SERVICE (8084)
│
│  Main Orchestrator Service
└───────────┬─────────────────────────────┬────────────────────────┘
            │                             │
     ┌──────┴─────────┐           ┌────────┴─────────┐
     ▼                 ▼           ▼                  ▼
┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌──────────────┐
│ User Service │  │ Flight Service│ │ Hotel Service│ │ Notification │
│    (8081)    │  │    (8082)     │ │    (8083)    │ │ Service (8086)│
│  WebClient   │  │ Feign Client  │ │ Feign Client │ │  WebClient    │
└─────────────┘  └──────────────┘ └──────────────┘ └───────────────┘


                ┌──────────────────────────┐
                │      PAYMENT SERVICE      │
                │          (8085)           │
                │        WebClient          │
                └──────────────────────────┘
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

Use the provided Postman collection:  
**`Smart Travel Booking Platform.postman_collection.json`**  
Loaded with requests for all 6 services.

<<<<<<< HEAD
### Example: Create Booking
=======
### Example: Create Booking  
>>>>>>> c3567d410f316608230254f7076e0afb2aec2d4f
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

| Service      | DB Name   |
| ------------ | --------- |
| User         | userdb    |
| Flight       | flightdb  |
| Hotel        | hoteldb   |
| Booking      | bookingdb |
| Payment      | paymentdb |
| Notification | (No DB)   |

Access H2 console:
```
http://localhost:<port>/h2-console
```

<<<<<<< HEAD
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

example-:

### 1️⃣ User Service (8081)
```bash
cd user-service
mvn spring-boot:run

