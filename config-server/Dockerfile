FROM openjdk:11
ARG JAR_FILE=config-server/target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p /var/tmp/repos && chmod -R 777 /var/tmp/repos
ENTRYPOINT ["java","-jar","/app.jar"]