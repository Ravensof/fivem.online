package online.fivem.nui.extensions

import js.externals.jquery.JQuery
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

fun JQuery<HTMLElement>.toHTMLImageElement(): HTMLImageElement {
	return get().first() as HTMLImageElement
}

fun JQuery<HTMLElement>.toHTMLCanvasElement(): HTMLCanvasElement {
	return get().first() as HTMLCanvasElement
}