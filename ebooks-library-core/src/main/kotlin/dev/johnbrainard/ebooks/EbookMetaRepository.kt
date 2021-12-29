package dev.johnbrainard.ebooks

import dev.johnbrainard.ebooks.db.Ebook

interface EbookMetaRepository {
	fun getMeta(ebookId: EbookId): EbookMeta
	fun listBooks(collectionId: EbookCollectionId): Collection<Ebook>

	fun saveBook(block: Builder.() -> Unit): Ebook

	class Builder {
		var collectionId: EbookCollectionId? = null
		var name: String? = null
		var path: String? = null
		var title: String? = null
		var authors: MutableList<String> = mutableListOf()
		var contents: MutableList<String> = mutableListOf()

		fun build(): Ebook {
			return Ebook(
				id = null,
				collectionId = requireNotNull(collectionId),
				name = requireNotNull(name),
				path = requireNotNull(path),
				title = requireNotNull(title),
				authors = authors.toSet(),
				contents = contents.toList()
			)
		}
	}

	fun search(title: String? = null): Collection<Ebook>
}
