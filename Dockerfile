FROM amazoncorretto:21-alpine-jdk
COPY ./target/showvisitskeeper-0.0.1-SNAPSHOT.jar /visitkeeper-app/showvisitskeeper.jar
WORKDIR /visitkeeper/
EXPOSE 8080
#HOST PORT 16880
ENTRYPOINT ["java", "-Dspring.config.location=/visitkeeper/config/application.properties", "-jar","/visitkeeper-app/showvisitskeeper.jar"]