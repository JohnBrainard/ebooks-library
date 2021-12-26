package dev.johnbrainard.ebooks.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import dev.johnbrainard.ebooks.api.plugins.configureRouting
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.slf4j.LoggerFactory

inline fun <reified T : Any> logger() = LoggerFactory.getLogger(T::class.java)

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
		install(FreeMarker) {
			templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
		}
		configureRouting()
	}.start(wait = true)
}
