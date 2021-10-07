package dev.johnbrainard.ebooks

interface EbookCollectionRepository {
	fun readCollectionId(id: String): EbookCollectionId
	fun readEbookId(collectionId: EbookCollectionId, id: String): EbookId
	fun listCollections(): Collection<EbookCollectionId>
	fun getCollection(collectionId: EbookCollectionId): EbookCollection
}
