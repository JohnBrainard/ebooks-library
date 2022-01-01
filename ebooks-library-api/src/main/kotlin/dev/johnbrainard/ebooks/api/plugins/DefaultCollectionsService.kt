package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.*
import dev.johnbrainard.ebooks.api.IndexerScheduler
import dev.johnbrainard.ebooks.index.Indexer
import io.ktor.application.*
import io.ktor.util.*

class DefaultCollectionsService(
	private val ebookCollectionRepository: EbookCollectionRepository,
	private val metaRepository: EbookRepository,
	private val listRepository: EbookListRepository,
	private val indexerScheduler: IndexerScheduler
) : CollectionsService {

	override fun listCollections(call: ApplicationCall): CollectionsDto {
		val collections = ebookCollectionRepository.listCollections()
			.map {
				CollectionSummaryDto(
					id = it.id.toString(),
					name = it.name,
					path = it.path,
					url = call.url {
						path("collections", it.id.toString())
					}
				)
			}

		return CollectionsDto(collections)
	}

	override fun getCollection(call: ApplicationCall, collectionId: EbookCollectionId): CollectionDto {
		val collection = ebookCollectionRepository.getCollection(collectionId)
		val entries = metaRepository.listBooks(collectionId)

		return CollectionDto(
			id = collection.id.toString(),
			name = collection.name,
			url = call.url {
				path("/collection/${collection.id}")
			},
			entries = entries.map { ebook ->
				ebook.toCollectionEntryDto(call, ebookCollectionRepository.getCollection(ebook.collectionId))
			}
		)
	}

	override fun reindexCollection(collectionId: EbookCollectionId) {
		val collection = ebookCollectionRepository.getCollection(collectionId)
		indexerScheduler.scheduleCollection(collection)
	}

	override fun getLists(): ListsDto {
		val lists = listRepository.getLists()

		return ListsDto(
			lists = lists.map {
				ListDto(
					id = it.id.toString(),
					name = it.name,
					entryCount = it.entries.size
				)
			}
		)
	}

	override fun getList(call: ApplicationCall, listId: EbookListId): ListDto {
		val list = listRepository.getList(listId)
		val entries = list.entries.map {
			metaRepository.getMeta(it.bookId)
		}.map {
			it.toCollectionEntryDto(call, ebookCollectionRepository.getCollection(it.collectionId))
		}

		return ListDto(
			id = list.id.toString(),
			name = list.name,
			entryCount = list.entries.size,
			entries = entries
		)
	}

	override fun createList(name: String) {
		listRepository.saveList(EbookList(name = name))
	}

	override fun addBookToList(bookId: EbookId, listId: EbookListId) {
		listRepository.addBookToList(listId, bookId)
	}

	override fun search(call: ApplicationCall, title: String?): SearchResultsDto {
		val titleFilter = title
			?.split(' ')
			?.joinToString(" & ")

		val results = metaRepository.search(title = titleFilter)
			.map { it.toCollectionEntryDto(call, ebookCollectionRepository.getCollection(it.collectionId)) }

		return SearchResultsDto(
			results = results
		)
	}

	override fun getEntry(call: ApplicationCall, entryId: EbookId): CollectionEntryDto {
		val entry = metaRepository.getMeta(entryId)
		val collection = ebookCollectionRepository.getCollection(entry.collectionId)

		return entry.toCollectionEntryDto(call, collection)
	}

	override fun getListsForEntry(entryId: EbookId): List<ListDto> {
		return listRepository.findListsContainingBook(entryId)
			.map {
				ListDto(
					id = it.id.toString(),
					name = it.name,
					entryCount = 0
				)
			}
	}
}

fun Ebook.toCollectionEntryDto(call: ApplicationCall, collection: EbookCollection): CollectionEntryDto =
	CollectionEntryDto(
		id = id.toString(),
		name = name,
		collectionId = collection.id.toString(),
		collectionName = collection.name,
		path = path,
		title = title,
		authors = authors,
		pageCount = pageCount,
		contents = contents,
		url = call.url {
			path("/collections/${collectionId}/${id}")
		},
		collectionUrl = call.url {
			path("/collection/${collectionId}")
		},
		downloadUrl = call.url {
			path("/download/${collection.path}/${path}")
		}
	)
