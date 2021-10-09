import kotlinx.html.js.onClickFunction
import model.CollectionSummaryDto
import react.Props
import react.RBuilder
import react.RComponent
import react.State
import react.dom.div
import react.dom.h2

external interface EbookCollectionSummaryProps : Props {
	var ebookCollection: CollectionSummaryDto
	var selected: Boolean
	var onEbookCollectionSelected: () -> Unit
}

@JsExport
class EbookCollectionSummary : RComponent<EbookCollectionSummaryProps, State>() {
	override fun RBuilder.render() {
		div {
			h2 {
				if (props.selected) {
					+"->"
				}
				+props.ebookCollection.name

				attrs.onClickFunction = {
					props.onEbookCollectionSelected()
				}
			}
		}
	}
}

fun RBuilder.ebookCollectionSummary(handler: EbookCollectionSummaryProps.() -> Unit) =
	child(EbookCollectionSummary::class) {
		this.attrs(handler)
	}
