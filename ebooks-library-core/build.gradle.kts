val kotlin_version: String by project
val logback_version: String by project
val pdfbox_version: String by project

plugins {
    kotlin("jvm")
}

group = "dev.johnbrainard.ebooks"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.apache.pdfbox:pdfbox:$pdfbox_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
