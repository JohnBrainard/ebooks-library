package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookListId
import dev.johnbrainard.ebooks.api.logger
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.getProperty
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

	val collectionsService: CollectionsService by inject()
	val logger = logger()

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

		post("/collection/{collection}/index") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw IllegalStateException()

			collectionsService.reindexCollection(collectionId)

			call.respondRedirect("/collection/$collectionId")
		}

		get("/collection/{collection}/entries/{entry}") {
			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw IllegalStateException()

			val entryId = call.parameters["entry"]
				?.let { EbookId(it) }
				?: throw IllegalStateException()

			val entry = collectionsService.getEntry(call, entryId)
			val lists = collectionsService.getListsForEntry(entryId)
			val allLists = collectionsService.getLists()
				.lists
				.filter { list -> !lists.any { it.id == list.id } }

			call.respond(
				FreeMarkerContent(
					"entry.ftl",
					mapOf(
						"entry" to entry,
						"listsContainingBook" to lists,
						"lists" to allLists
					)
				)
			)
		}

		post("/collection/{collection}/entries/{entry}/lists") {
			val form = call.receiveParameters()
			val listId = form["list"]
				?.let { EbookListId(it) }
				?: throw IllegalStateException("list is not supplied")

			val collectionId = call.parameters["collection"]
				?.let { EbookCollectionId(it) }
				?: throw java.lang.IllegalStateException()

			val bookId = call.parameters["entry"]
				?.let { EbookId(it) }
				?: throw IllegalStateException()

			collectionsService.addBookToList(bookId, listId)

			call.respondRedirect("/collection/$collectionId/entries/$bookId", permanent = true)
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

		post("/lists") {
			val form = call.receiveParameters()

			val listName = form["name"]
				?.trim()
				?: throw IllegalStateException()

			logger.info("creating new list: $listName")
			collectionsService.createList(listName)

			call.respondRedirect("/lists", permanent = true)
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
