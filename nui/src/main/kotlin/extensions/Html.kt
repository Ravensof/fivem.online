package online.fivem.nui.extensions

import js.externals.jquery.jQuery
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Html

fun Html.Companion.nuiResourcesLink(link: String): String {
	return getResourceLink("nui", link)
}

fun Html.Companion.getResourceLink(type: String, link: String): String {
	return GlobalConfig.RESOURCES_HTTP_HOME + "$type/resources/main/" + link
}

fun Html.Companion.prefetch(links: List<String>) {
	links.forEach { prefetch(it) }
}

fun Html.Companion.prefetch(link: String) {
	jQuery("html").find("head").append("<link rel=\"prefetch\" href=\"${Html.escape(link)}\"/>")
}