package external.danko

external class Base64 {
	companion object {
		fun decode(string: String): String

		fun encode(string: String): String
	}
}