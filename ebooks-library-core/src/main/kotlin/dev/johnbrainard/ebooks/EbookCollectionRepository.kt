package dev.johnbrainard.ebooks

interface EbookCollectionRepository {
	fun listCollections(): Collection<EbookCollection>
	fun getCollection(collectionId: EbookCollectionId): EbookCollection

	fun saveCollection(block: Builder.() -> Unit): EbookCollection

	class Builder {
		var name: String? = null
		var path: String? = null

		fun build(): EbookCollection {
			return EbookCollection(
				id = null,
				name = requireNotNull(name) { "name is null" },
				path = requireNotNull(path) { "path is null" }
			)
		}
	}
}
