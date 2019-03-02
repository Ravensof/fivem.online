import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import tasks.ExtractDependenciesTask
import tasks.UploadingToServerTask

buildscript {
	repositories {
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:" + Config.kotlinVersion)
	}
}

plugins {
	id("kotlin2js").version(Config.kotlinVersion)
	id("kotlinx-serialization").version(Config.kotlinVersion)
}

group = "online.fivem"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
	maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
}

subprojects {

	group = "online.fivem"

	repositories {
		mavenCentral()
		jcenter()
		// artifacts are published to this repository
		maven(url = "https://kotlin.bintray.com/kotlinx")
		maven(url = "http://dl.bintray.com/kotlin/kotlinx.html/")
		maven(url = "https://kotlin.bintray.com/js-externals")

	}

	buildscript {

		repositories {
			mavenCentral()
			jcenter()
		}

		dependencies {
			classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:" + Config.kotlinVersion)
			classpath("org.jetbrains.kotlin:kotlin-serialization:" + Config.kotlinVersion)
		}
	}

	apply("plugin" to "kotlin2js")
	apply("plugin" to "kotlinx-serialization")
	apply("plugin" to "kotlin-platform-js")

	(tasks["compileKotlin2Js"] as Kotlin2JsCompile).apply {
		kotlinOptions.moduleKind = "umd"
		kotlinOptions.sourceMap = true
	}

	configurations {
		getAt("compile").setTransitive(false)
	}

	dependencies {

		compile("org.jetbrains.kotlin:kotlin-stdlib-js:" + Config.kotlinVersion)
//		testCompile "org.jetbrains.kotlin:kotlin-test-js:$kotlinVersion"
		compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:" + Config.serializationRuntime)
		compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:" + Config.coroutinesVersion)

		when (project.name) {
			"common" -> {
			}

			"nui", "loadingScreen" -> {
				implementation("org.jetbrains.kotlinx:kotlinx-html-common:0.6.11")
				compile("org.jetbrains.kotlinx:kotlinx-html-js:0.6.1")
				compile("kotlin.js.externals:kotlin-js-jquery:3.2.0-0")
			}

			else -> {
			}
		}
	}

	tasks {
		val assemble = getAt("assemble")
		val classes = getAt("classes")
		val clean = getAt("clean")
//		val compileKotlin2Js = getAt("compileKotlin2Js") as Kotlin2JsCompile

		ExtractDependenciesTask.extract(buildDir, configurations.compile.get().files)

		val extractDependencies by registering(ExtractDependenciesTask::class) {
			dependsOn(clean)
			buildDir.set(this@subprojects.buildDir)
			files.set(configurations.compile.get().files)
		}

		assemble.dependsOn(classes)

		val copyToServer by registering(UploadingToServerTask::class) {
			dependsOn(assemble)
			moduleName.set(project.name)
			buildDir.set(this@subprojects.buildDir)
		}

		val fullBuildAndCopy by registering(Copy::class) {
			dependsOn(copyToServer)
		}
	}
}