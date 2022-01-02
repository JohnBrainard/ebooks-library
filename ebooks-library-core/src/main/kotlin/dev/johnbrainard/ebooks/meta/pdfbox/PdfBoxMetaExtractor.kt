package dev.johnbrainard.ebooks.meta.pdfbox

import dev.johnbrainard.ebooks.meta.ContentsEntry
import dev.johnbrainard.ebooks.meta.PdfMeta
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
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

			override val contents: List<ContentsEntry>
				get() = contents.mapNotNull { it }

			override val authors: Set<String>
				get() = information.author?.let { setOf(it) } ?: emptySet()

			override val pageCount: Int = doc.numberOfPages
		}
	}

	private fun countPages(doc: PDDocument): Int = doc.numberOfPages

	private fun extractContents(doc: PDDocument): List<ContentsEntry> {
		val contentsRoot = doc.documentCatalog?.documentOutline?.firstChild
		return if (contentsRoot != null) {
			extractContents(contentsRoot, 0)
				.toList()
		} else {
			emptyList()
		}
	}

	private fun extractContents(item: PDOutlineItem, level: Int): Sequence<ContentsEntry> = sequence {
		var next: PDOutlineItem? = item
		do {
			if (next == null) break

			val title = next.title?.let { sanitizeTitle(it) }
			if (title != null) {
				yield(
					ContentsEntry(
						level = level,
						title = title,
						pageStart = null,
						pageEnd = null
					)
				)
			}

			if (next.firstChild != null) {
				yieldAll(extractContents(next.firstChild, level + 1))
			}

			next = next.nextSibling
		} while (next != null)
	}

	private fun PDOutlineItem.toContentsEntry(level: Int) = ContentsEntry(
		level = level,
		title = title,
		pageStart = null,
		pageEnd = null
	)
}

private fun sanitizeTitle(title: String) = title
	.takeWhile { it != Char(0) }
	.ifBlank { null }
