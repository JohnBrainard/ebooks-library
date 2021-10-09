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
