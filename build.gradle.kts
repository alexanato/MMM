plugins {
    java
    id("com.gradleup.shadow") version "9.4.2"
}

// DAS HIER ERZWINGT JAVA 26 FÜR ALLES (Kompilieren & Ausführen):
java {

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25)) // Auf 25 stellen, passend zu deiner IDE
    }
}

group = "at.randorf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("net.minestom:minestom:2026.06.02-26.1.2")
}

tasks.withType<JavaCompile>().configureEach {
    // options.release wird durch die Toolchain oben automatisch mitverwaltet,
    // du kannst zur Sicherheit aber das Encoding drin lassen:
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveBaseName.set("MinestomServer")
    archiveClassifier.set("")
    archiveVersion.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "at.randorf.Main" // Angepasst an dein Paket aus dem Fehler log
        )
    }
}