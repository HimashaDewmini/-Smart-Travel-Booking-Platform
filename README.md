# Smart Travel Booking Platform

A distributed microservices-based travel booking system built using **Spring Boot 3+, Java 17, H2 in-memory databases, WebClient, and Feign Client**.
The platform demonstrates **inter-service communication**, **orchestration**, **exception handling**, and **clean REST API design**, following assignment specifications.

---
## Architecture Overview

The system consists of **6 microservices**, each running independently:

┌───────────────────────────────────────────────────────────────────┐
│                        BOOKING SERVICE                             │
│                  (Main Orchestrator - Port 8084)                   │
└──────────────┬──────────────────────────────┬──────────────────────┘
               │                              │
     ┌─────────┴──────────┐        ┌──────────┴───────────┐
     │                    │        │                      │
     ▼                    ▼        ▼                      ▼
┌─────────────┐   ┌─────────────┐ ┌─────────────┐   ┌───────────────┐
│ User Service│   │Flight Service│ │Hotel Service│   │Notification svc│
│   (8081)    │   │    (8082)    │ │    (8083)   │   │     (8086)     │
│ WebClient   │   │ Feign Client │ │ Feign Client│   │  WebClient     │
└─────────────┘   └─────────────┘ └─────────────┘   └───────────────┘

                     ┌───────────────────────────┐
                     │     Payment Service        │
                     │          (8085)            │
                     │        WebClient           │
                     └───────────────────────────┘
