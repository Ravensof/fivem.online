package online.fivem.common.common

external class Base64 {
	companion object {
		fun decode(string: String): String

		fun encode(string: String): String
	}
}