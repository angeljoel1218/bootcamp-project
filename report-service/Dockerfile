FROM openjdk:11
ADD target/report-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7007
ENTRYPOINT ["java", "-jar", "app.jar"]