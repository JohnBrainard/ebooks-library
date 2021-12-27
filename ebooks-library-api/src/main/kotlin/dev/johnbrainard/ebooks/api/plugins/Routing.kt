package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.getProperty
import org.koin.ktor.ext.inject
import java.lang.IllegalStateException

fun Application.configureRouting() {

	val collectionsService: CollectionsService by inject()

	routing {
		get("/") {
			val collections = collectionsService.listCollections(call)

			val indexHtml = this::class.java.classLoader.getResource("index.html")!!.readText()

			call.respond(
				FreeMarkerContent(
					"index.ftl",
					mapOf("collections" to collections.collections)
				)
			)
		}

		get("/collection/{collection}") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw IllegalStateException()

			val collection = collectionsService.getCollection(call, collectionId)

			call.respond(
				FreeMarkerContent(
					"collection.ftl",
					mapOf("collection" to collection)
				)
			)
		}

		get("/api/search") {
			val title = call.parameters["title"]

			val resultsDto = collectionsService.search(call, title = title)
			call.respond(resultsDto)
		}

		static("/") {
			resources("")
		}

		static("/download") {
			files(getProperty<String>("LIBRARY_PATH")!!)
		}

		get("/api/collections") {
			val collectionsDto = collectionsService.listCollections(call)
			call.respond(collectionsDto)
		}

		get("/api/collections/{collection}") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw IllegalStateException()

			val collection = collectionsService.getCollection(call, collectionId)
			call.respond(collection)
		}
	}
}
