import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.h1
import react.dom.li
import react.dom.ul

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
	val (collections, setCollections) = useState(CollectionsDto(emptyList()))

	useEffectOnce {
		scope.launch {
			console.log("getting collections...")
			setCollections(getCollections())
		}
	}

	h1 {
		+"eBook Collections"
	}
	ul {
		collections.collections.sortedBy { it.name }.forEach { collection ->
			li {
				key = collection.name
				+collection.name
			}
		}
	}
}
