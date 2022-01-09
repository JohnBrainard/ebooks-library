package dev.johnbrainard.ebooks.meta.pdfbox

import dev.johnbrainard.ebooks.logger
import dev.johnbrainard.ebooks.meta.ContentsEntry
import dev.johnbrainard.ebooks.meta.PdfMeta
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class PdfBoxMetaExtractor : PdfMetaExtractor {

	private val logger = logger()

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
			extractContents(doc, contentsRoot, 0)
				.toList()
		} else {
			emptyList()
		}
	}

	private fun extractContents(doc: PDDocument, item: PDOutlineItem, level: Int): Sequence<ContentsEntry> = sequence {
		var next: PDOutlineItem? = item
		do {
			if (next == null) break

			val title = next.title?.let { sanitizeTitle(it) }
			if (title != null) {
				val (pageStart, destinationName) = next.readDestination()

				yield(
					ContentsEntry(
						level = level,
						title = title,
						pageStart = pageStart,
						destination = destinationName
					)
				)
			}

			if (next.firstChild != null) {
				yieldAll(extractContents(doc, next.firstChild, level + 1))
			}

			next = next.nextSibling
		} while (next != null)
	}

	private fun PDDestination.pageNumber(): Int? = if (this is PDPageDestination) {
		retrievePageNumber()
	} else {
		null
	}

	private fun PDDestination.destinationName(): String? = if (this is PDNamedDestination) {
		namedDestination
	} else {
		null
	}

	private fun PDOutlineItem.readDestination(): Pair<Int?, String?> {
		val destination = if (action is PDActionGoTo) {
			(action as PDActionGoTo).destination
		} else {
			destination
		}

		return Pair(
			destination?.pageNumber(),
			destination?.destinationName()
		)
	}
}

private fun sanitizeTitle(title: String) = title
	.takeWhile { it != Char(0) }
	.ifBlank { null }
