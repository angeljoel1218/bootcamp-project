FROM openjdk:11
ADD target/account-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7005
ENTRYPOINT ["java", "-jar", "app.jar"]