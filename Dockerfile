

FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /usr/src/app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /usr/src/app/target/hop_oasis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

