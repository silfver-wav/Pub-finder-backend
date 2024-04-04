# Pub finder
Backend for pub finder.
## Requirements
* Java 17
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html)
* [Docker](https://www.docker.com/get-started)
## Configuration
Before running the project, you need to configure the application settings. Follow these steps:

1. Create a file named secret.yml.

2. Place the secret.yml file in the resources folder of your project.

3. Open the secret.yml file and specify the following configuration:
```yaml
security:
  jwt-secret: <your-jwt-secret>
  jwt-expiration-ms: <jwt-token-expiration-time-in-milliseconds>
  jwt-refresh-expiration-ms: <jwt-refresh-token-expiration-time-in-milliseconds>
```
Replace `<your-api-key>`, `<your-jwt-secret>`, `<jwt-token-expiration-time-in-milliseconds>`, and `<jwt-refresh-token-expiration-ms>` with your desired values.
### How to Run Project
Start the PostgreSQL database Docker container:
```shell
  docker-compose up
```
Build and run the Spring Boot application:
```shell
  ./gradlew bootRun
```
