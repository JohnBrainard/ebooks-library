package dev.johnbrainard.ebooks.api.plugins

import java.net.URLDecoder
import java.net.URLEncoder

fun encode(url: String): String = URLEncoder.encode(url, Charsets.UTF_8)
fun decode(url: String): String = URLDecoder.decode(url, Charsets.UTF_8)

data class CollectionsDto(
	val collections: Collection<CollectionSummaryDto>
)

data class CollectionSummaryDto(
	val name: String,
	val path: String,
	val url: String
)
