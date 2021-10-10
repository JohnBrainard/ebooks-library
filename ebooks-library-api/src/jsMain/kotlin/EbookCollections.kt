import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.id
import model.CollectionSummaryDto
import model.getCollections
import react.*
import react.dom.div

external interface EbookCollectionsProps : Props {
	var selected: CollectionSummaryDto?
	var onCollectionSelected: (collection: CollectionSummaryDto) -> Unit
}

external interface EbookCollectionsState : State {
	var collections: List<CollectionSummaryDto>
}

class EbookCollections : RComponent<EbookCollectionsProps, EbookCollectionsState>() {
	override fun EbookCollectionsState.init() {
		collections = emptyList()

		val scope = MainScope()
		scope.launch {
			val collections = getCollections()
			setState {
				this.collections = collections.collections.sortedBy { it.name }
			}
		}
	}

	override fun RBuilder.render() {
		div {
			attrs.id = "collections"

			state.collections.forEach { ebookCollection ->
				ebookCollectionSummary {
					this.ebookCollection = ebookCollection
					this.selected = ebookCollection == props.selected
					this.onEbookCollectionSelected = {
						props.onCollectionSelected(ebookCollection)
					}
				}
			}
		}
	}
}

fun RBuilder.ebookCollections(handler: EbookCollectionsProps.() -> Unit) = child(EbookCollections::class) {
	attrs(handler)
}
