package web.extensions

import js.externals.jquery.JQuery
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

fun JQuery<HTMLElement>.toHTMLImageElement(): HTMLImageElement? {
	return this.get().first() as HTMLImageElement
}

fun JQuery<HTMLElement>.toHTMLCanvasElement(): HTMLCanvasElement? {
	return this.get().first() as HTMLCanvasElement
}
