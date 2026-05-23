# Property Search

A Spring Boot MVC web application for searching Singapore private residential property transaction records.

## Prerequisites

- Java 21+
- Maven 3.9+
- Microsoft SQL Server (with the MasterTrnx dataset loaded)

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/minhkhoa6868/propertysearch.git
cd propertysearch
```

### 2. Configure database connection
Create `src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://YOUR_HOST:1433;databaseName=YOUR_DB
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and run
```bash
mvn clean install
mvn spring-boot:run
```

Open your browser at **http://localhost:8080**

## Features

- Search property transactions by street name, postal code, floor area, and contract date
- Sort results by any column
- Pagination with configurable page size
- Filter state preserved in URL (shareable and bookmarkable)
- Export CSV and PDF

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5, Spring Data JPA
- **Database:** Microsoft SQL Server
- **Frontend:** Thymeleaf, TomSelect
- **Build:** Maven