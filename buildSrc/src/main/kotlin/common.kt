import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile

fun unZip(from: String, toDir: String, filter: (String) -> Boolean = { it.endsWith(".js") }) {
	ZipFile(from).use { zip ->

		zip.entries().asSequence().forEach { entry ->
			if (entry.isDirectory) return@forEach
			if (!filter(entry.name)) return@forEach

			File(toDir + "/" + entry.name).let { file ->

				File(file.absolutePath.removeSuffix(file.name)).mkdirs()

				zip.getInputStream(entry).use { input ->
					file.outputStream().use { output ->
						input.copyTo(output)
					}
				}
			}
		}
	}
}

fun removeDir(path: String) {
	if (!File(path).exists()) return

	val pathToBeDeleted = Path.of(path)

	Files.walk(pathToBeDeleted)
		.sorted(Comparator.reverseOrder())
		.map(Path::toFile)
		.forEach(File::delete)
}