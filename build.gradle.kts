plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.nonswag.tnl.discord-bot"
version = "3.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.thenextlvl.net/releases")
    }
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.4") {
        exclude("org.jetbrains", "annotations")
        exclude("club.minnced", "opus-java")
        exclude("org.slf4j", "slf4j-api")
    }
    implementation("net.nonswag.core:core-api:2.1.3") {
        exclude("jakarta.xml.bind", "jakarta.xml.bind-api")
        exclude("com.sun.xml.bind", "jaxb-impl")
        exclude("com.zaxxer", "HikariCP")
    }
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.jar {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "net.nonswag.tnl.bot.Bot"
    }
}

tasks {
    jar {
        dependsOn(shadowJar)
    }
}
