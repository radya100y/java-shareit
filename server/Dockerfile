FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareIt-server.jar
ENTRYPOINT ["java","-jar","/shareIt-server.jar"]