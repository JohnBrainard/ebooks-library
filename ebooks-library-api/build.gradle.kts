val hikari_version: String by project
val koin_version: String by project
val kotlin_version: String by project
val kotlinx_cli_version: String by project
val ktor_version: String by project
val liquibase_version: String by project
val logback_version: String by project
val postgres_version: String by project

plugins {
	application
	kotlin("jvm")
	kotlin("plugin.serialization") version "1.6.10"
}

group = "dev.johnbrainard.ebooks"
version = "0.0.1"

application {
	mainClass.set("dev.johnbrainard.ebooks.api.ApplicationKt")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("ch.qos.logback:logback-classic:$logback_version")
	implementation("com.zaxxer:HikariCP:$hikari_version")
	implementation("io.insert-koin:koin-ktor:$koin_version")
	implementation("io.ktor:ktor-freemarker:$ktor_version")
	implementation("io.ktor:ktor-jackson:$ktor_version")
	implementation("io.ktor:ktor-server-core:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("io.ktor:ktor-utils:$ktor_version")
	implementation("org.jetbrains.kotlinx:kotlinx-cli:$kotlinx_cli_version")
	implementation("org.liquibase:liquibase-core:$liquibase_version")

	implementation(project(":ebooks-library-core"))

	runtimeOnly("org.postgresql:postgresql:$postgres_version")
}

application {
	mainClass.set("dev.johnbrainard.ebooks.api.ApplicationKt")
}

distributions {
	main {
		contents {
			from("$buildDir/libs") {
				rename("${rootProject.name}-jvm", rootProject.name)
				into("lib")
			}
			duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		}
	}
}
