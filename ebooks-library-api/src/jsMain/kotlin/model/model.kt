package model

import endpoint
import io.ktor.client.request.*
import jsonClient

suspend fun getCollections(): CollectionsDto {
	return jsonClient.get("$endpoint/collections")
}

suspend fun getCollection(url: String): CollectionDto {
	return jsonClient.get(url)
}
