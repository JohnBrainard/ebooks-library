pluginManagement {
    repositories {
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "ebooks-library"

include(
    "ebooks-library-api",
    "ebooks-library-core"
)
