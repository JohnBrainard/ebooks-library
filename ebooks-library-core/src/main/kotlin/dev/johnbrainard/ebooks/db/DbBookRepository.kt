package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.*
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

data class Ebook(
	val id: EbookId?,
	val collectionId: EbookCollectionId,
	val name: String,
	val path: String,
	val title: String,
	val authors: Set<String>
)

class DbBookRepository(private val dataSource: DataSource) : EbookMetaRepository {

	private val logger = logger()

	private inner class DbOperations(val connection: Connection) {
		fun listBooks(collectionId: EbookCollectionId): Collection<Ebook> {
			val statement = connection.prepareStatement(
				"""
					select book_id, collection_id, name, path, title, authors
					from ebooks.books
					where collection_id=?::uuid
					order by title, name
				""".trimIndent()
			).apply {
				setObject(1, collectionId.toString())
			}

			val results = statement.executeQuery()
			return sequence {
				while (results.next()) {
					yield(results.readEbook())
				}
			}.toList()
		}

		fun saveBook(book: Ebook) {
			if (book.id != null) {
				throw EbooksException("use updateBook instead when saving with a known id")
			}

			val statement = connection.prepareStatement(
				"""
					insert into ebooks.books (collection_id, name, path, title, authors)
					values (?::uuid, ?, ?, ?, ?)
					on conflict (collection_id, path) do update
					set name=excluded.name,
						title=excluded.title,
						authors=excluded.authors
				""".trimIndent()
			).apply {
				setString(1, book.collectionId.toString())
				setString(2, book.name)
				setString(3, book.path)
				setString(4, book.title)
				setArray(5, connection.createArrayOf("text", book.authors.toTypedArray()))
			}

			statement.executeUpdate()
		}

		fun searchBooks(title: String? = null): Collection<Ebook> {
			val statement = connection.prepareStatement(
				"""
					select book_id, collection_id, name, path, title, authors
					from ebooks.books
					where to_tsvector('english', title) @@ to_tsquery(?)
					order by title, name
				""".trimIndent()
			).apply {
				setString(1, title)
			}

			val resultSet = statement.executeQuery()

			return sequence {
				while (resultSet.next()) {
					yield(resultSet.readEbook())
				}
			}.toList()
		}
	}

	override fun listBooks(collectionId: EbookCollectionId): Collection<Ebook> {
		return withOperations { dbOperations ->
			dbOperations.listBooks(collectionId)
		}
	}

	override fun saveBook(block: EbookMetaRepository.Builder.() -> Unit): Ebook {
		val ebook = EbookMetaRepository.Builder()
			.apply(block)
			.build()

		return withOperations { dbOperations ->
			dbOperations.saveBook(ebook)
			ebook
		}
	}

	override fun search(title: String?): Collection<Ebook> {
		if (title == null) {
			return emptyList()
		}

		return withOperations { dbOperations ->
			dbOperations.searchBooks(title = title)
		}
	}

	override fun getMeta(ebookId: EbookId): EbookMeta {
		TODO("Not yet implemented")
	}

	private fun <T> withOperations(block: (DbOperations) -> T): T {
		dataSource.connection.use { connection ->
			return try {
				block(DbOperations(connection))
					.also { connection.commit() }
			} catch (ex: SQLException) {
				connection.rollback()
				logger.error("unable to complete database operations", ex)
				throw EbooksException("unexpected error", ex)
			}
		}
	}

	private fun ResultSet.readEbook(): Ebook {
		val authors = getArray("authors").array as Array<String>

		return Ebook(
			id = EbookId(getString("book_id")),
			collectionId = EbookCollectionId(getString("collection_id")),
			name = getString("name"),
			path = getString("path"),
			title = getString("title"),
			authors = authors.toSet()
		)
	}
}