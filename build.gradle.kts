import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

buildscript {
	repositories {
	}

	dependencies {
	}
}

plugins {
	id("kotlin2js").version("1.3.21")
	id("kotlinx-serialization").version("1.3.21")
}

group = "online.fivem"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
	maven { setUrl("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
}

subprojects {

	group = "online.fivem"

	repositories {
		mavenCentral()
		jcenter()
		// artifacts are published to this repository
		maven { setUrl("https://kotlin.bintray.com/kotlinx") }
		maven { setUrl("http://dl.bintray.com/kotlin/kotlinx.html/") }
		maven { setUrl("https://kotlin.bintray.com/js-externals") }

	}

	buildscript {

		repositories {
			mavenCentral()
			jcenter()
		}

		dependencies {
			classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["config.kotlin_version"]}")
			classpath("org.jetbrains.kotlin:kotlin-serialization:${properties["config.kotlin_version"]}")
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

		compile("org.jetbrains.kotlin:kotlin-stdlib-js:${properties["config.kotlin_version"]}")
//		testCompile "org.jetbrains.kotlin:kotlin-test-js:$kotlin_version"
		compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${properties["config.serialization_runtime"]}")
		compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${properties["config.coroutines_version"]}")

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
		val clean = getAt("assemble")
		val compileKotlin2Js = getAt("compileKotlin2Js") as Kotlin2JsCompile

		val assembleWeb = create("assembleWeb", Sync::class) {
			dependsOn(classes)

			configurations.compile.forEach { file ->
				from(zipTree(file.absolutePath), {
					includeEmptyDirs = false
					include { fileTreeElement ->
						val path = fileTreeElement.path
						(path.endsWith(".js") || path.endsWith(".js.map")) && (path.startsWith("META-INF/resources/") ||
								!path.startsWith("META-INF/"))
					}
				})
			}

			from(compileKotlin2Js.destinationDir)
			into("$buildDir/lib")
		}

		getAt("assemble").dependsOn(assembleWeb)

		val cleanServerCopy = create("cleanServerCopy", Delete::class) {
			delete(properties["config.serverDir"].toString() + project.name + "\\lib")
			delete(properties["config.serverDir"].toString() + project.name + "\\resources")
			isFollowSymlinks = true
		}

		clean.dependsOn(cleanServerCopy)

		val debugCopyToServer = create("debugCopyToServer", Copy::class) {
			from(buildDir) {
				include("lib/**")
				include("resources/**")
			}
			into(properties["config.serverDir"].toString() + project.name)
		}

		val copyToServer = create("copyToServer", Copy::class) {

			from(buildDir) {
				include("lib/**.js")
				exclude("lib/**.meta.js")
				include("resources/**")
			}

			into(properties["config.serverDir"].toString() + project.name)
		}

		val fullBuildAndCopy = create("fullBuildAndCopy") {
			dependsOn(clean)
			dependsOn(assemble)
			dependsOn(copyToServer)
		}

		val debugBuildAndDeploy = create("debugBuildAndDeploy") {
			dependsOn(clean)
			dependsOn(assemble)
			dependsOn(debugCopyToServer)
		}
	}
}