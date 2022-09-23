FROM amazoncorretto:11-alpine-jdk
COPY /target/alpha.jar alpha.jar
ENTRYPOINT ["java","-jar","/alpha.jar"]