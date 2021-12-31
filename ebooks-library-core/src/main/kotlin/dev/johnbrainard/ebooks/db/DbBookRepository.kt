package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.*
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

class DbBookRepository(private val dataSource: DataSource) : EbookRepository {

	private val logger = logger()

	private inner class DbOperations(val connection: Connection) {
		fun listBooks(collectionId: EbookCollectionId): Collection<Ebook> {
			val statement = connection.prepareStatement(
				"""
					select book_id, collection_id, name, path, title, authors, contents, page_count
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

		fun getBook(bookId: EbookId): Ebook {
			val statement = connection.prepareStatement(
				"""
					select book_id, collection_id, name, path, title, authors, contents, page_count
					from ebooks.books
					where book_id=?::uuid
					order by title, name
				""".trimIndent()
			).apply {
				setObject(1, bookId.toString())
			}

			val results = statement.executeQuery()
			return sequence {
				while (results.next()) {
					yield(results.readEbook())
				}
			}.single()
		}

		fun saveBook(book: Ebook) {
			if (book.id != null) {
				throw EbooksException("use updateBook instead when saving with a known id")
			}

			val statement = connection.prepareStatement(
				"""
					insert into ebooks.books (collection_id, name, path, title, authors, contents, page_count)
					values (?::uuid, ?, ?, ?, ?, ?, ?)
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
				setArray(6, connection.createArrayOf("text", book.contents.toTypedArray()))
				setInt(7, book.pageCount)
			}

			statement.executeUpdate()
		}

		fun searchBooks(terms: String? = null): Collection<Ebook> {
			val statement = connection.prepareStatement(
				"""
					select book_id, collection_id, name, path, title, authors, contents, page_count
					from ebooks.books
					where to_tsvector('english', title) @@ to_tsquery(?)
						or to_tsvector('english', text_array_to_string(contents, ' ', ' ')) @@ to_tsquery(?)
					order by title, name
				""".trimIndent()
			).apply {
				setString(1, terms)
				setString(2, terms)
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

	override fun saveBook(block: EbookRepository.Builder.() -> Unit): Ebook {
		val ebook = EbookRepository.Builder()
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
			dbOperations.searchBooks(terms = title)
		}
	}

	override fun getMeta(ebookId: EbookId): Ebook {
		return withOperations { dbOperations ->
			dbOperations.getBook(ebookId)
		}
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
		val contents = getArray("contents").array as Array<String>

		return Ebook(
			id = EbookId(getString("book_id")),
			collectionId = EbookCollectionId(getString("collection_id")),
			name = getString("name"),
			path = getString("path"),
			title = getString("title"),
			authors = authors.toSet(),
			contents = contents.toList(),
			pageCount = getInt("page_count")
		)
	}
}
