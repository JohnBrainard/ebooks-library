package dev.johnbrainard.ebooks.meta

import java.io.InputStream

interface PdfMetaExtractor {
	fun extract(inputStream: InputStream): PdfMeta
}
