FROM adoptopenjdk/openjdk11:ubi as build
COPY ./target/backend-coding-challenge-1.0-SNAPSHOT.jar /app.jar
WORKDIR /
CMD ["java", "-jar", "app.jar"]