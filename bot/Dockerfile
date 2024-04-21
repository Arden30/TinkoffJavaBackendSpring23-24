FROM openjdk:21
WORKDIR /app
COPY ./target/bot.jar /app
ENV TELEGRAM_API_KEY=${TELEGRAM_API_KEY}
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "-Dapp.telegram-token=$TELEGRAM_API_KEY", "bot.jar"]
