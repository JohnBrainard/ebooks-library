import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import java.nio.file.Path

interface Extractor {
	fun run(path: Path)
}

class DefaultExtractor(private val metaExtractor: PdfMetaExtractor) : Extractor {
	override fun run(path: Path) {
		val pdfMeta = metaExtractor.extract(path)
		val contents = pdfMeta.contents.joinToString("\n\t")

		println(
			"""
				Title: ${pdfMeta.title}
				Authors: ${pdfMeta.authors.joinToString(",")}
			""".trimIndent()
		)

		println("Contents: \n\t$contents")
	}
}
