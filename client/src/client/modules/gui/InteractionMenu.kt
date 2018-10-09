package client.modules.gui

import universal.modules.gui.ButtonItem
import universal.modules.gui.MenuItem

class InteractionMenu {


	private val menuInteracter = MenuInteracter()

	init {

		val menu = MenuItem(
				title = "Test Menu",
				description = null,
				items = arrayOf(
						ButtonItem(
								title = "Item 1",
								description = null,
								name = "test1"
						),
						ButtonItem(
								title = "Item 2",
								description = null,
								name = "test2"
						),
						ButtonItem(
								title = "Item 3",
								description = null,
								name = "test3"
						),
						ButtonItem(
								title = "Item 4",
								description = null,
								name = "test4"
						),
						ButtonItem(
								title = "Item 5",
								description = null,
								name = "test5"
						),
						ButtonItem(
								title = "Item 6",
								description = null,
								name = "test6"
						),
						ButtonItem(
								title = "Item 7",
								description = null,
								name = "test7"
						)
				)
		)

		menuInteracter.menuItem = menu
	}

	fun show() {
		menuInteracter.show()
	}

	fun close() {
		menuInteracter.close()
	}
}