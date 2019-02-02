package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

@Serializable
class PrefetchFileEvent {
	val files: List<String>

	constructor(files: List<String>) {
		this.files = files
	}

	constructor(file: String) {
		files = listOf(file)
	}
}