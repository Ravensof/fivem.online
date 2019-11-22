package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import removeDir
import unZip
import java.io.File

open class ExtractDependenciesTask : DefaultTask() {

	@InputFile
	lateinit var buildDir: File
	@InputFile
	lateinit var files: Set<File>

	@TaskAction
	fun start() {
		extract(buildDir, files)
	}

	companion object {
		fun extract(buildDir: File, files: Set<File>) {
			removeDir("${buildDir.absolutePath}/lib")

			files.forEach {
				unZip(it.absolutePath, "${buildDir.absolutePath}/lib") {
					it.endsWith(".js") && !it.endsWith(".meta.js") && !it.endsWith(".map.js")
				}
			}
		}
	}
}