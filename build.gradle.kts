plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

group = "net.thenextlvl"
version = "4.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.0")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "net.thenextlvl.bot.Bot"
}
