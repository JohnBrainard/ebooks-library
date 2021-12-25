package dev.johnbrainard.ebooks.index

import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookMetaRepository
import dev.johnbrainard.ebooks.logger
import dev.johnbrainard.ebooks.meta.PdfMetaExtractor
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.io.path.isRegularFile
import kotlin.streams.asSequence

class DefaultIndexer(
	path: String,
	private val metaExtractor: PdfMetaExtractor,
	private val collectionRepository: EbookCollectionRepository,
	private val ebookMetaRepository: EbookMetaRepository
) : Indexer {

	private val libraryPath = Path.of(path)
	private val executor = Executors.newFixedThreadPool(8)

	private val logger = logger()

	init {
		logger.info("scheduling indexing")
		executor.submit {
			logger.info("indexing started")
			scanCollections()
				.onEach { logger.debug("indexing entry: $it") }
				.forEach { indexCollection(it) }
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

		val entries = Files.walk(collectionEntry.fullPath)
			.filter { it.isRegularFile() && globMatcher.matches(it) }
			.map { path ->
				collectionEntry.createBookEntry(path)
			}

		return entries.asSequence()
	}

	private fun indexCollection(collectionEntry: CollectionEntry) {
		logger.debug("indexing collection $collectionEntry")
		val collection = collectionRepository.saveCollection {
			name = collectionEntry.name
			path = collectionEntry.path.toString()
		}

		executor.submit {
			val bookEntries = scanCollection(collectionEntry)
			bookEntries.onEach { logger.debug("indexing book entry: $it") }
				.forEach { book ->
					ebookMetaRepository.saveBook {
						collectionId = collection.id
						name = book.name
						title = book.title
						path = book.path.toString()
						authors.addAll(book.authors)
					}
				}
		}
	}

	private fun CollectionEntry.createBookEntry(entryPath: Path): BookEntry {
		val pdfMeta = metaExtractor.extract(entryPath)

		return BookEntry(
			path = fullPath.relativize(entryPath),
			fullPath = entryPath,
			name = entryPath.fileName.toString(),
			title = pdfMeta.title,
			authors = pdfMeta.authors
		)
	}
}
