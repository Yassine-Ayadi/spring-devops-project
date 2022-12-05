FROM maven:3.8.4-jdk-11-slim as builder

RUN mkdir -p /home/project

COPY . /home/Devops_Project

WORKDIR /home/Devops_Project

RUN mvn install -Dmaven.test.skip=true

# For Java 11,

FROM adoptopenjdk/openjdk11:alpine-jre as runtime

COPY --from=builder /home/Devops_Project/target/*.jar /home/Devops_Project/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/project/app.jar"]
