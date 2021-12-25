package dev.johnbrainard.ebooks.meta

interface PdfMeta {
	val title: String?
	val authors: Set<String>
}
