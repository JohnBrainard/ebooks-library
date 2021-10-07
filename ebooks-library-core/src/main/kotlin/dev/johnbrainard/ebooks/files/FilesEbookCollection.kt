package dev.johnbrainard.ebooks.files

import dev.johnbrainard.ebooks.EbookCollection
import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId

data class FilesEbookCollection(
	override val id: EbookCollectionId,
	override val name: String,
	override val ebooks: Collection<EbookId>
) : EbookCollection
