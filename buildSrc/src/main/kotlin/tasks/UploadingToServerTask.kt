package tasks

import Config
import org.apache.commons.net.ftp.FTPClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path

open class UploadingToServerTask : DefaultTask() {

	var moduleName = ""

	lateinit var buildDir: File

	lateinit var projectResourcesDir: File

	private val client = FTPClient()

	private lateinit var rootDir: String

	@TaskAction
	fun start() {
		if (!this::buildDir.isInitialized || !this::projectResourcesDir.isInitialized || !buildDir.exists()) return

		rootDir = "${Config.Ftp.root}$moduleName"

		try {
			client.connect(Config.Ftp.host)
			client.login(Config.Ftp.user, Config.Ftp.password)
			client.setFileType(FTPClient.BINARY_FILE_TYPE)

			client.removeDirectory("$rootDir/lib")

			uploadToLibsRoot(buildDir.absolutePath + "/lib")
			uploadToLibsRoot(buildDir.absolutePath + "/classes")
			uploadResources()
			uploadBuildSrcResources()

			client.logout()
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			try {
				client.disconnect()
			} catch (e: Throwable) {
				e.printStackTrace()
			}
		}
	}

	private fun uploadFile(file: File, destination: String) {
		val fis = FileInputStream(file)

		client.makeDirectory(destination.removeSuffix(file.name))

		if (!client.storeFile(destination, fis)) {
			println(client.replyString)
		}

		fis.close()
	}

	private fun uploadToLibsRoot(path: String) {
		if (!File(path).exists()) return

		Files.walk(Path.of(path))
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.filter { !it.isDirectory }
			.filter { it.name.endsWith(".js") }// && !it.name.endsWith(".meta.js") && !it.name.endsWith(".map.js") }
			.forEach {
				uploadFile(it, "$rootDir/lib/${it.name}")
			}
	}

	private fun uploadResources() {
		client.removeDirectory("$rootDir/resources")

		Files.walk(Path.of(buildDir.absolutePath + "/resources"))
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.filter { !it.isDirectory }
			.forEach {
				val remotePath = it.absolutePath.removePrefix(buildDir.absolutePath)

				uploadFile(
					it,
					"$rootDir$remotePath"
				)
			}
	}

	//todo сделать, чтобы не выполнялось каждый раз для каждого модуля
	private fun uploadBuildSrcResources() {
		Files.walk(Path.of(projectResourcesDir.absolutePath))
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.filter { !it.isDirectory }
			.forEach {
				val remotePath = it.absolutePath.removePrefix(projectResourcesDir.absolutePath)

				uploadFile(
					it,
					"${Config.Ftp.root}$remotePath"
				)
			}
	}

}