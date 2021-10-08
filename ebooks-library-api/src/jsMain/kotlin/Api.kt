import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.browser.window
import kotlinx.serialization.Serializable

val endpoint = window.location.origin

val jsonClient = HttpClient {
	install(JsonFeature) {
		serializer = KotlinxSerializer()
	}
}

suspend fun getCollections(): CollectionsDto {
	return jsonClient.get("$endpoint/collections")
}

@Serializable
data class CollectionsDto(
	val collections: List<CollectionDto>
)

@Serializable
data class CollectionDto(
	val name: String,
	val path: String,
	val url: String
)
