import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.id
import model.CollectionSummaryDto
import model.getCollections
import react.*
import react.dom.div

external interface EbooksAppState : State {
	var selectedCollection: CollectionSummaryDto?
	var collections: List<CollectionSummaryDto>
}

fun RBuilder.ebooksApp(handler: Props.() -> Unit) = child(EbooksApp::class) {
	this.attrs(handler)
}

class EbooksApp : RComponent<Props, EbooksAppState>() {
	override fun RBuilder.render() {
		ebookCollections {
			selected = state.selectedCollection
			onCollectionSelected = { collection ->
				setState {
					selectedCollection = collection
				}
			}
		}

		state.selectedCollection?.let {
			ebookCollection {
				ebookCollection = it
			}
		}
	}
}
