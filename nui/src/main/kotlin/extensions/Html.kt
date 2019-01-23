package online.fivem.nui.extensions

import js.externals.jquery.JQuery.jqXHR
import js.externals.jquery.jQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
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

fun Html.Companion.loadJS(jsFile: String): Job {
	val waitingForRequest = Channel<Boolean>()

	return GlobalScope.launch {
		jQuery.getScript(
			jsFile,
			object : jqXHR.DoneCallback<String?, jqXHR<String>> {
				override fun invoke(t: String?, u: dynamic, v: jqXHR<String>, vararg r: Any?) {
					launch {
						waitingForRequest.send(true)
					}
				}
			}
		)

		waitingForRequest.receive()
		waitingForRequest.close()
	}
}