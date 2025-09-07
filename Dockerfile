FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
RUN mvn clean package -DskipTests

# 2️⃣ Run stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/apps-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
