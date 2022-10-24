FROM openjdk:11
ARG JAR_FILE=api-gateway/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]