plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo1.maven.org/maven2/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("com.google.guava:guava:33.0.0-jre")
    compileOnly("net.minestom:minestom-snapshots:6758737b80")
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