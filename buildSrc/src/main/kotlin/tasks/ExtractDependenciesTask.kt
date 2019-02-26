package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import removeDir
import unZip
import java.io.File

open class ExtractDependenciesTask : DefaultTask() {

	@Input
	val buildDir = project.objects.property<File>()
	@Input
	val files = project.objects.property<Set<File>>()

	@TaskAction
	fun start() {
		extract(buildDir.get(), files.get())
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