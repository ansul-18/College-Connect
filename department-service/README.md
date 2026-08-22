# Department Service

Department Service is responsible for managing college departments.

It provides APIs to create, retrieve, update, and delete department information.

```text
Client
   |
   v
API Gateway :8080
   |
   v
DEPARTMENT-SERVICE :8083
   |
   +----> Create Department
   +----> Get All Departments
   +----> Get Department By ID
   +----> Update Department
   +----> Delete Department
   |
   v
department_db
```

---

## Purpose

The Department Service is responsible for:

- Creating departments
- Retrieving all departments
- Retrieving a department by ID
- Updating department information
- Deleting departments
- Validating department request data
- Handling exceptions globally
- Registering itself with Eureka

---

## Technologies Used

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Spring Cloud Netflix Eureka Client
- Spring Boot Actuator
- Spring Boot Validation
- Maven
- Lombok

---

## Dependencies

### Spring Web

**Why?**

Used to create REST APIs.

```text
POST   /api/departments
GET    /api/departments
GET    /api/departments/{id}
PUT    /api/departments/{id}
DELETE /api/departments/{id}
```

### Spring Data JPA

**Why?**

Used for database operations through JPA and Hibernate.

```text
Department Entity
        |
        v
DepartmentRepository
        |
        v
department_db
```

### MySQL Driver

**Why?**

Used to connect the Department Service with the MySQL database.

### Validation

**Why?**

Used to validate incoming department request data.

Important annotations:

```text
@NotBlank
@Size
@Valid
```

### Eureka Client

**Why?**

Used to register the Department Service with Eureka.

```text
DEPARTMENT-SERVICE
        |
        v
Eureka Server :8761
```

### Spring Boot Actuator

**Why?**

Used for application health and monitoring.

---

## Project Structure

```text
department-service/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/cllg/department_service/
│       │
│       │       ├── DepartmentServiceApplication.java
│       │       │
│       │       ├── controller/
│       │       │   └── DepartmentController.java
│       │       │
│       │       ├── service/
│       │       │   ├── DepartmentService.java
│       │       │   └── DepartmentServiceImpl.java
│       │       │
│       │       ├── repository/
│       │       │   └── DepartmentRepository.java
│       │       │
│       │       ├── entity/
│       │       │   └── Department.java
│       │       │
│       │       ├── dto/
│       │       │   ├── DepartmentRequest.java
│       │       │   └── DepartmentResponse.java
│       │       │
│       │       └── exception/
│       │           ├── ResourceNotFoundException.java
│       │           ├── ResourceAlreadyExistsException.java
│       │           ├── ErrorResponse.java
│       │           └── GlobalExceptionHandler.java
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
└── README.md
```

### Folder Purpose

| Folder | Purpose |
|---|---|
| `controller` | Handles incoming HTTP requests |
| `service` | Contains business logic |
| `repository` | Communicates with the database |
| `entity` | Represents the database table |
| `dto` | Handles API request and response data |
| `exception` | Handles custom and global exceptions |

---

## Important Annotations

```text
@SpringBootApplication
→ Starts the Spring Boot application

@RestController
→ Creates REST API endpoints

@RequestMapping
→ Defines the base API path

@PostMapping
→ Handles create requests

@GetMapping
→ Handles read requests

@PutMapping
→ Handles update requests

@DeleteMapping
→ Handles delete requests

@PathVariable
→ Reads values from the URL

@RequestBody
→ Converts request JSON into a Java object

@Valid
→ Validates incoming request data

@Service
→ Marks the business logic class

@Entity
→ Maps a Java class to a database table

@Id
→ Defines the primary key

@GeneratedValue
→ Generates primary key values

@NotBlank
→ Ensures a field is not empty

@Size
→ Validates field length

@RestControllerAdvice
→ Handles exceptions globally

@ExceptionHandler
→ Handles specific exceptions
```

---

## Application Properties

### Application Name

```properties
spring.application.name=department-service
```

Defines the logical name of the service.

This name is used when the service registers with Eureka.

---

### Server Port

```properties
server.port=8083
```

Runs the Department Service on port `8083`.

---

### MySQL Database

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/department_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Kolkata
```

Important parts:

```text
createDatabaseIfNotExist=true
→ Creates the database if it does not exist

useSSL=false
→ Disables SSL for local development

serverTimezone=Asia/Kolkata
→ Sets the database connection timezone
```

---

### Database Username

```properties
spring.datasource.username=root
```

Used to authenticate with MySQL.

---

### Database Password

```properties
spring.datasource.password=YOUR_PASSWORD
```

Used to connect to the MySQL database.

---

### Hibernate DDL

```properties
spring.jpa.hibernate.ddl-auto=update
```

Automatically updates the database schema based on entity changes.

---

### Show SQL

```properties
spring.jpa.show-sql=true
```

Shows generated SQL queries in the console.

---

### Format SQL

```properties
spring.jpa.properties.hibernate.format_sql=true
```

Formats generated SQL queries to make them easier to read.

---

### Eureka Server

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

Tells the Department Service where Eureka Server is running.

---

### Register With Eureka

```properties
eureka.client.register-with-eureka=true
```

Registers the Department Service with Eureka.

---

### Fetch Registry

```properties
eureka.client.fetch-registry=true
```

Fetches information about other registered services.

---

### Prefer IP Address

```properties
eureka.instance.prefer-ip-address=true
```

Makes Eureka prefer the service IP address during registration.

---

## Quick Property Reference

```text
spring.application.name
→ Logical service name

server.port
→ Port on which the service runs

spring.datasource.url
→ MySQL database connection

spring.datasource.username
→ MySQL username

spring.datasource.password
→ MySQL password

spring.jpa.hibernate.ddl-auto
→ Database schema management

spring.jpa.show-sql
→ Shows SQL queries

spring.jpa.properties.hibernate.format_sql
→ Formats SQL output

eureka.client.service-url.defaultZone
→ Eureka Server address

eureka.client.register-with-eureka
→ Registers service with Eureka

eureka.client.fetch-registry
→ Fetches Eureka registry

eureka.instance.prefer-ip-address
→ Prefers IP address during registration
```

---

## Request Flow

### Create Department

```text
Client
   |
   | POST /api/departments
   v
DepartmentController
   |
   v
DepartmentService
   |
   v
DepartmentServiceImpl
   |
   v
DepartmentRepository
   |
   v
department_db
   |
   v
DepartmentResponse
```

### Get All Departments

```text
Client
   |
   | GET /api/departments
   v
DepartmentController
   |
   v
DepartmentService
   |
   v
DepartmentRepository
   |
   v
department_db
   |
   v
List<DepartmentResponse>
```

### Get Department By ID

```text
Client
   |
   | GET /api/departments/{id}
   v
DepartmentController
   |
   v
DepartmentService
   |
   v
DepartmentRepository
   |
   v
department_db
   |
   v
DepartmentResponse
```

### Update Department

```text
Client
   |
   | PUT /api/departments/{id}
   v
DepartmentController
   |
   v
DepartmentService
   |
   v
Find Existing Department
   |
   v
Update Department
   |
   v
DepartmentRepository
   |
   v
department_db
```

### Delete Department

```text
Client
   |
   | DELETE /api/departments/{id}
   v
DepartmentController
   |
   v
DepartmentService
   |
   v
Find Department
   |
   v
DepartmentRepository
   |
   v
department_db
```

---

## Exception Flow

```text
Request
   |
   v
Controller
   |
   v
Exception Occurs
   |
   v
GlobalExceptionHandler
   |
   +----> ResourceNotFoundException
   |
   +----> ResourceAlreadyExistsException
   |
   +----> Validation Exception
   |
   +----> General Exception
   |
   v
ErrorResponse
```

---

## Important Concepts Explored

```text
REST API
CRUD Operations
Spring Data JPA
Hibernate
MySQL
DTO
Entity
Repository Pattern
Service Layer
Controller Layer
Request Validation
@NotBlank
@Size
@Valid
Global Exception Handling
Custom Exceptions
Eureka Client
Service Discovery
Spring Boot Actuator
```

---

## Reusable Knowledge

When creating a similar CRUD microservice:

```text
1. Create Spring Boot project
        ↓
2. Add Web dependency
        ↓
3. Add JPA
        ↓
4. Add Database
        ↓
5. Add Validation
        ↓
6. Create Entity
        ↓
7. Create DTOs
        ↓
8. Create Repository
        ↓
9. Create Service
        ↓
10. Create Controller
        ↓
11. Add Validation
        ↓
12. Add Exception Handling
        ↓
13. Configure Eureka
        ↓
14. Test CRUD APIs
```

---

## Development Status

```text
Eureka Server       → DONE
API Gateway         → DONE
Auth Service        → DONE
Department Service  → DONE
```

---

## Notes

This README is maintained as a developer reference for the Department Service.

The focus is on:

```text
What dependencies were used?
Why were they used?
Which annotations were used?
What does each application property do?
What is the CRUD flow?
How is the project structured?
How are exceptions handled?
What should be remembered for another project?
```
