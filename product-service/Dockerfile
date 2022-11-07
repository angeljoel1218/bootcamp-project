FROM openjdk:11
ADD target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7003
ENTRYPOINT ["java", "-jar", "app.jar"]