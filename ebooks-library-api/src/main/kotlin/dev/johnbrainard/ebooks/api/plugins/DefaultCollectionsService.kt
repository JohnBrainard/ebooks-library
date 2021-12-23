package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
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
				CollectionEntryDto(
					name = ebook.name,
					url = call.url {
						path("/collections/${collection.id}/${ebook.id}")
					}
				)
			}
		)
	}
}
