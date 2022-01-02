package dev.johnbrainard.ebooks.api

import dev.johnbrainard.ebooks.EbookCollection
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

	fun scheduleFullIndex() {
		logger.info("reindexing library")
		executor.schedule(
			{ indexer.run(true) },
			0, TimeUnit.SECONDS
		)
	}

	fun scheduleCollection(collection: EbookCollection) {
		logger.info("reindexing collection: ${collection.path}")
		val collectionEntry = indexer.resolveCollectionEntry(collection)

		executor.schedule(
			{ indexer.indexCollection(collectionEntry, reindex = true) },
			0, TimeUnit.SECONDS
		)
	}
}
