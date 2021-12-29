pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    plugins {
        kotlin("jvm") version "1.6.10"
    }
}

rootProject.name = "ebooks-library"

include(
    "ebooks-library-api",
    "ebooks-library-core",
    "tools:meta-extractor"
)
