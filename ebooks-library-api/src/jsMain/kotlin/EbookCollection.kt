import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.*
import react.*
import react.dom.div
import react.dom.h2
import react.dom.li
import react.dom.ul

external interface EbookCollectionProps : Props {
	var ebookCollection: CollectionSummaryDto
}

external interface EbookCollectionState : State {
	var ebookCollection: CollectionDto?
	var entries: List<EntryDetailDto>
}

class EbookCollection : RComponent<EbookCollectionProps, EbookCollectionState>() {
	override fun EbookCollectionState.init() {
		console.log("updating ebook collection state")
		entries = emptyList()

		val scope = MainScope()
		scope.launch {
			val collectionDto = getCollection(props.ebookCollection.url)
			val entries = collectionDto.entries.map {
				getCollectionEntry(it.url)
			}
			setState {
				this.ebookCollection = collectionDto
				this.entries = entries
			}
		}
	}

	override fun componentDidUpdate(prevProps: EbookCollectionProps, prevState: EbookCollectionState, snapshot: Any) {
		if (this.props.ebookCollection != prevProps.ebookCollection) {
			this.state.init()
		}
	}

	override fun RBuilder.render() {
		div {
			h2 {
				+props.ebookCollection.name
			}

			ul {
				state.entries.forEach {
					li {
						+it.meta.title
					}
				}
			}
		}
	}
}

fun RBuilder.ebookCollection(handler: EbookCollectionProps.() -> Unit) = child(EbookCollection::class) {
	attrs(handler)
}
