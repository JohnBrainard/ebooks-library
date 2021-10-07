package dev.johnbrainard.ebooks.api.plugins

data class CollectionEntryDto(
	val name: String,
	val url: String
) {
	var meta: MetaDto? = null
}

