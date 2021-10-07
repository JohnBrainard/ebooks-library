package dev.johnbrainard.ebooks

data class EbookMeta(
	val ebookId: EbookId,
	val title: String,
	val authors: Set<String>
)
