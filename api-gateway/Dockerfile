FROM openjdk:11
ADD target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "app.jar"]