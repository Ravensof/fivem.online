package online.fivem.nui.common

import js.externals.jquery.jQuery
import kotlinx.coroutines.await
import online.fivem.common.common.Html
import online.fivem.nui.extensions.nuiResourcesLink
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document

open class View {
	open val view: js.externals.jquery.JQuery<HTMLElement> = jQuery("<div class=\"view\"></div>")

	var parent: View? = null

	val children: List<View>
		get() {
			return _children
		}

	var isVisible: Boolean
		set(value) {
			if (value) {
				onShow()
				view.show()
			} else {
				view.hide()
				onHide()
			}
		}
		get() {
			return view.`is`(":hidden")
		}

	protected val canvas: HTMLCanvasElement by lazy {
		val canvas = document.createElement("canvas") as HTMLCanvasElement
		view.append(canvas)
		canvas
	}

	protected val context2D: CanvasRenderingContext2D by lazy { canvas.getContext("2d") as CanvasRenderingContext2D }

	private val _children = mutableListOf<View>()

	open fun add(view: View) {
		this.view.append(view.view)
		view.parent = this
		_children.add(view)
	}

	protected open fun onShow() {}

	protected open fun onHide() {}

	protected open fun onRemove() {}

	fun remove() {
		isVisible = false
		parent = null

		onRemove()

		children.forEach {
			_children.remove(it)
			it.remove()
		}

		view.remove()
	}

	private fun doOnChild(function: (View) -> Unit) {
		children.forEach {
			function(it)
		}
	}

	suspend fun loadHTML(path: String) {
		val data = jQuery.get(Html.nuiResourcesLink(path)).await() as String
		val jquery = jQuery(data)

		val style = jquery.find("head").find("style").first()
		val body = jquery.find("body").first()
	}
}