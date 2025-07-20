FROM openjdk:17-jdk-slim
LABEL authors="Prateek Arya"
EXPOSE 9000
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","/accounts-0.0.1-SNAPSHOT.jar"]