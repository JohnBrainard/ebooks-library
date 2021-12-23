package dev.johnbrainard.ebooks.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import dev.johnbrainard.ebooks.api.plugins.configureRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.koin.environmentProperties

fun main() {
	startKoin {
		environmentProperties()
		modules(applicationModule)
	}

	embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
		install(ContentNegotiation) {
			jackson {
				enable(SerializationFeature.INDENT_OUTPUT)
				setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
				dateFormat = StdDateFormat()
				propertyNamingStrategy = PropertyNamingStrategies.SnakeCaseStrategy()
			}
		}
		configureRouting()
	}.start(wait = true)
}
