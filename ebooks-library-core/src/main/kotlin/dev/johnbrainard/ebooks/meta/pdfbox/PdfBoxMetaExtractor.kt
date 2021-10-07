package dev.johnbrainard.ebooks.meta.pdfbox

import dev.johnbrainard.ebooks.meta.PdfMeta
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import org.apache.pdfbox.Loader
import java.io.InputStream

class PdfBoxMetaExtractor : PdfMetaExtractor {
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
