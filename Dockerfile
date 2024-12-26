# Use the official OpenJDK 17 image as the base image for building the application
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Maven pom.xml and install dependencies
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use a smaller image for running the application
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/fastchat-backend-0.0.1-SNAPSHOT.jar /app/fastchat-backend.jar

# Set the environment variable for the Spring profile
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/fastchat-backend.jar"]
