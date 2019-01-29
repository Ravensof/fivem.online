package online.fivem.common.events.net

class PrefetchFileEvent {
	val files: List<String>//todo проверить, list может не сериализоваться

	constructor(files: List<String>) {
		this.files = files
	}

	constructor(file: String) {
		files = listOf(file)
	}
}