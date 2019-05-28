package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

sealed class WebStorageEvent : Serializable() {

	class Post(
		val key: String,
		val value: String,
		val eventId: Int
	) : WebStorageEvent()

	class Request(
		val requestId: Int,
		val key: String
	) : WebStorageEvent()

	class Response(
		val responseId: Int,
		val data: String?
	) : WebStorageEvent()
}