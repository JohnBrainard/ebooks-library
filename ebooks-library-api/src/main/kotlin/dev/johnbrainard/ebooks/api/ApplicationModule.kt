package dev.johnbrainard.ebooks.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.api.plugins.CollectionsService
import dev.johnbrainard.ebooks.api.plugins.DefaultCollectionsService
import dev.johnbrainard.ebooks.db.DbBookRepository
import dev.johnbrainard.ebooks.db.DbCollectionRepository
import dev.johnbrainard.ebooks.index.DefaultIndexer
import dev.johnbrainard.ebooks.index.Indexer
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import org.koin.dsl.module
import javax.sql.DataSource

val applicationModule = module {
	single<Indexer>(createdAtStart = true) {
		DefaultIndexer(getProperty("LIBRARY_PATH"), get(), get(), get())
	}

	single<EbookCollectionRepository> { DbCollectionRepository(get()) }
	single<EbookMetaRepository> { DbBookRepository(get()) }

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
