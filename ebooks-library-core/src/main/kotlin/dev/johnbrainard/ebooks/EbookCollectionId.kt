package dev.johnbrainard.ebooks

import java.util.*

@JvmInline
value class EbookCollectionId(private val uuid: UUID) {
	constructor(value: String) : this(parseIdValue(value))

	private companion object {
		private fun parseIdValue(value: String): UUID = try {
			UUID.fromString(value)
		} catch (ex: IllegalArgumentException) {
			throw EbooksException("invalid id string")
		}
	}

	override fun toString(): String = uuid.toString()
}
