package dev.johnbrainard.ebooks.api

import dev.johnbrainard.ebooks.index.DefaultIndexer
import dev.johnbrainard.ebooks.index.Indexer
import org.koin.dsl.module
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

val indexerModule = module {
	single<Indexer> {
		DefaultIndexer(getProperty("LIBRARY_PATH"), get(), get(), get(), get())
	}

	single(createdAtStart = true) { IndexerScheduler(get(), get()) }
}

class IndexerScheduler(
	private val indexer: Indexer,
	private val executor: ScheduledExecutorService
) {
	private val logger = logger()

	init {
		logger.info("scheduling indexer")
		executor.scheduleAtFixedRate(indexer::run, 1, 60, TimeUnit.MINUTES)
	}
}
