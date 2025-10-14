# Stage 1: Build the application
FROM gradle:8.7-jdk17 AS build
COPY --chown=gradle:gradle . /project/smart
WORKDIR /project/smart

#skip task: test
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the application
FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /project/smart/build/libs/*.jar /project/spring-boot-project.jar
ENTRYPOINT ["java", "-jar", "/project/spring-boot-project.jar"]
