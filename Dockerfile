# syntax=docker/dockerfile:1.2

FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/*.jar app.jar
EXPOSE 8080
# Comment out below, and replace with manual ENVs if your system doesn't support Docker secrets
RUN --mount=type=secret,id=fracktail_secrets,dst=/application.yml
ENTRYPOINT ["java","-jar","/app.jar"]