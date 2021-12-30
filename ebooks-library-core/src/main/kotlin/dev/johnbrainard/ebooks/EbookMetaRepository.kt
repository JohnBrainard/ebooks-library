package dev.johnbrainard.ebooks

interface EbookMetaRepository {
	fun getMeta(ebookId: EbookId): EbookMeta
	fun listBooks(collectionId: EbookCollectionId): Collection<EbookMeta>

	fun saveBook(block: Builder.() -> Unit): EbookMeta

	class Builder {
		var collectionId: EbookCollectionId? = null
		var name: String? = null
		var path: String? = null
		var title: String? = null
		var pageCount: Int? = null
		var authors: MutableList<String> = mutableListOf()
		var contents: MutableList<String> = mutableListOf()

		fun build(): EbookMeta {
			return EbookMeta(
				id = null,
				collectionId = requireNotNull(collectionId),
				name = requireNotNull(name),
				path = requireNotNull(path),
				title = requireNotNull(title),
				pageCount = requireNotNull(pageCount),
				authors = authors.toSet(),
				contents = contents.toList()
			)
		}
	}

	fun search(title: String? = null): Collection<EbookMeta>
}
