package online.fivem.common.common

import online.fivem.common.GlobalConfig

class Html {
	companion object {
		fun escape(text: String): String {

			var result = text

			val map = mapOf(
				"&" to "&amp;",
				"<" to "&lt;",
				">" to "&gt;",
				"\"" to "&quot;",
				"'" to "&#039;"
			)

			map.forEach {
				result = result.replace(it.key, it.value)
			}

			return result
		}

		fun urlEncode(string: String): String {
			return encodeURIComponent(string)
		}

		fun getResourceLink(type: String, link: String): String {
			return GlobalConfig.RESOURCES_HTTP_HOME + "$type/resources/main/" + link
		}
	}
}

private external fun encodeURIComponent(str: Any): String