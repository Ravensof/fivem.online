package external.nodejs.stream

external interface Stream {
	fun <T> Transform(transformParams: TransformParams<T>): Transform
}

external interface Transform

class TransformParams<T>(
	val objectMode: Boolean = true,
	val transform: (T, String, () -> Unit) -> Unit
)