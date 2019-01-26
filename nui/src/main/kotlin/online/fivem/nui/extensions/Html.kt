package online.fivem.nui.extensions

import js.externals.jquery.jQuery
import online.fivem.common.common.Html
import org.w3c.dom.HTMLImageElement

fun Html.Companion.nuiResourcesLink(link: String): String {
	return getResourceLink("nui", link)
}

fun Html.Companion.prefetch(links: List<String>) {
	jQuery("html").find("head").append(
		links.joinToString { "<link rel=\"prefetch\" href=\"${escape(it)}\"/>" }
	)
}

fun Html.Companion.prefetch(link: String) {
	jQuery("html").find("head").append("<link rel=\"prefetch\" href=\"${escape(link)}\"/>")
}

fun Html.Companion.getImage(path: String): HTMLImageElement {
	val image = jQuery("<img src=\"$path\"/>").first() as HTMLImageElement
	if (image.width == 0) throw Exception("no image found at path $path")
	return image
}