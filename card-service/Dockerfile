FROM openjdk:11
ADD target/card-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7008
ENTRYPOINT ["java", "-jar", "app.jar"]