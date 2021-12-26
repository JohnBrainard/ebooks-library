package dev.johnbrainard.ebooks.api.plugins

data class CollectionSummaryDto(
	val id: String?,
	val name: String,
	val path: String,
	val url: String
)
