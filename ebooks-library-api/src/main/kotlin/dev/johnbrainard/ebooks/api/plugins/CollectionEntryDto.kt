package dev.johnbrainard.ebooks.api.plugins

data class CollectionEntryDto(
	val name: String,
	val collectionName: String,
	val url: String,
	val collectionUrl: String,
	val downloadUrl: String,
	val title: String?,
	val path: String,
	val authors: Collection<String>
) {
	var meta: MetaDto? = null
}
