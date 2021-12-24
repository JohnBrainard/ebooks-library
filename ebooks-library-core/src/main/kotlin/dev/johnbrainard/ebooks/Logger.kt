package dev.johnbrainard.ebooks

import org.slf4j.LoggerFactory

internal inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)
