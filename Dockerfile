FROM gradle:jdk25-alpine AS build

WORKDIR /gradle

COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts /gradle/
COPY --chown=gradle:gradle src /gradle/src

RUN gradle shadowJar

FROM openjdk:25-slim

WORKDIR /app

COPY --from=build /gradle/build/libs/discord-bot.jar /app/discord-bot.jar
CMD ["java", "-Xmx50M", "-jar", "/app/discord-bot.jar"]