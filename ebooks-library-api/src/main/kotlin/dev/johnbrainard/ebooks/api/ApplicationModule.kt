package dev.johnbrainard.ebooks.api

import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.api.plugins.CollectionsService
import dev.johnbrainard.ebooks.api.plugins.DefaultCollectionsService
import dev.johnbrainard.ebooks.files.FilesEbookCollectionRepository
import dev.johnbrainard.ebooks.files.FilesEbookMetaRepository
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import org.koin.dsl.module

val applicationModule = module {
	single<EbookCollectionRepository> {
		FilesEbookCollectionRepository(getProperty("LIBRARY_PATH"))
	}

	single<EbookMetaRepository> { FilesEbookMetaRepository(get()) }
	single<PdfMetaExtractor> { PdfBoxMetaExtractor() }
	single<CollectionsService> { DefaultCollectionsService(get(), get()) }
}
