package universal.modules.gui

import universal.events.IEvent

abstract class Item(
		val name: String,
		val value: Any? = null
) : IEvent()

abstract class ViewItem(
		val title: String,
		val description: String? = null,

		name: String,
		value: Any? = null) : Item(name, value)

class MenuItem(
		title: String,
		description: String? = null,

		val items: Array<ViewItem> = arrayOf()
) : ViewItem(title, description, "") {
	fun countItems() = items.count()
}

class ButtonItem(
		title: String,
		description: String? = null,

		name: String
) : ViewItem(title, description, name)

class CheckBoxItem(
		title: String,
		description: String? = null,

		name: String,
		value: Boolean = false
) : ViewItem(title, description, name, value)

class RadioItem(
		title: String,
		description: String? = null,

		name: String,
		value: Int = -1,

		val values: Array<Pair<String, String>> = arrayOf()
) : ViewItem(title, description, name, value)

class TextInputItem(
		title: String,
		description: String? = null,

		name: String,
		value: String = ""
) : ViewItem(title, description, name, value)

class ColorPicker(
		title: String,
		description: String? = null,

		name: String,
		value: Int = -1,

		val colors: Array<Int> = arrayOf()
) : ViewItem(title, description, name, value)