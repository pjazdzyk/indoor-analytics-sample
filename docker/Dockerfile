# Use Temurin JDK as base image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY core/target/*.jar /app/indoor-analytics.jar

# Expose the port that your Spring Boot application uses (default is 8090)
EXPOSE 8090

# Define the entry point for the Docker container
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "indoor-analytics.jar", "com.synerset.indooranalyticsapi.IndoorAnalyticsApplication"]