import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

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
		val compileKotlin2Js = getAt("compileKotlin2Js") as Kotlin2JsCompile

		val cleanExternalDependencies = create("cleanExternalDependencies", Delete::class) {
			delete("$buildDir/lib")
		}

		val extractExternalDependencies = create("extractExternalDependencies") {
			dependsOn(cleanExternalDependencies)

			configurations.compile.get().files.forEach {
				unZip(it.absolutePath, "$buildDir/lib") {
					it.endsWith(".js") && !it.endsWith(".meta.js") && !it.endsWith(".map.js")
				}
			}
		}

		val assembleWeb = create("assembleWeb", Sync::class) {
			dependsOn(classes)
			dependsOn(extractExternalDependencies)
		}

		getAt("assemble").dependsOn(assembleWeb)

		val cleanServerCopy = create("cleanServerCopy", Delete::class) {
			delete(Config.serverDir + project.name + "\\lib")
			delete(Config.serverDir + project.name + "\\resources")
			isFollowSymlinks = true
		}

		val debugCopyToServer = create("debugCopyToServer", Copy::class) {
			from(buildDir) {
				include("$buildDir/lib/**.js")
				include("${compileKotlin2Js.destinationDir}/**.js")
				include("resources/**")
			}
			into(Config.serverDir + project.name)
		}

		val copyToServer = create("copyToServer", Copy::class) {

			from(buildDir) {
				include("$buildDir/lib/**.js")
				include("${compileKotlin2Js.destinationDir}/**.js")
				exclude("${compileKotlin2Js.destinationDir}/**.meta.js")
				include("resources/**")
			}

			into(Config.serverDir + project.name)
		}

		val fullBuildAndCopy = create("fullBuildAndCopy") {
			dependsOn(cleanServerCopy)
			dependsOn(assemble)
			dependsOn(copyToServer)
		}

		val debugBuildAndDeploy = create("debugBuildAndDeploy") {
			dependsOn(cleanServerCopy)
			dependsOn(assemble)
			dependsOn(debugCopyToServer)
		}
	}
}