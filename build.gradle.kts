plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
}

group = "net.thenextlvl"
version = "4.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.compileJava {
    options.release.set(25)
}

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.0")
}

tasks.shadowJar {
    archiveFileName.set("discord-bot.jar")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "net.thenextlvl.bot.Bot"
}
