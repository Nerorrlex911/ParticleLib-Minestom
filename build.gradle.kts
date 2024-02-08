plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    // lss的库
    maven("https://lss233.littleservice.cn/repositories/minecraft")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("dev.hollowcube:minestom-ce:47dfb29c20")
    compileOnly("dev.hollowcube:minestom-ce-extensions:1.2.0")
}

group = "top.zoyn.particlelib"
version = "1.5.1"
description = "ParticleLib"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}


tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}