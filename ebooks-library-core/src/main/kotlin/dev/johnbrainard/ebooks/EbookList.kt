package dev.johnbrainard.ebooks

data class EbookList(
	val id: EbookListId? = null,
	val name: String,
	val entries: List<EbookListEntry> = emptyList()
)
