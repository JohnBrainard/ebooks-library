import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import org.koin.core.context.startKoin
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
	val argParser = ArgParser("meta-extractor")

	val pdFPath by argParser.option(ArgType.String, "path", "p", "pdf path")
		.required()

	argParser.parse(args)

	if (!Files.exists(Path.of(pdFPath))) {
		System.err.println("$pdFPath not found")
		return
	}

	val application = startKoin {
		modules(extractorModule)
	}

	val extractor = application.koin.get<Extractor>()
	extractor.run(Path.of(pdFPath))
}