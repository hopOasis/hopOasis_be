FROM amazoncorretto:21-alpine-jdk AS build
RUN apk add --no-cache maven
COPY . .
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM amazoncorretto:21-alpine-jdk
COPY --from=build /target/hop_oasis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
