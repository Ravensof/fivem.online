package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

sealed class LocalStorageEvent : Serializable() {

	class Post(
		val key: String,
		val value: String
	) : LocalStorageEvent()

	class Request(
		val requestId: Int,
		val key: String
	) : LocalStorageEvent()

	class Response(
		val responseId: Int,
		val data: String?
	) : LocalStorageEvent()
}