val hikari_version: String by project
val koin_version: String by project
val kotlin_version: String by project
val kotlinx_cli_version: String by project
val ktor_version: String by project
val logback_version: String by project
val postgres_version: String by project

plugins {
	application
	kotlin("jvm")
}

group = "dev.johnbrainard.ebooks"
version = "0.0.1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("ch.qos.logback:logback-classic:$logback_version")
	implementation("io.insert-koin:koin-ktor:$koin_version")
	implementation("org.jetbrains.kotlinx:kotlinx-cli:$kotlinx_cli_version")

	implementation(project(":ebooks-library-core"))

	testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
