FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareIt-gateway.jar
ENTRYPOINT ["java","-jar","/shareIt-gateway.jar"]