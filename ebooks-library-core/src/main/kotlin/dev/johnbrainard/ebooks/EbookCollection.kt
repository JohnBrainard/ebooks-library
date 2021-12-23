package dev.johnbrainard.ebooks

data class EbookCollection(
	val id: EbookCollectionId,
	val name: String,
	val path: String
)
