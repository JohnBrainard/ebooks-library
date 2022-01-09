package dev.johnbrainard.ebooks.index

import dev.johnbrainard.ebooks.*
import dev.johnbrainard.ebooks.logger
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ExecutorService
import kotlin.io.path.isRegularFile
import kotlin.streams.asSequence

class DefaultIndexer(
	path: String,
	private val executor: ExecutorService,
	private val metaExtractor: PdfMetaExtractor,
	private val collectionRepository: EbookCollectionRepository,
	private val ebookRepository: EbookRepository
) : Indexer {

	private val libraryPath = Path.of(path)

	private val logger = logger()

	override fun run(reindex: Boolean) {
		logger.info("scheduling indexing")
		executor.submit {
			logger.info("indexing started")
			try {
				scanCollections()
					.onEach { logger.debug("indexing entry: $it") }
					.forEach { indexCollection(it, reindex) }
			} catch (ex: Exception) {
				logger.error("uncaught exception encountered", ex)
			}
		}
	}

	override fun scanCollections(): Sequence<CollectionEntry> {
		val collectionPathStream = Files.list(libraryPath)
			.filter { path -> Files.isDirectory(path) }
			.map { path ->
				CollectionEntry(
					path = libraryPath.relativize(path),
					fullPath = path,
					name = path.fileName.toString()
				)
			}

		return collectionPathStream.asSequence()
	}

	override fun scanCollection(collectionEntry: CollectionEntry): Sequence<BookEntry> {
		val globMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.pdf")

		return Files.walk(collectionEntry.fullPath)
			.asSequence()
			.filter { it.isRegularFile() && globMatcher.matches(it) }
			.map { path ->
				collectionEntry.createBookEntry(path)
			}
	}

	override fun resolveCollectionEntry(collection: EbookCollection): CollectionEntry {
		val path = Path.of(collection.path)
		val fullPath = libraryPath.resolve(path)

		return CollectionEntry(path, fullPath, collection.name)
	}

	override fun indexCollection(collectionEntry: CollectionEntry, reindex: Boolean) {
		logger.debug("indexing collection $collectionEntry")
		if (!reindex && collectionRepository.existsAtPath(collectionEntry.path.toString())) {
			logger.info("collection at path ${collectionEntry.path} already exists; skipping")
			return
		}

		val collection = collectionRepository.saveCollection {
			name = collectionEntry.name
			path = collectionEntry.path.toString()
		}

		executor.submit {
			try {
				scanCollection(collectionEntry)
					.onEach { logger.debug("indexing book entry: ${it.path}, ${it.title}") }
					.forEach { book ->
						ebookRepository.saveBook {
							collectionId = collection.id
							name = book.name
							title = book.title
							pageCount = book.pageCount
							path = book.path.toString()
							authors.addAll(book.authors)
							contents.addAll(book.contents.map {
								EbookContents(
									level = it.level,
									title = it.title,
									pageStart = it.pageStart,
									destination = it.destination
								)
							})
						}
					}
			} catch (ex: Exception) {
				logger.error("uncaught exception encountered", ex)
			}
		}
	}

	private fun CollectionEntry.createBookEntry(entryPath: Path): BookEntry {
		val pdfMeta = metaExtractor.extract(entryPath)

		return BookEntry(
			path = fullPath.relativize(entryPath),
			fullPath = entryPath,
			name = entryPath.fileName.toString(),
			title = pdfMeta.title ?: entryPath.fileName.toString(),
			authors = pdfMeta.authors,
			contents = pdfMeta.contents.map {
				ContentsEntry(
					level = it.level,
					title = it.title,
					pageStart = it.pageStart,
					destination = it.destination
				)
			},
			pageCount = pdfMeta.pageCount
		)
	}
}
