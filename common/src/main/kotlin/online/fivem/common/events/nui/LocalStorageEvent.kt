package online.fivem.common.events.nui

import kotlinx.serialization.Serializable

sealed class LocalStorageEvent {
	@Serializable
	class Post(
		val key: String,
		val value: String
	)

	@Serializable
	class Request(
		val requestId: Int,
		val key: String
	)

	@Serializable
	class Response(
		val responseId: Int,
		val data: String?
	)
}