FROM ubuntu:latest
LABEL authors="ngtph"

#Stage 1: build
#Start with a maven image that includes JDK21
FROM maven:3.9.9-amazoncorretto-21 AS build

#Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

#Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
#Start with Amazon Correctto JDK21
FROM amazoncorretto:21.0.6 AS create-image

#Set working folder to App and copy compiled file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

#Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
