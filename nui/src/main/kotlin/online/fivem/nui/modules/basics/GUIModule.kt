package online.fivem.nui.modules.basics

import kotlinx.coroutines.CoroutineScope
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.nui.ShowGuiEvent
import online.fivem.nui.modules.basics.test.BlackScreenModule
import online.fivem.nui.modules.basics.test.MainView
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class GUIModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val mainView = MainView()

	override fun onInit() {
		ClientEvent.on<ShowGuiEvent> { onShowGui(it.show) }
		moduleLoader.add(DebugModule(coroutineContext, mainView))
		moduleLoader.add(BlackScreenModule(coroutineContext))
	}

	private fun onShowGui(show: Boolean) {
		if (show) {
			mainView.view.fadeIn()
		} else {
			mainView.view.fadeOut()
		}
	}
}