package dev.johnbrainard.ebooks

import java.util.*

data class EbookListId(private val value: UUID) {
	constructor(value: String) : this(parseValue(value))

	override fun toString(): String = value.toString()

	private companion object {
		private fun parseValue(value: String) = try {
			UUID.fromString(value)
		} catch (_: IllegalArgumentException) {
			throw EbooksException("invalid id string")
		}
	}
}
