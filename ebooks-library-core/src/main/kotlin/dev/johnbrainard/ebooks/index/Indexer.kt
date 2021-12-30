package dev.johnbrainard.ebooks.index

interface Indexer {
	fun scanCollections(): Sequence<CollectionEntry>
	fun scanCollection(collectionEntry: CollectionEntry): Sequence<BookEntry>
	fun run()
}
