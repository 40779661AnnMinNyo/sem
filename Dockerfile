FROM openjdk:latest
COPY ./target/SemDev-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "SemDev-1.0-SNAPSHOT-jar-with-dependencies.jar"]