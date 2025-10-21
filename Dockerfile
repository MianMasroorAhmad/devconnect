# Multi-stage Dockerfile for DevConnect

# Build stage: use slim Maven image to reduce installed packages
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src src
# Use a reproducible, non-interactive build; skip tests in image build
RUN mvn -B -DskipTests package

# Runtime stage: use a Debian-based Temurin JRE (smaller attack surface vs older alpine variants)
FROM eclipse-temurin:21-jre-jammy

# Create a non-root user to run the app
RUN groupadd -r app && useradd -r -g app app

WORKDIR /app
COPY --from=build /workspace/target/devconnect-0.0.1-SNAPSHOT.jar app.jar

# Give ownership to the non-root user and switch
RUN chown -R app:app /app
USER app

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
