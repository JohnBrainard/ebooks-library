val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project

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
	implementation("io.ktor:ktor-server-core:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("io.ktor:ktor-utils:$ktor_version")
	implementation("io.ktor:ktor-jackson:$ktor_version")
	implementation(project(":ebooks-library-core"))
	implementation("ch.qos.logback:logback-classic:$logback_version")
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
		}
	}
}
