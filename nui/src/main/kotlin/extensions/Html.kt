package online.fivem.nui.extensions

import online.fivem.common.GlobalConfig
import online.fivem.common.common.Html

fun Html.Companion.nuiLink(string: String): String {
	return GlobalConfig.RESOURCES_HTTP_HOME + string
}