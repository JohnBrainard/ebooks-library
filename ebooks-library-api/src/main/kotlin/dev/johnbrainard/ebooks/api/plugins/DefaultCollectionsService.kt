package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.*
import io.ktor.application.*
import io.ktor.util.*

class DefaultCollectionsService(
	private val ebookCollectionRepository: EbookCollectionRepository,
	private val metaRepository: EbookMetaRepository
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
			name = collection.name,
			url = call.url {
				path("/collection/${collection.id}")
			},
			entries = entries.map { ebook ->
				ebook.toCollectionEntryDto(call, ebookCollectionRepository.getCollection(ebook.collectionId))
			}
		)
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
}

fun EbookMeta.toCollectionEntryDto(call: ApplicationCall, collection: EbookCollection): CollectionEntryDto =
	CollectionEntryDto(
		name = name,
		collectionName = collection.name,
		path = path,
		title = title,
		authors = authors,
		pageCount = pageCount,
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
