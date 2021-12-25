package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.*
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource


class DbCollectionRepository(private val dataSource: DataSource) : EbookCollectionRepository {

	private val logger = logger()

	private inner class DbOperations(val connection: Connection) {
		fun listCollections(): List<EbookCollection> {
			val statement = connection.createStatement()
			val results = statement.executeQuery(
				"""
					select collection_id,
							name,
							path
					from ebooks.collections
					order by name asc;
				""".trimIndent()
			)

			return sequence {
				while (results.next()) {
					yield(results.readCollection())
				}
			}.toList()
		}

		fun findCollectionById(collectionId: EbookCollectionId): EbookCollection {
			val statement = connection.prepareStatement(
				"""
					select collection_id, name, path 
					from ebooks.collections
					where collection_id = ?::uuid
				""".trimIndent()
			)

			statement.setObject(1, collectionId.toString())
			val results = statement.executeQuery()

			return sequence {
				while (results.next()) {
					yield(results.readCollection())
				}
			}.single()
		}

		fun findCollectionByPath(path: String): EbookCollection {
			val statement = connection.prepareStatement(
				"""
					select collection_id, name, path
					from ebooks.collections
					where path=?
				""".trimIndent()
			)
			statement.setString(1, path)
			val resultSet = statement.executeQuery()

			return sequence {
				while (resultSet.next()) {
					yield(resultSet.readCollection())
				}
			}.single()
		}

		fun saveCollection(collection: EbookCollection) {
			if (collection.id != null) {
				throw EbooksException("use updateCollection instead when saving with a known id")
			}

			val statement = connection.prepareStatement(
				"""
					insert into ebooks.collections (name, path) values (?, ?)
					on conflict (path) do update
					set name=excluded.name
				""".trimIndent()
			).apply {
				setString(1, collection.name)
				setString(2, collection.path)
			}

			statement.executeUpdate()
		}
	}

	override fun listCollections(): Collection<EbookCollection> {
		return withOperations { dbOperations ->
			dbOperations.listCollections()
		}
	}

	override fun getCollection(collectionId: EbookCollectionId): EbookCollection {
		return withOperations { dbOperations ->
			dbOperations.findCollectionById(collectionId)
		}
	}

	override fun saveCollection(block: EbookCollectionRepository.Builder.() -> Unit): EbookCollection {
		val collectionEntry = EbookCollectionRepository.Builder()
			.apply(block)
			.build()

		return withOperations { dbOperations ->
			dbOperations.saveCollection(collectionEntry)
			return@withOperations dbOperations.findCollectionByPath(collectionEntry.path)
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

	private fun ResultSet.readCollection(): EbookCollection {
		return EbookCollection(
			id = EbookCollectionId(getString("collection_id")),
			name = getString("name"),
			path = getString("path")
		)
	}
}