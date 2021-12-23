package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.*
import java.sql.ResultSet
import javax.sql.DataSource


class DbCollectionRepository(private val dataSource: DataSource) : EbookCollectionRepository {
	override fun listCollections(): Collection<EbookCollection> {
		return dataSource.connection.use {
			val statement = it.createStatement()
			val results = statement.executeQuery(
				"""
					select collection_id,
							name,
							path
					from ebooks.collections
					order by name asc;
				""".trimIndent()
			)

			sequence {
				while (results.next()) {
					yield(results.readCollection())
				}
			}.toList()
		}
	}

	override fun getCollection(collectionId: EbookCollectionId): EbookCollection {
		return dataSource.connection.use { conn ->
			val statement = conn.prepareStatement(
				"""
				select collection_id, name, path 
				from ebooks.collections
				where collection_id = ?::uuid
			""".trimIndent()
			)

			statement.setObject(1, collectionId.toString())
			val results = statement.executeQuery()

			sequence {
				while (results.next()) {
					yield(results.readCollection())
				}
			}.single()
		}
	}

	private fun ResultSet.readCollection(): EbookCollection {
		return EbookCollection(
			id = EbookCollectionId(getString("collection_id")),
			name = getString("name"),
			path = getString("path")
		)
	}
}