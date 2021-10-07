package dev.johnbrainard.ebooks

interface EbookCollection {
    val id: EbookCollectionId
    val name: String
    val ebooks: Collection<EbookId>
}
