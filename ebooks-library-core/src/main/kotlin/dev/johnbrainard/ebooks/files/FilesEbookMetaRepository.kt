package dev.johnbrainard.ebooks.files

import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookMeta
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import java.lang.IllegalArgumentException
import kotlin.io.path.inputStream

class FilesEbookMetaRepository(
	private val pdfMetaExtractor: PdfMetaExtractor
) : EbookMetaRepository {

	override fun getMeta(ebookId: EbookId): EbookMeta {
		if (ebookId !is FilesEbookId) throw IllegalArgumentException("ebookId is the wrong kind")

		val pdfMeta = ebookId.path.inputStream().use {
			pdfMetaExtractor.extract(it)
		}

		return EbookMeta(
			ebookId,
			pdfMeta.title,
			pdfMeta.authors
		)
	}
}