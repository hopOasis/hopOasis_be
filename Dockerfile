FROM maven:3.9.5-eclipse-temurin-21 AS builder
ARG JAR_FILE=target/*.jar
COPY ./target/hop_oasis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]