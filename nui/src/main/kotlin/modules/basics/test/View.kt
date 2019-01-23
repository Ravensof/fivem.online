package online.fivem.nui.modules.basics.test

import js.externals.jquery.jQuery
import kotlinx.coroutines.await
import online.fivem.common.common.Html
import online.fivem.nui.extensions.nuiResourcesLink
import org.w3c.dom.HTMLElement

open class View {
	open val view: js.externals.jquery.JQuery<HTMLElement> = jQuery("<div class=\"view\"></div>")

	var parent: View? = null
	private val _children = mutableListOf<View>()
	val children: List<View>
		get() {
			return _children
		}

	var isVisible: Boolean
		set(value) {
			if (value)
				view.show()
			else
				view.hide()
		}
		get() {
			return view.`is`(":hidden")
		}

	open fun add(view: View) {
		this.view.append(view.view)
		view.parent = this
		_children.add(view)
	}

//	fun remove(view: View) {
//		view.isVisible = false
//		view.parent = null
//		_children.remove(view)
//
//	}

	fun remove() {
		isVisible = false
		parent = null

		_children.forEach {
			it.remove()
			_children.remove(it)
		}

		view.remove()
	}

	fun getChildren(): List<View> {
		return _children
	}

	suspend fun loadHTML(path: String) {
		val data = jQuery.get(Html.nuiResourcesLink(path)).await() as String
		val jquery = jQuery(data)

		val style = jquery.find("head").find("style").first()
		val body = jquery.find("body").first()
	}
}