package dev.johnbrainard.ebooks.api.plugins

data class CollectionDto(
	val name: String,
	val url: String,
	val entries: Collection<CollectionEntryDto>
)
