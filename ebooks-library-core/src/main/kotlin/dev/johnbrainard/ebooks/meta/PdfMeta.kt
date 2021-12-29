package dev.johnbrainard.ebooks.meta

interface PdfMeta {
	val title: String?
	val authors: Set<String>
	val contents: List<String>
	val pageCount: Int
}
