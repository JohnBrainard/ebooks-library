package dev.johnbrainard.ebooks

data class EbookList(
	val id: EbookListId,
	val name: String,
	val entries: List<EbookListEntry>
)
