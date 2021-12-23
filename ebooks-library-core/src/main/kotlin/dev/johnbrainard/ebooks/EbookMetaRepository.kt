package dev.johnbrainard.ebooks

import dev.johnbrainard.ebooks.db.Ebook

interface EbookMetaRepository {
	fun getMeta(ebookId: EbookId): EbookMeta
	fun listBooks(collectionId: EbookCollectionId): Collection<Ebook>
}
