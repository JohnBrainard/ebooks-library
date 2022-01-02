package dev.johnbrainard.ebooks.index

import dev.johnbrainard.ebooks.EbookCollection

interface Indexer {
	fun scanCollections(): Sequence<CollectionEntry>
	fun scanCollection(collectionEntry: CollectionEntry): Sequence<BookEntry>
	fun run(reindex:Boolean = false)

	fun resolveCollectionEntry(collection: EbookCollection): CollectionEntry
	fun indexCollection(collectionEntry: CollectionEntry, reindex: Boolean = false)
}
