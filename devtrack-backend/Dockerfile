# -------- Build stage --------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# copy dependency files first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# copy source code
COPY src ./src

# build jar (skip tests for speed in docker builds)
RUN mvn clean package -DskipTests

# -------- Runtime stage --------
FROM eclipse-temurin:17-jre

WORKDIR /app

# run as non-root (better practice)
RUN useradd -m appuser
USER appuser

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# expose Spring Boot port
EXPOSE 8080

# JVM tuning for containers
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
