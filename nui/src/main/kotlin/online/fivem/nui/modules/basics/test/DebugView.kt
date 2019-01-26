package online.fivem.nui.modules.basics.test

import online.fivem.nui.common.View

class DebugView(val id: Int) : View() {
	init {
		view.attr(
			"style", """
			max-width: 300px;
			min-width: 100px;
			padding: 15px;
			font-family: Arial, Helvetica, sans-serif;
			font-size: 14px;
			color: #ffffff;
			background: rgba(0, 0, 0, 0.80);
		""".trimIndent()
		)
	}

	var text: String
		set(value) {
			view.text(value)
		}
		get() {
			return view.text()
		}
}