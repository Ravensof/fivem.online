package universal.struct

class CustomHttpResponse<T>(
		val code: Int,
		val response: T
)