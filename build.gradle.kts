plugins {
    id("java")
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
    implementation("net.dv8tion:JDA:5.0.0-beta.3") {
        exclude("org.jetbrains", "annotations")
        exclude("club.minnced", "opus-java")
        exclude("org.slf4j", "slf4j-api")
    }
    implementation("net.nonswag.core:core-api:2.1.3") {
        exclude("jakarta.xml.bind", "jakarta.xml.bind-api")
        exclude("com.sun.xml.bind", "jaxb-impl")
        exclude("com.zaxxer", "HikariCP")
    }
}

tasks.jar {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "net.nonswag.tnl.bot.Bot"
    }
}
