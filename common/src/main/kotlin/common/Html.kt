package online.fivem.common.common

class Html {
	companion object {
		fun escape(text: String): String {

			var text = text

			val map = mapOf(
				"&" to "&amp;",
				"<" to "&lt;",
				">" to "&gt;",
				"\"" to "&quot;",
				"'" to "&#039;"
			)

			map.forEach {
				text = text.replace(it.key, it.value)
			}

			return text
		}

		fun urlEncode(string: String): String {
			return encodeURIComponent(string)
		}
	}
}

private external fun encodeURIComponent(str: Any): String