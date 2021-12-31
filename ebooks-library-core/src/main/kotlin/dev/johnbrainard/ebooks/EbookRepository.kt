package dev.johnbrainard.ebooks

interface EbookRepository {
	fun getMeta(ebookId: EbookId): Ebook
	fun listBooks(collectionId: EbookCollectionId): Collection<Ebook>

	fun saveBook(block: Builder.() -> Unit): Ebook

	class Builder {
		var collectionId: EbookCollectionId? = null
		var name: String? = null
		var path: String? = null
		var title: String? = null
		var pageCount: Int? = null
		var authors: MutableList<String> = mutableListOf()
		var contents: MutableList<String> = mutableListOf()

		fun build(): Ebook {
			return Ebook(
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

	fun search(title: String? = null): Collection<Ebook>
}
