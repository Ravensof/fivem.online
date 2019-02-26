import java.io.File
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