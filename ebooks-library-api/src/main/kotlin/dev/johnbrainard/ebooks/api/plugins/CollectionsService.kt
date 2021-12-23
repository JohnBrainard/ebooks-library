package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import io.ktor.application.*


interface CollectionsService {
	fun listCollections(call: ApplicationCall): CollectionsDto
	fun getCollection(call: ApplicationCall, collectionId: EbookCollectionId): CollectionDto
}
