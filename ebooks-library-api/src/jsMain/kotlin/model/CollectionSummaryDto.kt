package model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionSummaryDto(
	val name: String,
	val path: String,
	val url: String
)
