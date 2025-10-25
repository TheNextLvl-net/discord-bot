FROM gradle:9.1-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM openjdk:17-slim

RUN mkdir /app
WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/discord-bot.jar
ENTRYPOINT ["java", "-Xmx50M", "-jar", "/app/discord-bot.jar"]
