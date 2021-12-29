import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import org.koin.dsl.module

val extractorModule = module {
	single<PdfMetaExtractor>(createdAtStart = true) {
		PdfBoxMetaExtractor()
	}

	single<Extractor> {
		DefaultExtractor(get())
	}
}
