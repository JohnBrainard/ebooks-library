package dev.johnbrainard.ebooks.api.plugins

data class CollectionEntryDto(
	val id: String,
	val name: String,
	val url: String,
	val collectionId: String,
	val collectionName: String,
	val collectionUrl: String,
	val downloadUrl: String,
	val title: String?,
	val path: String,
	val pageCount: Int,
	val authors: Collection<String>,
	val contents: Collection<ContentsDto>
) {
	var meta: MetaDto? = null
}
