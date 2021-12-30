package dev.johnbrainard.ebooks.index

import java.nio.file.Path

data class BookEntry(
	val path: Path,
	val fullPath: Path,
	val name: String,
	val title: String,
	val pageCount: Int,
	val authors: Set<String>,
	val contents: List<String>
)
