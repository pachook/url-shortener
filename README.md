### Overview

A simple URL shortener service built with Java and Spring Boot.
This REST API allows clients to generate shortened URLs, with support for custom IDs and optional time-to-live (TTL) expiration for each shortened URL.

### Features

- Generate Short URLs: Create shortened URLs for any long URL.
- Custom IDs: Option to specify custom IDs for shortened URLs.
- TTL (Time-to-Live): Optionally specify an expiration time for each shortened URL.
- Redirection: Redirect to the original URL using the shortened URL.
- Deletion: Delete shortened URLs by ID.

### Technologies Used

- Java (17 or higher)
- Spring Boot
- Spring Data JPA
- H2 (in-memory database for testing; switchable to other databases)

### Cloning application

`git clone https://github.com/yourusername/url-shortener.git`

`cd url-shortener`

### Running the Application
`mvn clean install`

`mvn spring-boot:run`
It will start application on http://localhost:8080/api/url . 
### API Documentation
The API documentation is available at http://localhost:8080/swagger-ui/index.html.
