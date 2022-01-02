package dev.johnbrainard.ebooks.meta

data class ContentsEntry(
	val level: Int,
	val title: String,
	val pageStart: Int?,
	val pageEnd: Int?
)
