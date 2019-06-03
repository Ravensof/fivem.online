package online.fivem.nui.modules.mobile_phone

import js.externals.jquery.JQuery
import kotlinx.coroutines.launch
import online.fivem.nui.common.AbstractNuiModule
import org.w3c.dom.HTMLElement

//todo переделать в просто Browser и передавать viewContainer
class MobilePhoneBrowserModule(
	private val mobilePhoneModule: MobilePhoneModule
) : AbstractNuiModule() {

	private lateinit var window: JQuery<HTMLElement>
	private lateinit var frame: JQuery<HTMLElement>

	override fun onStart() = launch {
		mobilePhoneModule.waitForStart()

		frame = mobilePhoneModule.browserFrame
		window = frame.find("#browser_window").contents()

		frame.hide()
	}
}