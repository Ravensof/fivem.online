package online.fivem.nui.modules.mobile_phone.apps

import js.externals.jquery.JQuery
import kotlinx.coroutines.async
import online.fivem.nui.common.AbstractNuiModule
import org.w3c.dom.HTMLElement

//todo переделать в просто Browser и передавать viewContainer
class MobilePhoneBrowserModule(
	private val mobilePhoneModule: MobilePhoneModule
) : AbstractNuiModule() {

	private lateinit var window: JQuery<HTMLElement>
	var browserContainer: JQuery<HTMLElement>? = null
		set(value) {
			field = value

			window = value?.find("#browser_screen")?.contents() ?: return

			value.hide()
		}

	override fun onStartAsync() = async {

	}
}