# Automated Urban Drone Delivery Landing Pad & Package Locker Network

A full-stack **Java 17 Spring Boot 3.x Maven** web application designed to manage autonomous urban drone deliveries, landing pads, and automated package lockers.

---

## 🚀 Key Features

* **Role-Based Security & Access Control**: Admin (`/admin/**`) & Customer (`/customer/**`) dashboards powered by Spring Security & BCrypt.
* **Autonomous Resource Allocation Engine**: `AssignmentService` automatically evaluates drone capacity, battery charge (>20%), landing pad availability, and locker reservation.
* **Live Step-by-Step Telemetry & Package Tracking**: Visual flight status pipeline (`REGISTERED` → `DRONE ASSIGNED` → `IN TRANSIT` → `ARRIVED` → `STORED IN LOCKER` → `COMPLETED`).
* **Complete Management Portals**:
  * **Packages**: Auto-generated tracking numbers (`UDN-2026-XXXX`), search & status filters.
  * **Drones**: Payload capacity, battery levels, flight status & maintenance overrides.
  * **Landing Pads**: Capacity and location monitoring.
  * **Lockers**: Real-time occupation & package association.

---

## 🛠️ Technology Stack

* **Java**: 17 / 21
* **Framework**: Spring Boot 3.3.4
* **Security**: Spring Security & BCrypt Password Encoder
* **Persistence**: Spring Data JPA & Hibernate
* **Database**: MySQL 8.0 (`urban_drone_db`) with H2 fallback for testing
* **Template Engine**: Thymeleaf + Thymeleaf Extras Spring Security 6
* **UI Styling**: Bootstrap 5 + FontAwesome 6 + Custom CSS
* **Build Tool**: Apache Maven (Wrapper included: `mvnw`, `mvnw.cmd`)

---

## 📂 Project Structure

```text
UrbanDroneDelivery
│
├── .idea
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.urbandrone.delivery
│   │   │       ├── config
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── DataInitializer.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller
│   │   │       │   ├── AdminController.java
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── CustomerController.java
│   │   │       │   ├── DeliveryController.java
│   │   │       │   ├── DroneController.java
│   │   │       │   ├── LandingPadController.java
│   │   │       │   ├── LockerController.java
│   │   │       │   ├── PackageController.java
│   │   │       │   └── TrackingController.java
│   │   │       ├── model
│   │   │       │   ├── Delivery.java
│   │   │       │   ├── DeliveryStatus.java
│   │   │       │   ├── Drone.java
│   │   │       │   ├── DroneStatus.java
│   │   │       │   ├── LandingPad.java
│   │   │       │   ├── LandingPadStatus.java
│   │   │       │   ├── Locker.java
│   │   │       │   ├── LockerStatus.java
│   │   │       │   ├── Notification.java
│   │   │       │   ├── Package.java
│   │   │       │   ├── PackageStatus.java
│   │   │       │   ├── User.java
│   │   │       │   └── UserRole.java
│   │   │       ├── repository
│   │   │       │   ├── DeliveryRepository.java
│   │   │       │   ├── DroneRepository.java
│   │   │       │   ├── LandingPadRepository.java
│   │   │       │   ├── LockerRepository.java
│   │   │       │   ├── NotificationRepository.java
│   │   │       │   ├── PackageRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service
│   │   │       │   ├── AssignmentService.java
│   │   │       │   ├── DeliveryService.java
│   │   │       │   ├── DroneService.java
│   │   │       │   ├── LandingPadService.java
│   │   │       │   ├── LockerService.java
│   │   │       │   ├── NotificationService.java
│   │   │       │   ├── PackageService.java
│   │   │       │   └── UserService.java
│   │   │       └── UrbanDroneDeliveryApplication.java
│   │   │
│   │   └── resources
│   │       ├── static
│   │       │   ├── css/style.css
│   │       │   ├── js/main.js
│   │       │   └── images
│   │       ├── templates
│   │       │   ├── admin
│   │       │   │   ├── dashboard.html
│   │       │   │   ├── deliveries.html
│   │       │   │   ├── drones.html
│   │       │   │   ├── landing-pads.html
│   │       │   │   ├── lockers.html
│   │       │   │   ├── packages.html
│   │       │   │   └── users.html
│   │       │   ├── customer
│   │       │   │   ├── dashboard.html
│   │       │   │   ├── packages.html
│   │       │   │   ├── profile.html
│   │       │   │   └── tracking.html
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   └── register.html
│   │       └── application.properties
│   │
│   └── test
│
├── target
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 🔑 Database & Configuration Setup

1. Open `src/main/resources/application.properties`.
2. Configure your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/urban_drone_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

3. Ensure MySQL server is running on `localhost:3306`.

---

## 🔑 Seeded Demo Credentials

On initial boot, `DataInitializer.java` automatically seeds sample users, packages, drones, landing pads, lockers, and deliveries:

* **Admin Portal**:
  * **Email**: `admin@urbandrone.com`
  * **Password**: `admin123`
* **Customer Portal**:
  * **Email**: `customer@gmail.com`
  * **Password**: `customer123`

---

## 🏃 Running the Application

### Via Maven Wrapper
```bash
./mvnw clean spring-boot:run
```
*(On Windows Command Prompt / PowerShell: `.\mvnw.cmd spring-boot:run`)*

### Access URL
Open your browser and navigate to:
```text
http://localhost:8080
```
