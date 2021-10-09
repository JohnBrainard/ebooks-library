package model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionsDto(
	val collections: List<CollectionSummaryDto>
)
