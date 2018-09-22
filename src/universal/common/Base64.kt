package universal.common

external object Base64 {
	fun toBase64(string: String): String
	fun fromBase64(string: String): String
}