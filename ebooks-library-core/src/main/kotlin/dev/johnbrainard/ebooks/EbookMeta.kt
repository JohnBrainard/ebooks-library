package dev.johnbrainard.ebooks

data class EbookMeta(
	val ebookId: EbookId,
	val title: String,
	val pageCount: Int,
	val authors: Set<String>,
	val contents: List<String>
)
