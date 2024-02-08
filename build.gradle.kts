plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("com.google.guava:guava:33.0.0-jre")
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