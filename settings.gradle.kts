pluginManagement {
	resolutionStrategy.eachPlugin {
		when (requested.id.id) {

			"kotlin2js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")

			"kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
		}
	}
}
rootProject.name = "gtav"

include(":client", ":server", ":nui", ":common", ":loadingScreen", ":client_natives", ":server_natives")