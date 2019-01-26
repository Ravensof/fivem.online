package online.fivem.common.external


private interface Extensions {
	fun hex(string: String): String
	fun array(string: String): Array<Int>
	fun digest(string: String): Array<Int>
	//		fun arrayBuffer(string: String): dynamic
	fun hmac(key: String, string: String): String
}

class Sha512(val string: String) {

	override fun toString(): String {
		return sha512(string)
	}

	companion object : Extensions {
		override fun hex(string: String): String = sha512.hex(string)

		override fun array(string: String): Array<Int> = sha512.array(string)

		override fun digest(string: String): Array<Int> = sha512.digest(string)

//			override fun arrayBuffer(string: String): dynamic = sha512.arrayBuffer(string)

		override fun hmac(key: String, string: String): String = sha512.hmac(key, string)
	}
}

class Sha512_256(val string: String) {

	override fun toString(): String {
		return sha512_256(string)
	}

	companion object : Extensions {
		override fun hex(string: String): String = sha512_256.hex(string)

		override fun array(string: String): Array<Int> = sha512_256.array(string)

		override fun digest(string: String): Array<Int> = sha512_256.digest(string)

//			override fun arrayBuffer(string: String): dynamic = sha512_256.arrayBuffer(string)

		override fun hmac(key: String, string: String): String = sha512_256.hmac(key, string)
	}
}

class Sha512_224(val string: String) {

	override fun toString(): String {
		return sha512_224(string)
	}

	companion object : Extensions {
		override fun hex(string: String): String = sha512_224.hex(string)

		override fun array(string: String): Array<Int> = sha512_224.array(string)

		override fun digest(string: String): Array<Int> = sha512_224.digest(string)

//			override fun arrayBuffer(string: String): dynamic = sha512_224.arrayBuffer(string)

		override fun hmac(key: String, string: String): String = sha512_224.hmac(key, string)
	}
}

class Sha384(val string: String) {

	override fun toString(): String {
		return sha384(string)
	}

	companion object : Extensions {
		override fun hex(string: String): String = sha384.hex(string)

		override fun array(string: String): Array<Int> = sha384.array(string)

		override fun digest(string: String): Array<Int> = sha384.digest(string)

//			override fun arrayBuffer(string: String): dynamic = sha384.arrayBuffer(string)

		override fun hmac(key: String, string: String): String = sha384.hmac(key, string)
	}
}

private external val sha512: dynamic
private external val sha384: dynamic
private external val sha512_256: dynamic
private external val sha512_224: dynamic