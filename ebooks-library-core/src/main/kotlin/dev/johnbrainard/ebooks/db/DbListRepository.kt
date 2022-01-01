package dev.johnbrainard.ebooks.db

import dev.johnbrainard.ebooks.*
import dev.johnbrainard.ebooks.logger
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

class DbListRepository(private val dataSource: DataSource) : EbookListRepository {

	private data class ListRecord(
		val listId: EbookListId,
		val name: String
	)

	private data class ListEntryRecord(
		val list: ListRecord,
		val ebookId: EbookId?
	)

	private val logger = logger()

	private inner class DbOperations(val connection: Connection) {
		fun getLists(): List<EbookList> {
			val statement = connection.prepareStatement(
				"""
					select lists.list_id,
					       lists.name as list_name,
					       list_entries.book_id as book_id
					from ebooks.lists
					         left outer join ebooks.list_entries using (list_id)
					order by lists.name, lists.created_at desc, list_entries.created_at desc					
				""".trimIndent()
			)

			val results = statement.executeQuery()
			return sequence {
				while (results.next()) {
					yield(results.readListEntry())
				}
			}.toEbookLists()
		}

		fun getList(listId: EbookListId): EbookList {
			val statement = connection.prepareStatement(
				"""
					select lists.list_id,
					       lists.name as list_name,
					       list_entries.book_id as book_id
					from ebooks.lists
					         left outer join ebooks.list_entries using (list_id)
					where lists.list_id = ?::uuid
					order by lists.name, lists.created_at desc, list_entries.created_at desc					
				""".trimIndent()
			).apply { setString(1, listId.toString()) }

			val results = statement.executeQuery()
			return sequence {
				while (results.next()) {
					yield(results.readListEntry())
				}
			}.toEbookLists().single()
		}

		fun saveList(list: EbookList) {
			val statement = connection.prepareStatement(
				"""
					insert into ebooks.lists (name) values (?)
				""".trimIndent()
			).apply {
				setString(1, list.name)
			}

			statement.executeUpdate()
		}

		fun findListsWithBook(bookId: EbookId): List<EbookList> {
			val statement = connection.prepareStatement(
				"""
					select lists.list_id,
					       lists.name as list_name
					from ebooks.lists
					         left outer join ebooks.list_entries using (list_id)
					where list_entries.book_id=?::uuid
					order by lists.name, lists.created_at desc, list_entries.created_at desc										
				""".trimIndent()
			).apply {
				setString(1, bookId.toString())
			}

			val results = statement.executeQuery()
			return sequence {
				while (results.next()) {
					yield(results.readListSummary())
				}
			}.toList()
		}
	}

	override fun getLists(): List<EbookList> {
		return withOperations { dbOperations -> dbOperations.getLists() }
	}

	override fun getList(listId: EbookListId): EbookList {
		return withOperations { dbOperations ->
			dbOperations.getList(listId)
		}
	}

	override fun findListsContainingBook(bookId: EbookId): List<EbookList> {
		return withOperations { dbOperations ->
			dbOperations.findListsWithBook(bookId)
		}
	}

	override fun saveList(list: EbookList) {
		withOperations { dbOperations ->
			dbOperations.saveList(list)
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

	private fun ResultSet.readListEntry(): ListEntryRecord {
		return ListEntryRecord(
			list = ListRecord(
				listId = EbookListId(getString("list_id")),
				name = getString("list_name")
			),
			ebookId = getString("book_id")?.let { EbookId(it) }
		)
	}

	private fun ResultSet.readListSummary(): EbookList {
		return EbookList(
			id = EbookListId(getString("list_id")),
			name = getString("list_name")
		)
	}

	private fun Sequence<ListEntryRecord>.toEbookLists() = groupBy { it.list }
		.map { (list, records) ->
			EbookList(
				id = list.listId,
				name = list.name,
				entries = records
					.filter { it.ebookId != null }
					.map {
						EbookListEntry(it.ebookId!!)
					}.toList()
			)
		}

}