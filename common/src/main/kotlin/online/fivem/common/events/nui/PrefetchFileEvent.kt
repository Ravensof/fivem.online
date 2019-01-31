package online.fivem.common.events.nui

class PrefetchFileEvent {
	val files: Array<String>//todo сделать чтобы можно было сериализовать List

	constructor(files: Array<String>) {
		this.files = files
	}

	constructor(file: String) {
		files = arrayOf(file)
	}
}