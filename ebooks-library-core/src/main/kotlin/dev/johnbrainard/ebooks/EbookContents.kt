package dev.johnbrainard.ebooks

data class EbookContents(
	val level: Int,
	val title: String,
	val pageStart: Int? = null,
	val destination: String? = null
)
