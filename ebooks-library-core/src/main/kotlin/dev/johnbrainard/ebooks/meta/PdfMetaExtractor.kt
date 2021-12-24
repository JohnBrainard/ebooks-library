package dev.johnbrainard.ebooks.meta

import java.io.InputStream
import java.nio.file.Path

interface PdfMetaExtractor {
	fun extract(inputStream: InputStream): PdfMeta
	fun extract(path: Path): PdfMeta
}
