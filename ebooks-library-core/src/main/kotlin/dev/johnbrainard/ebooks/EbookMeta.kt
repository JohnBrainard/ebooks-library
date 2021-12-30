package dev.johnbrainard.ebooks

data class EbookMeta(
	val id: EbookId?,
	val collectionId: EbookCollectionId,
	val name: String,
	val title: String,
	val path: String,
	val pageCount: Int,
	val authors: Set<String>,
	val contents: List<String>
)
