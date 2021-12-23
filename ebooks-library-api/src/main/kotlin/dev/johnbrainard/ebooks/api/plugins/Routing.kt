package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

	val collectionsService: CollectionsService by inject()

	routing {
		get("/") {
			val indexHtml = this::class.java.classLoader.getResource("index.html")!!.readText()
			call.respondText(
				indexHtml,
				ContentType.Text.Html
			)
		}

		static("/") {
			resources("")
		}

		get("/collections") {
			val collectionsDto = collectionsService.listCollections(call)
			call.respond(collectionsDto)
		}

		get("/collections/{collection}") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw java.lang.IllegalStateException()

			val collection = collectionsService.getCollection(call, collectionId)
			call.respond(collection)
		}
	}
}
