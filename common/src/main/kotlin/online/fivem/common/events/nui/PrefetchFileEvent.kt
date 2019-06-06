package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class PrefetchFileEvent(val files: List<String>) : Serializable() {

	constructor(file: String) : this(listOf(file))
}