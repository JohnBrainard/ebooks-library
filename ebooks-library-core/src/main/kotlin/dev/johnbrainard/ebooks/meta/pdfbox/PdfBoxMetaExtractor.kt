package dev.johnbrainard.ebooks.meta.pdfbox

import dev.johnbrainard.ebooks.meta.PdfMeta
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import org.apache.pdfbox.Loader
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class PdfBoxMetaExtractor : PdfMetaExtractor {
	override fun extract(path: Path): PdfMeta = Files.newInputStream(path).use(this::extract)

	override fun extract(inputStream: InputStream): PdfMeta {
		val doc = inputStream.use {
			Loader.loadPDF(inputStream)
		}

		val information = doc.documentInformation

		return object : PdfMeta {
			override val title: String
				get() = information.title ?: ""

			override val authors: Set<String>
				get() = information.author?.let { setOf(it) } ?: emptySet()

		}
	}
}
