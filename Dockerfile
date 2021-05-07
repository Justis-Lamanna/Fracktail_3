# syntax=docker/dockerfile:1.2

FROM openjdk:8-jdk-alpine
COPY target/*.jar app.jar
EXPOSE 8080
RUN --mount=type=secret,id=fracktail_secrets,target=/application.yml
ENTRYPOINT ["java","-jar","/app.jar"]