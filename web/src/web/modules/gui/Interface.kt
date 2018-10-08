package web.modules.gui

import RESOURCES_URL
import js.externals.jquery.JQuery
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement
import universal.common.clearTimeout
import universal.common.escapeHtml
import universal.common.setTimeout
import universal.modules.gui.MenuItem
import universal.modules.gui.events.CompoundMenuCloseEvent
import universal.modules.gui.events.CompoundMenuFocusItemEvent
import universal.modules.gui.events.CompoundMenuShowEvent
import web.common.Event

class Interface(bodyBlock: JQuery<HTMLElement>) {

	private val body = bodyBlock

	private var menuContainer =
			jQuery("""
		<div class="vertical-menu">
		</div>
	""".trimIndent())

	private var itemContainer = jQuery("""
				<ul class="vertical-menu-frame">
				</ul>
		""".trimIndent())

	init {
		Event.onNui<CompoundMenuShowEvent> { onShowMenu(it.menu) }
		Event.onNui<CompoundMenuCloseEvent> { onHideMenu() }
		Event.onNui<CompoundMenuFocusItemEvent> { onFocusItem(it.index) }

		body.append("<link rel=\"stylesheet\" href=\"$RESOURCES_URL/Menu/style.css\">")

		body.append(menuContainer)
		menuContainer.hide()
	}

	private fun setMenu(menuItem: MenuItem) {

		val menuHeader = jQuery("""
			<div class="vertical-menu-header">${escapeHtml(menuItem.title)}</div>
		""".trimIndent())

		itemContainer = jQuery("""
				<ul class="vertical-menu-frame"></ul>
		""".trimIndent())

		menuItem.items.forEach {
			itemContainer.append("""
				<li class="vertical-menu-item"><a href="">${escapeHtml(it.title)}</a></li>
			""".trimIndent())
		}

		menuContainer.html("")
		menuContainer.append(menuHeader)
		menuContainer.append(itemContainer)

		menuContainer.show()
	}

	private var timeoutId: dynamic = null
	private var selectedIndex = 0

	private fun onFocusItem(index: Int) {
		clearTimeout(timeoutId)

		val animationTime = 500
		val item = itemContainer.children().eq(index)

		itemContainer.children().attr("class", "vertical-menu-item")

		item.attr("class", "vertical-menu-item vertical-menu-item-active")

		timeoutId = setTimeout(animationTime / 2) {
			item.attr("class", "vertical-menu-item vertical-menu-item-focus")
		}

		if (selectedIndex != index) {
			itemContainer.stop().asDynamic()
					.animate(
							object {
								val scrollTop = item.position().top
							},
							animationTime,
							"swing"
					)
		}
		selectedIndex = index
	}

	private fun onShowMenu(menu: MenuItem) {
		setMenu(menu)
	}

	private fun onHideMenu() {
		menuContainer.hide()
	}
}