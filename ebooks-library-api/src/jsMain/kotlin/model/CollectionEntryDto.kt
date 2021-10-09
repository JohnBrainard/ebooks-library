package model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionEntryDto(
	val name: String,
	val url: String
)
