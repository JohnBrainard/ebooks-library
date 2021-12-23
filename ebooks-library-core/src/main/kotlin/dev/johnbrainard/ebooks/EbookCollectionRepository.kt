package dev.johnbrainard.ebooks

interface EbookCollectionRepository {
	fun listCollections(): Collection<EbookCollection>
	fun getCollection(collectionId: EbookCollectionId): EbookCollection
}
