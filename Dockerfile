FROM maven:3.9.7-sapmachine-21 as build
# Copy Maven files for dependency resolution
COPY pom.xml ./
COPY .mvn .mvn

# Copy application source code
COPY src src

# Build the project and create the executable JAR
RUN mvn clean install -DskipTests

# Stage 2: Run stage
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR app

# Copy the JAR file from the build stage
COPY --from=build target/*.jar /app/task-management-system-3.3.2.jar

ENTRYPOINT ["java", "-jar", "task-management-system-3.3.2.jar", "--spring.profiles.active=dev"]
