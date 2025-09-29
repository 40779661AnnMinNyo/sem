FROM openjdk:latest
COPY ./target/alpha-0.1.0.3-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar",  "alpha-0.1.0.3-jar-with-dependencies.jar"]