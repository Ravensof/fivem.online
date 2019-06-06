package online.fivem.common.events.nui

import online.fivem.common.other.Serializable

sealed class WebStorageEvent : Serializable() {

	@kotlinx.serialization.Serializable
	class Post(
		val key: String,
		val value: String,
		val eventId: Int
	) : WebStorageEvent()

	@kotlinx.serialization.Serializable
	class Request(
		val requestId: Int,
		val key: String
	) : WebStorageEvent()

	@kotlinx.serialization.Serializable
	class Response(
		val responseId: Int,
		val data: String?
	) : WebStorageEvent()
}