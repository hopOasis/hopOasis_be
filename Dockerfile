#FROM maven:3.9.5-eclipse-temurin-21 AS builder
#ARG JAR_FILE=target/*.jar
#COPY ./target/hop_oasis-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
# FROM debian:stable AS glibc
# RUN apt-get update && apt-get install -y --no-install-recommends \
#     libc6 \
#     && rm -rf /var/lib/apt/lists/*

FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /usr/src/app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /usr/src/app/target/hop_oasis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

