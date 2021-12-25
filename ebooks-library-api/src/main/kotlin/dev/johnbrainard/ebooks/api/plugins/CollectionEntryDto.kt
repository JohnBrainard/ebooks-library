package dev.johnbrainard.ebooks.api.plugins

data class CollectionEntryDto(
	val name: String,
	val url: String,
	val title: String?,
	val authors: Collection<String>
) {
	var meta: MetaDto? = null
}

