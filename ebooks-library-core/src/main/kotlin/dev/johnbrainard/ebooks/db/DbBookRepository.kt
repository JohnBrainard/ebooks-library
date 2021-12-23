package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.EbookCollectionId
import dev.johnbrainard.ebooks.EbookId
import dev.johnbrainard.ebooks.EbookMeta
import dev.johnbrainard.ebooks.EbookMetaRepository
import java.sql.ResultSet
import javax.sql.DataSource

data class Ebook(
	val id: EbookId,
	val collectionId: EbookCollectionId,
	val name: String,
	val path: String
)

class DbBookRepository(private val dataSource: DataSource) : EbookMetaRepository {
	override fun listBooks(collectionId: EbookCollectionId): Collection<Ebook> {
		return dataSource.connection.use { conn ->
			val statement = conn.prepareStatement(
				"""
					select book_id, collection_id, name, path
					from ebooks.books
					where collection_id=?::uuid
					order by name
				""".trimIndent()
			)
			statement.setObject(1, collectionId.toString())

			val results = statement.executeQuery()

			sequence {
				while (results.next()) {
					yield(results.readEbook())
				}
			}.toList()
		}
	}

	override fun getMeta(ebookId: EbookId): EbookMeta {
		TODO("Not yet implemented")
	}

	private fun ResultSet.readEbook(): Ebook {
		return Ebook(
			id = EbookId(getString("book_id")),
			collectionId = EbookCollectionId(getString("collection_id")),
			name = getString("name"),
			path = getString("path")
		)
	}
}