package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookListId
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
		static("/") {
			resources("")
		}

		static("/download") {
			files(getProperty<String>("LIBRARY_PATH")!!)
		}

		get("/") {
			val collections = collectionsService.listCollections(call)

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

		get("/collection/{collection}/entries/{entry}") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw IllegalStateException()

			val entryId = call.parameters["entry"]
				?.let { EbookId(it) }
				?: throw IllegalStateException()

			val entry = collectionsService.getEntry(call, entryId)

			call.respond(
				FreeMarkerContent(
					"entry.ftl",
					mapOf("entry" to entry)
				)
			)
		}

		get("/lists") {
			val listsDto = collectionsService.getLists()

			call.respond(
				FreeMarkerContent(
					"lists.ftl",
					mapOf("lists" to listsDto)
				)
			)
		}

		get("/lists/{list}") {
			val listId = call.parameters["list"]
				?.let { EbookListId(it) }
				?: throw IllegalStateException()

			val list = collectionsService.getList(call, listId)

			call.respond(
				FreeMarkerContent(
					"list.ftl",
					mapOf(
						"list" to list
					)
				)
			)
		}

		get("/search") {
			val title = call.parameters["title"]

			val resultsDto = collectionsService.search(call, title = title)
			call.respond(
				FreeMarkerContent(
					"search.ftl",
					mapOf(
						"results" to resultsDto,
						"query" to "$title"
					)
				)
			)
		}

		get("/api/search") {
			val title = call.parameters["title"]

			val resultsDto = collectionsService.search(call, title = title)
			call.respond(resultsDto)
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
