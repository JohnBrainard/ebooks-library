package dev.johnbrainard.ebooks.api.plugins

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.files.FilesEbookCollectionId
import io.ktor.application.*
import io.ktor.util.*


interface CollectionsService {
	fun listCollections(call: ApplicationCall): CollectionsDto
	fun getCollection(call: ApplicationCall, collectionId: EbookCollectionId): CollectionDto
	fun getCollectionEntry(call: ApplicationCall, ebookId: EbookId): CollectionEntryDto
	fun toCollectionId(value: String): EbookCollectionId
	fun toEbookId(collectionId: EbookCollectionId, value: String): EbookId
}

class DefaultCollectionsService(
	private val ebookCollectionRepository: EbookCollectionRepository,
	private val metaRepository: EbookMetaRepository
) : CollectionsService {

	override fun toCollectionId(value: String): EbookCollectionId =
		ebookCollectionRepository.readCollectionId(
			decode(value)
		)

	override fun toEbookId(collectionId: EbookCollectionId, value: String): EbookId =
		ebookCollectionRepository.readEbookId(collectionId, decode(value))

	override fun listCollections(call: ApplicationCall): CollectionsDto {
		val collections = ebookCollectionRepository.listCollections()
			.map { it as FilesEbookCollectionId }
			.map {
				CollectionSummaryDto(
					name = it.name,
					path = it.path.toString(),
					url = call.url {
						path("collections", encode(it.name))
					}
				)
			}

		return CollectionsDto(collections)
	}

	override fun getCollection(call: ApplicationCall, collectionId: EbookCollectionId): CollectionDto {
		val collection = ebookCollectionRepository.getCollection(collectionId)
			.let {
				CollectionDto(
					name = it.name,
					url = call.url {
						pathToCollection(collectionId)
					},
					entries = it.ebooks.map {
						CollectionEntryDto(
							name = it.name,
							url = call.url {
								pathToCollectionEntry(collectionId, it)
							}
						)
					}
				)
			}

		return collection
	}

	override fun getCollectionEntry(call: ApplicationCall, ebookId: EbookId): CollectionEntryDto {
		val ebookMeta = metaRepository.getMeta(ebookId)

		return CollectionEntryDto(
			ebookId.name,
			call.url()
		).apply {
			meta = MetaDto(
				title = ebookMeta.title,
				authors = ebookMeta.authors
			)
		}
	}
}
