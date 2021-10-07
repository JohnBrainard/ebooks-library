package dev.johnbrainard.ebooks.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import dev.johnbrainard.ebooks.api.plugins.DefaultCollectionsService
import dev.johnbrainard.ebooks.api.plugins.configureRouting
import dev.johnbrainard.ebooks.files.FilesEbookCollectionRepository
import dev.johnbrainard.ebooks.files.FilesEbookMetaRepository
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.nio.file.Path

fun main() {
	val libraryPath = System.getenv("LIBRARY_PATH")

	val collectionsService = DefaultCollectionsService(
		ebookCollectionRepository = FilesEbookCollectionRepository(
			Path.of(libraryPath)
		),
		metaRepository = FilesEbookMetaRepository(
			pdfMetaExtractor = PdfBoxMetaExtractor()
		)
	)

	embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
		install(ContentNegotiation) {
			jackson {
				enable(SerializationFeature.INDENT_OUTPUT)
				setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
				dateFormat = StdDateFormat()
				propertyNamingStrategy = PropertyNamingStrategies.SnakeCaseStrategy()
			}
		}
		configureRouting(collectionsService)
	}.start(wait = true)
}
