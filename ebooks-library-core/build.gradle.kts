val jackson_version:String by project
val kotlin_version: String by project
val logback_version: String by project
val pdfbox_version: String by project

plugins {
    kotlin("jvm")
    idea
}

group = "dev.johnbrainard.ebooks"
version = "0.0.1"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.apache.pdfbox:pdfbox:$pdfbox_version")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")


    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
