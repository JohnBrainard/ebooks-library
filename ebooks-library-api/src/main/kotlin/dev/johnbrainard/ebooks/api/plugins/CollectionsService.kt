package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookListId
import io.ktor.application.*


interface CollectionsService {
	fun listCollections(call: ApplicationCall): CollectionsDto
	fun getCollection(call: ApplicationCall, collectionId: EbookCollectionId): CollectionDto
	fun search(call: ApplicationCall, title: String? = null): SearchResultsDto
	fun getEntry(call: ApplicationCall, entryId: EbookId): CollectionEntryDto

	fun getLists(): ListsDto
	fun getList(call: ApplicationCall, listId: EbookListId): ListDto
	fun createList(name: String)
}
