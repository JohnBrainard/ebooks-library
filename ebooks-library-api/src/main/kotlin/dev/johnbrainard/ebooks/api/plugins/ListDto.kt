package dev.johnbrainard.ebooks.api.plugins

data class ListDto(
	val id: String,
	val name: String,
	val entryCount: Int,
	val entries: List<CollectionEntryDto> = emptyList()
)
