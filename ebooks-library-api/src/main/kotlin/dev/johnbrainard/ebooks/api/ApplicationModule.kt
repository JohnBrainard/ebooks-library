package dev.johnbrainard.ebooks.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.api.plugins.CollectionsService
import dev.johnbrainard.ebooks.api.plugins.DefaultCollectionsService
import dev.johnbrainard.ebooks.files.FilesEbookCollectionRepository
import dev.johnbrainard.ebooks.files.FilesEbookMetaRepository
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import org.koin.dsl.module
import javax.sql.DataSource

val applicationModule = module {
	single<EbookCollectionRepository> {
		FilesEbookCollectionRepository(getProperty("LIBRARY_PATH"))
	}

//	single<EbookCollectionRepository> {
//		DbCollectionRepository(get())
//	}

	single<EbookMetaRepository> { FilesEbookMetaRepository(get()) }
	single<PdfMetaExtractor> { PdfBoxMetaExtractor() }
	single<CollectionsService> { DefaultCollectionsService(get(), get()) }

	single<DataSource> {
		HikariDataSource(get())
	}

	single {
		HikariConfig().apply {
			driverClassName = "org.postgresql.Driver"
			jdbcUrl = getProperty("DB_URL")
			username = getProperty("DB_USER")
			password = getProperty("DB_PASSWORD")
			maximumPoolSize = 3
			isAutoCommit = false
			transactionIsolation = "TRANSACTION_REPEATABLE_READ"
			validate()
		}
	}
}
