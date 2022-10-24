FROM openjdk:11
ARG JAR_FILE=discovery-server/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]