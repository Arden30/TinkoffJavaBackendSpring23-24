FROM openjdk:21
WORKDIR /app
COPY ./target/scrapper.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
