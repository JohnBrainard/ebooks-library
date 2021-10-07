package dev.johnbrainard.ebooks.files

import dev.johnbrainard.ebooks.EbookCollection
import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookCollectionRepository
import dev.johnbrainard.ebooks.EbookId
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.relativeTo

data class FilesEbookCollectionId(
	override val name: String,
	val path: Path
) : EbookCollectionId

data class FilesEbookId(
	override val collectionId: EbookCollectionId,
	override val name: String,
	val path: Path
) : EbookId

class FilesEbookCollectionRepository(private val path: Path) : EbookCollectionRepository {
	override fun readCollectionId(id: String): EbookCollectionId {
		val collectionPath = path.resolve(id)
		return FilesEbookCollectionId(id, collectionPath)
	}

	override fun readEbookId(collectionId: EbookCollectionId, id: String): EbookId {
		if (collectionId !is FilesEbookCollectionId) throw IllegalArgumentException("collectionId is the wrong type")
		return FilesEbookId(
			collectionId,
			id,
			collectionId.path.resolve(id),
		)
	}

	override fun listCollections(): Collection<EbookCollectionId> {
		return path.listDirectoryEntries()
			.filter { it.isDirectory() }
			.map { path ->
				FilesEbookCollectionId(
					path.fileName.toString(),
					path
				)
			}
	}

	override fun getCollection(collectionId: EbookCollectionId): EbookCollection {
		if (collectionId !is FilesEbookCollectionId) throw IllegalArgumentException()

		val matcher = FileSystems.getDefault()
			.getPathMatcher("glob:**/*.pdf")

		val ebooks = Files.walk(collectionId.path)
			.filter(matcher::matches)
			.use { stream ->
				stream.collect(Collectors.toList())
			}

		return FilesEbookCollection(
			id = collectionId,
			name = collectionId.name,
			ebooks = ebooks.map {
				FilesEbookId(
					collectionId,
					it.relativeTo(collectionId.path).toString(),
					it
				)
			}
		)
	}
}

