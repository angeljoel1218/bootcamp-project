FROM openjdk:11
ADD target/wallet-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7009
ENTRYPOINT ["java", "-jar", "app.jar"]