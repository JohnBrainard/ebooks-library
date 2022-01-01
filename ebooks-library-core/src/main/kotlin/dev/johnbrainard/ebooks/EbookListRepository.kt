package dev.johnbrainard.ebooks

interface EbookListRepository {
	fun getLists(): List<EbookList>
	fun getList(listId: EbookListId): EbookList

	fun findListsContainingBook(bookId:EbookId):List<EbookList>

	fun saveList(list: EbookList)
}
