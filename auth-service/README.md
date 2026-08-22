Auth Service

Auth Service is responsible for user registration, login, authentication, password security, and JWT token generation.

It is the central service responsible for verifying user credentials and generating tokens for authenticated users.

Client
   |
   v
API Gateway :8080
   |
   v
AUTH-SERVICE
   |
   +----> Register
   +----> Login
   +----> JWT Generation
   +----> User Authentication
   |
   v
auth_db
1. Purpose

The Auth Service is responsible for:

User registration
User login
Password encryption
User authentication
JWT token generation
JWT token validation
Role-based authentication
Registering itself with Eureka
2. Technologies Used
Java
Spring Boot
Spring Web
Spring Security
Spring Data JPA
MySQL
JWT
Spring Cloud Netflix Eureka Client
Spring Boot Actuator
Maven
3. Dependencies
Spring Web
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

Why?

Used to create REST APIs such as:

POST /api/auth/register
POST /api/auth/login
Spring Security
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

Why?

Used for authentication and security.

It provides:

PasswordEncoder
AuthenticationManager
AuthenticationProvider
SecurityFilterChain
UserDetailsService
Spring Data JPA
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

Why?

Used for database operations through JPA and Hibernate.

User Entity
     |
     v
UserRepository
     |
     v
auth_db
MySQL Driver
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

Why?

Used to connect the Auth Service with the MySQL database.

Validation
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

Why?

Used to validate incoming requests.

Example:

Email should not be empty
Password should not be empty
Password should have required validation
Eureka Client
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

Why?

Registers the Auth Service with Eureka.

AUTH-SERVICE
      |
      v
Eureka Server :8761

Other services or API Gateway can discover the Auth Service using its service name.

Spring Boot Actuator
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

Why?

Used for application health and monitoring.

JWT

JWT libraries are used for:

Generate Token
Extract User Information
Validate Token
Check Token Expiration
4. Project Structure
auth-service/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.collegeconnect.auth/
│       │
│       │       ├── AuthServiceApplication.java
│       │       │
│       │       ├── controller/
│       │       │   └── AuthController.java
│       │       │
│       │       ├── service/
│       │       │   ├── AuthService.java
│       │       │   └── CustomUserDetailsService.java
│       │       │
│       │       ├── repository/
│       │       │   └── UserRepository.java
│       │       │
│       │       ├── entity/
│       │       │   ├── User.java
│       │       │   └── Role.java
│       │       │
│       │       ├── dto/
│       │       │   ├── RegisterRequest.java
│       │       │   ├── LoginRequest.java
│       │       │   └── AuthResponse.java
│       │       │
│       │       ├── security/
│       │       │   ├── JwtService.java
│       │       │   ├── JwtAuthenticationFilter.java
│       │       │   └── SecurityConfig.java
│       │       │
│       │       └── config/
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
└── README.md
Important Folders
Folder	Purpose
controller	Handles HTTP requests
service	Contains business logic
repository	Communicates with database
entity	Database models
dto	Request and response objects
security	JWT and Spring Security configuration
5. Important Annotations
@SpringBootApplication
→ Starts the Spring Boot application


@RestController
→ Creates REST API controller


@RequestMapping
→ Defines the base API path


@PostMapping
→ Handles POST requests


@Service
→ Marks the business logic class


@Repository
→ Handles database operations


@Entity
→ Maps Java class to a database table


@Id
→ Defines the primary key


@GeneratedValue
→ Generates primary key values


@Configuration
→ Creates configuration class


@Bean
→ Creates Spring-managed beans


@Valid
→ Validates request data


@Value
→ Reads values from application.properties
6. Application Properties
Application Name
spring.application.name=AUTH-SERVICE

Why?

Defines the logical name of the service.

Eureka uses this name to identify the service.

Server Port
server.port=8081

Why?

Runs the Auth Service on port 8081.

API Gateway
     |
     v
AUTH-SERVICE :8081
MySQL URL
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db

Why?

Defines where the Auth Service database is located.

localhost
   |
MySQL :3306
   |
auth_db
Database Username
spring.datasource.username=root

Why?

Used to authenticate with MySQL.

Database Password
spring.datasource.password=YOUR_PASSWORD

Why?

Used to connect to MySQL.

Database Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

Why?

Defines the MySQL JDBC driver used by the application.

Hibernate DDL
spring.jpa.hibernate.ddl-auto=update

Why?

Automatically updates the database schema based on entity changes.

User Entity
    |
    v
Hibernate
    |
    v
MySQL Table
Show SQL
spring.jpa.show-sql=true

Why?

Shows generated SQL queries in the console.

Useful during development and debugging.

Eureka Server
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

Why?

Tells the Auth Service where Eureka Server is running.

AUTH-SERVICE
      |
      v
Eureka Server :8761
Register with Eureka
eureka.client.register-with-eureka=true

Why?

Registers the Auth Service with Eureka.

Fetch Registry
eureka.client.fetch-registry=true

Why?

Allows the Auth Service to fetch information about other registered services.

JWT Secret
jwt.secret=YOUR_SECRET_KEY

Why?

Used to sign and validate JWT tokens.

User Data
    |
    v
JWT Secret
    |
    v
Signed JWT Token
JWT Expiration
jwt.expiration=86400000

Why?

Defines how long the JWT token remains valid.

86400000 milliseconds
        ↓
24 Hours
7. Quick Property Reference
spring.application.name
→ Logical service name


server.port
→ Port on which Auth Service runs


spring.datasource.url
→ MySQL database connection URL


spring.datasource.username
→ MySQL username


spring.datasource.password
→ MySQL password


spring.datasource.driver-class-name
→ MySQL JDBC driver


spring.jpa.hibernate.ddl-auto
→ Database schema management


spring.jpa.show-sql
→ Shows SQL queries


eureka.client.service-url.defaultZone
→ Eureka Server address


eureka.client.register-with-eureka
→ Registers Auth Service with Eureka


eureka.client.fetch-registry
→ Fetches Eureka registry


jwt.secret
→ Used to sign and validate JWT


jwt.expiration
→ JWT token validity duration
8. Authentication Flow
Registration
User
   |
   | Register Request
   v
AuthController
   |
   v
AuthService
   |
   | Check Existing User
   v
UserRepository
   |
   v
PasswordEncoder
   |
   | Encrypt Password
   v
auth_db
   |
   v
Generate JWT
   |
   v
AuthResponse
Login
User
   |
   | Email + Password
   v
AuthController
   |
   v
AuthService
   |
   v
AuthenticationManager
   |
   v
CustomUserDetailsService
   |
   v
UserRepository
   |
   v
Validate Password
   |
   v
Generate JWT
   |
   v
AuthResponse
Protected Request
Client
   |
   | Bearer JWT_TOKEN
   v
JwtAuthenticationFilter
   |
   v
Extract Token
   |
   v
Validate Token
   |
   v
Load User
   |
   v
Spring Security Context
   |
   v
Request Allowed
9. Important Security Components
SecurityConfig
→ Defines security rules


JwtService
→ Generates and validates JWT tokens


JwtAuthenticationFilter
→ Checks JWT in incoming requests


CustomUserDetailsService
→ Loads user details from the database


PasswordEncoder
→ Encrypts passwords


AuthenticationManager
→ Authenticates user credentials


UserRepository
→ Loads user data from MySQL
10. Important Concepts Explored
Authentication
Spring Security
JWT
Password Encryption
BCrypt
UserDetailsService
AuthenticationManager
AuthenticationProvider
SecurityFilterChain
JWT Filter
OncePerRequestFilter
Spring Data JPA
MySQL
Eureka Client
DTO
REST API
11. Reusable Knowledge

When creating an Auth Service for another Spring Boot project:

1. Add Web
2. Add Spring Security
3. Add JPA
4. Connect Database
5. Create User Entity
6. Create UserRepository
7. Create Register/Login DTOs
8. Configure PasswordEncoder
9. Create UserDetailsService
10. Create JwtService
11. Create JWT Filter
12. Configure Security
13. Create AuthService
14. Create AuthController
15. Test Register
16. Test Login
17. Test JWT Authentication

The following parts may change in another project:

User fields
Roles
Database
JWT claims
Token expiration
Public endpoints
Protected endpoints
Security rules
Development Status
Eureka Server  → DONE
API Gateway    → DONE
Auth Service   → IN PROGRESS
Department     → NEXT
Notes

This README is maintained as a developer reference for the Auth Service.

The focus is on:

What dependencies were used?
Why were they used?
Which annotations were used?
What does each application property do?
What is the authentication flow?
How does JWT work in this service?
What should be remembered for another project?
