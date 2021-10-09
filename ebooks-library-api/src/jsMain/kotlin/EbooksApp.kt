import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import model.CollectionSummaryDto
import model.getCollections
import react.*
import react.dom.div
import react.dom.h1
import react.dom.h2

external interface EbooksAppState : State {
	var selectedCollection: CollectionSummaryDto?
	var collections: List<CollectionSummaryDto>
}

fun RBuilder.ebooksApp(handler: Props.() -> Unit) = child(EbooksApp::class) {
	this.attrs(handler)
}

class EbooksApp : RComponent<Props, EbooksAppState>() {
	override fun EbooksAppState.init() {
		collections = emptyList()

		val scope = MainScope()
		scope.launch {
			val collections = getCollections()
			setState {
				this.collections = collections.collections.sortedBy { it.name }
				this.selectedCollection = this.collections.firstOrNull()
			}
		}
	}

	override fun RBuilder.render() {
		h1 {
			+"eBooks Collections"
		}

		state.collections.forEach { ebookCollection ->
			ebookCollectionSummary {
				this.ebookCollection = ebookCollection
				this.selected = ebookCollection == state.selectedCollection
				this.onEbookCollectionSelected = {
					setState {
						selectedCollection = ebookCollection
					}
				}
			}
		}
	}
}
