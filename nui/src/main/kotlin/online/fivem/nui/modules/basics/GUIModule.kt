package online.fivem.nui.modules.basics

import js.externals.jquery.jQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import online.fivem.common.common.Html
import online.fivem.common.events.nui.ShowGuiEvent
import online.fivem.common.extensions.forEach
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.modules.basics.test.BlackScreenModule
import online.fivem.nui.modules.basics.test.MainView
import online.fivem.nui.modules.client_event_exchanger.ClientEvent

class GUIModule : AbstractNuiModule() {

	lateinit var mainView: MainView
		private set

	private val content = jQuery("#content")

	private val iframeLoading = async {
		val channel = Channel<Unit>()

		iframeLoadedListener = {
			channel.close()
		}

		content.attr("src", interfaceFile)

		channel.forEach { }

		return@async MainView(content.contents().find("body"))
	}

	override suspend fun onInit() {
		val mainView = iframeLoading.await()
		this.mainView = mainView

		moduleLoader.add(DebugModule(coroutineContext, mainView))
		moduleLoader.add(BlackScreenModule(coroutineContext))

		ClientEvent.on<ShowGuiEvent> { onShowGui(it.show) }
	}

	private fun onShowGui(show: Boolean) {
		if (show) {
			content.fadeIn()
		} else {
			content.fadeOut()
		}
	}

	private companion object {
		val interfaceFile = Html.nuiResourcesLink("interface.html")
	}
}

private external var iframeLoadedListener: () -> Unit