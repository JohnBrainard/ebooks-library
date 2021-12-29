package dev.johnbrainard.ebooks.meta.pdfbox

import dev.johnbrainard.ebooks.meta.PdfMeta
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
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
		val contents = extractContents(doc)

		return object : PdfMeta {
			override val title: String?
				get() = information.title
					?.let(::sanitizeTitle)

			override val contents: List<String>
				get() = contents.mapNotNull { sanitizeTitle(it) }

			override val authors: Set<String>
				get() = information.author?.let { setOf(it) } ?: emptySet()

		}
	}

	private fun extractContents(doc: PDDocument): List<String> {
		val outline = doc.documentCatalog?.documentOutline
		if (outline == null) {
			return emptyList()
		}

		var current = outline.firstChild
		val contents = sequence<String> {
			while (current != null) {
				yield(current.title)
				current = current.nextSibling
			}
		}.toList()

		return contents
	}
}

private fun sanitizeTitle(title: String) = title
	.takeWhile { it != Char(0) }
	.ifBlank { null }
