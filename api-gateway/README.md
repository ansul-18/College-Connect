# API Gateway

API Gateway is the **single entry point** for the B.Tech College Connect microservices architecture.

Instead of the frontend directly communicating with every microservice, requests first reach the API Gateway, which routes them to the appropriate service.

```text
Frontend
   |
   v
API Gateway :8080
   |
   +----> Auth Service
   +----> User Service
   +----> Department Service
   +----> College Service
   +----> Mentor Service
   +----> Community Service
   +----> Chat Service
```

---

## 1. Purpose

The API Gateway is responsible for:

* Providing a single entry point for the frontend
* Routing requests to microservices
* Discovering services through Eureka
* Hiding individual service ports from the frontend
* Providing a central place for future filters and security-related processing

---

## 2. Technologies Used

* Java
* Spring Boot
* Spring Cloud Gateway
* Spring Cloud Netflix Eureka Client
* Spring Boot Actuator

---

## 3. Dependencies

### Spring Cloud Gateway

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

**Why?**

Provides the API Gateway functionality, including request routing.

---

### Eureka Client

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**Why?**

Allows the Gateway to communicate with Eureka and discover registered microservices.

Instead of hardcoding service locations, the Gateway can use service names.

```text
API Gateway
     |
     v
Eureka
     |
     +---- AUTH-SERVICE
     +---- USER-SERVICE
     +---- COLLEGE-SERVICE
```

---

### Spring Boot Actuator

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Why?**

Used for application health and monitoring.

---

## 4. Project Structure

```text
api-gateway/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.collegeconnect.gateway/
│       │       └── ApiGatewayApplication.java
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
└── README.md
```

---

# 5. Application Properties

## Server Port

```properties
server.port=8080
```

**Why?**

Runs the API Gateway on port `8080`.

```text
Frontend
   |
   v
localhost:8080
   |
   v
API Gateway
```

---

## Application Name

```properties
spring.application.name=API-GATEWAY
```

**Why?**

Defines the logical name of the application.

This name is used when identifying the Gateway in the microservices environment and Eureka registry.

---

## Eureka Server URL

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**Why?**

Tells the Gateway where the Eureka Server is running.

```text
API Gateway
     |
     | Eureka Client
     v
Eureka Server
localhost:8761
```

---

## Register with Eureka

```properties
eureka.client.register-with-eureka=true
```

**Why?**

Registers the API Gateway itself with Eureka.

```text
API Gateway starts
       |
       v
Eureka Server
       |
       v
API-GATEWAY registered
```

---

## Fetch Registry

```properties
eureka.client.fetch-registry=true
```

**Why?**

Allows the Gateway to fetch information about services registered with Eureka.

Example:

```text
Eureka Registry

AUTH-SERVICE
USER-SERVICE
DEPARTMENT-SERVICE
COLLEGE-SERVICE
MENTOR-SERVICE
COMMUNITY-SERVICE
CHAT-SERVICE
```

---

## Prefer IP Address

If used:

```properties
eureka.instance.prefer-ip-address=true
```

**Why?**

Makes the Eureka instance prefer its IP address when advertising itself.

This can be useful in development environments where hostname resolution may cause issues.

---

# 6. Quick Property Reference

```text
server.port
→ Port on which Gateway runs

spring.application.name
→ Logical application/service name

eureka.client.service-url.defaultZone
→ Eureka Server address

eureka.client.register-with-eureka
→ Whether Gateway registers itself with Eureka

eureka.client.fetch-registry
→ Whether Gateway fetches registered services

eureka.instance.prefer-ip-address
→ Prefer IP address for Eureka instance information
```

---

# 7. Gateway Request Flow

Example:

```text
Frontend
   |
   | GET /api/events
   v
API Gateway :8080
   |
   v
Eureka
   |
   | Find COLLEGE-SERVICE
   v
COLLEGE-SERVICE
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
college_db
```

Response:

```text
college_db
    ↓
College Service
    ↓
API Gateway
    ↓
Frontend
```

---

# 8. Routes

The Gateway will provide routes for the project services.

```text
/api/auth/**
        ↓
AUTH-SERVICE

/api/users/**
        ↓
USER-SERVICE

/api/departments/**
        ↓
DEPARTMENT-SERVICE

/api/events/**
        ↓
COLLEGE-SERVICE

/api/mentors/**
        ↓
MENTOR-SERVICE

/api/community/**
        ↓
COMMUNITY-SERVICE

/api/chat/**
        ↓
CHAT-SERVICE
```

The frontend therefore communicates through:

```text
http://localhost:8080
```

instead of directly calling every microservice.

---

# 9. Why Eureka + Gateway?

Without service discovery:

```text
Gateway
   |
   +----> http://localhost:8081
   +----> http://localhost:8082
   +----> http://localhost:8083
```

The Gateway would need fixed service addresses.

With Eureka:

```text
Gateway
   |
   v
Eureka
   |
   +----> AUTH-SERVICE
   +----> USER-SERVICE
   +----> COLLEGE-SERVICE
```

The Gateway discovers services using their registered service names.

---

# 10. Important Concepts Explored

```text
API Gateway
Spring Cloud Gateway
Route Configuration
Service Discovery
Eureka Client
Eureka Server
Microservice Routing
Logical Service Names
Single Entry Point
Spring Boot Actuator
```

---

# 11. Reusable Knowledge

When creating an API Gateway for another Spring Boot microservices project:

```text
1. Create a Spring Boot project
        ↓
2. Add Spring Cloud Gateway
        ↓
3. Add Eureka Client if Eureka is used
        ↓
4. Configure Gateway port
        ↓
5. Configure Eureka Server URL
        ↓
6. Configure routes
        ↓
7. Start Eureka
        ↓
8. Start microservices
        ↓
9. Start Gateway
        ↓
10. Test requests through Gateway
```

Usually, these parts change between projects:

```text
Service Names
Route Paths
Ports
Eureka URL
Gateway Filters
Security Configuration
```

---

# 12. Current Architecture

```text
                         FRONTEND
                            |
                            v
                     API GATEWAY :8080
                            |
                            v
                       EUREKA :8761
                            |
          +-----------------+------------------+
          |        |        |        |         |
          v        v        v        v         v
        AUTH      USER   DEPARTMENT COLLEGE  MENTOR
                                                   |
                                                   v
                                              COMMUNITY
                                                   |
                                                   v
                                                 CHAT
```

---

# 13. Development Status

```text
Eureka Server  → DONE
API Gateway    → IN PROGRESS
Auth Service   → NEXT
```

---

## Notes

This README is maintained as a **developer reference** for the API Gateway.

The focus is on:

```text
What was used?
Why was it used?
Where is it configured?
How does the request flow?
What should I remember for the next project?
```
