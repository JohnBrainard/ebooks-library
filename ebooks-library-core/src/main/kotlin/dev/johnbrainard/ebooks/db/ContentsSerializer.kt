package dev.johnbrainard.ebooks.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.readValue
import dev.johnbrainard.ebooks.EbookContents

class ContentsSerializer {
	fun serialize(contents: Collection<EbookContents>): String = contentsMapper.writeValueAsString(contents)
	fun deserialize(contents: String): Collection<EbookContents> = contentsMapper.readValue(contents)

	private companion object {
		private val contentsMapper = ObjectMapper().apply {
			findAndRegisterModules()
			propertyNamingStrategy = PropertyNamingStrategies.SnakeCaseStrategy()
		}
	}
}
