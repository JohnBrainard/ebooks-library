package dev.johnbrainard.ebooks.index

data class ContentsEntry(
	val level: Int,
	val title: String,
	val pageStart: Int?,
	val destination: String?
)