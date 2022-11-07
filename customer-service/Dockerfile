FROM openjdk:11
ADD target/customer-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7004
ENTRYPOINT ["java", "-jar", "app.jar"]