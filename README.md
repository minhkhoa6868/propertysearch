# Property Search

A Spring Boot MVC web application for searching Singapore private residential property transaction records.

## Prerequisites

- Java 21+
- Maven 3.9+
- Microsoft SQL Server (with the MasterTrnx dataset loaded)

## Environment Setup

### Java
1. Download and install Java 21 from [Oracle JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
2. Set `JAVA_HOME` environment variable to your JDK installation path
3. Verify installation:
```bash
java -version
```
Expected output: `java version "21.x.x"`

### Maven
1. Download Maven from [apache.org](https://maven.apache.org/download.cgi)
2. Verify installation:
```bash
mvn -version
```

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/minhkhoa6868/propertysearch.git
cd propertysearch
```

### 2. Configure database connection
Create `src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://<your_server>:1433;databaseName=<your_database_name>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
```

### 3. Build and run
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Run unit test
```bash
mvn test
```

Open your browser at **http://localhost:8080**

## Features

- Search property transactions by street name, postal code, floor area, and contract date
- Sort results by any column
- Pagination with configurable page size
- Filter state preserved in URL (shareable and bookmarkable)
- Export selected rows to CSV and PDF

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5, Spring Data JPA
- **Database:** Microsoft SQL Server
- **Frontend:** Thymeleaf, CSS, Javascript, TomSelect, jsPDF AutoTable
- **Build:** Maven