package dev.johnbrainard.ebooks.index

import java.nio.file.Path

data class CollectionEntry(
	val path: Path,
	val fullPath: Path,
	val name: String
)
