package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.files.FilesEbookMetaRepository
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

fun Application.configureRouting(collectionsService: CollectionsService) {

	routing {
		get("/") {
			call.respond(mapOf(
				"collections" to call.url {
					path("collections")
				}
			))
		}

		get("/collections") {
			val collectionsDto = collectionsService.listCollections(call)
			call.respond(collectionsDto)
		}

		get("/collections/{collection}") {
			val collectionId = call.parameters["collection"]
				?.let { collectionsService.toCollectionId(it) }
				?: throw IllegalStateException()

			val collection = collectionsService.getCollection(call, collectionId)
			call.respond(
				collection
			)
		}

		get("/collections/{collection}/{entry}") {
			val collectionId = call.parameters["collection"]
				?.let { collectionsService.toCollectionId(it) }
				?: throw IllegalStateException("this shouldn't even happen to a dog!")

			val ebookId = call.parameters["entry"]
				?.let { collectionsService.toEbookId(collectionId, it) }
				?: throw IllegalStateException("this shouldn't even happen to a dog!")

			val collectionEntry = collectionsService.getCollectionEntry(call, ebookId)
			call.respond(collectionEntry)
		}
	}
}

fun URLBuilder.pathToCollection(collectionId: EbookCollectionId) = path(
	"collections",
	encode(collectionId.name)
)

fun URLBuilder.pathToCollectionEntry(collectionId: EbookCollectionId, ebookId: EbookId) = path(
	"collections",
	encode(collectionId.name),
	encode(ebookId.name)
)