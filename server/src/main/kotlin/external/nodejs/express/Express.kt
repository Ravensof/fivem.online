package external.nodejs.express

external interface Express {
	fun static(path: String): (Request, Response, () -> Unit) -> Unit
}

fun Express.getInstance(): App {
	return asDynamic()().unsafeCast<App>()
}