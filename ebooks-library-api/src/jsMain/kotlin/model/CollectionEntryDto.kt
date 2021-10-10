package model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionEntryDto(
	val name: String,
	val url: String
)

@Serializable
data class EntryDetailDto(
	val name: String,
	val url: String,
	val meta: EntryMetaDto
)

@Serializable
data class EntryMetaDto(
	val title: String,
	val authors: List<String>
)