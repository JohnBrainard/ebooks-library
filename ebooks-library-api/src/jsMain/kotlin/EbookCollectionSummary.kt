import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import model.CollectionSummaryDto
import react.Props
import react.RBuilder
import react.RComponent
import react.State
import react.dom.div
import react.dom.h1

external interface EbookCollectionSummaryProps : Props {
	var ebookCollection: CollectionSummaryDto
	var selected: Boolean
	var onEbookCollectionSelected: () -> Unit
}

@JsExport
class EbookCollectionSummary : RComponent<EbookCollectionSummaryProps, State>() {
	override fun RBuilder.render() {
		div {
			attrs.classes = setOf("collection")
			if (props.selected) {
				attrs.classes = attrs.classes + "selected"
			}

			attrs.onClickFunction = {
				props.onEbookCollectionSelected()
			}

			h1 {
				+props.ebookCollection.name
			}
		}
	}
}

fun RBuilder.ebookCollectionSummary(handler: EbookCollectionSummaryProps.() -> Unit) =
	child(EbookCollectionSummary::class) {
		this.attrs(handler)
	}
