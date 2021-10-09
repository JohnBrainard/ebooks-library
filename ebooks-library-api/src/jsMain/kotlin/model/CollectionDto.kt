package model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
	val name: String,
	val url: String,
	val entries: List<CollectionEntryDto>
)
