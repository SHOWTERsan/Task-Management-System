FROM openjdk:21
WORKDIR /app
COPY . /app
RUN chmod +x /app/mvnw
RUN ./mvnw package -DskipTests
ENTRYPOINT ["java", "-jar", "./target/task-management-system-3.3.2.jar", "--spring.profiles.active=dev"]
