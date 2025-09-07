# Sonareclipse Application

**Sonareclipse** is a Spring Boot application integrated with PostgreSQL, Kafka, Zookeeper, and MinIO, running via Docker Compose. Swagger UI is included for API exploration and testing.

---

## Prerequisites

Before running the project, make sure you have the following installed:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

---

## Environment Variables

## Rename the `.env_example` file to `.env_docker`:

```bash
cp .env_example .env_docker
```

## Open the .env_docker file and populate it with the necessary configuration values for your environment.

Getting Started

To run the application along with all required services, use Docker Compose:

1. Navigate to the root directory of the project in your terminal.

2. Build the Docker images and start all containers in detached mode:
   
```bash
docker-compose up -d --build
```

This command will:

- Pull the required base images (PostgreSQL, Kafka, MinIO, etc.).

- Build the Docker image for your Spring Boot application.

- Create and start all services defined in docker-compose.yml.

## To check the status of the running containers:
   
```bash
docker ps
```

## API Documentation (Swagger)

Once the application container is running, you can access the API documentation through Swagger UI:

- URL: [http://localhost:8080/v1/swagger-ui/index.html](http://localhost:8080/v1/swagger-ui/index.html) 

Swagger UI allows you to explore and test all API endpoints interactively.

## MinIO Access

If you want to check MinIO objects:
- URL: [http://localhost:9001](http://localhost:9001)

---

### Stopping the Application

To stop and remove all containers, networks, and volumes created by Docker Compose:
```bash
docker-compose down
```
