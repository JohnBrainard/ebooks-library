package dev.johnbrainard.ebooks.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.api.plugins.CollectionsService
import dev.johnbrainard.ebooks.api.plugins.DefaultCollectionsService
import dev.johnbrainard.ebooks.db.DbBookRepository
import dev.johnbrainard.ebooks.db.DbCollectionRepository
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import dev.johnbrainard.ebooks.meta.pdfbox.PdfBoxMetaExtractor
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.sql.DataSource

val applicationModule = module {
	single<EbookCollectionRepository> { DbCollectionRepository(get()) }
	single<EbookMetaRepository> { DbBookRepository(get()) }

	single<PdfMetaExtractor> { PdfBoxMetaExtractor() }
	single<CollectionsService> { DefaultCollectionsService(get(), get()) }

	single<DataSource> {
		HikariDataSource(get())
	}

	single<ScheduledExecutorService> {
		Executors.newScheduledThreadPool(1)
	}

	single<ExecutorService> {
		Executors.newFixedThreadPool(8)
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
			schema = "public"
			validate()
		}
	}
}
