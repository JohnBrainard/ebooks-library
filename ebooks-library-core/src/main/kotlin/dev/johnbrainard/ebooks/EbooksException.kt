package dev.johnbrainard.ebooks

class EbooksException : RuntimeException {
	constructor() : super()
	constructor(message: String) : super(message)
	constructor(message: String, cause: Throwable) : super(message, cause)
}