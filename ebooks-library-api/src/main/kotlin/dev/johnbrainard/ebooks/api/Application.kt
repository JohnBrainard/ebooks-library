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
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.slf4j.LoggerFactory

inline fun <reified T : Any> logger() = LoggerFactory.getLogger(T::class.java)

fun main(args: Array<String>) {
	val argParser = ArgParser("ebooks-library")

	val port by argParser.option(ArgType.Int, fullName = "port", shortName = "p", description = "Server Port")
		.default(8080)

	argParser.parse(args)

	startKoin {
		environmentProperties()
		modules(applicationModule)
	}

	embeddedServer(Netty, port = port, host = "0.0.0.0") {
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
