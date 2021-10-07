package dev.johnbrainard.ebooks

interface EbookMetaRepository {
	fun getMeta(ebookId: EbookId): EbookMeta
}
