package online.fivem.nui.modules.loadingScreen

import js.externals.jquery.jQuery
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.common.events.HideLoadingScreen
import online.fivem.nui.modules.clientEventEchanger.ClientEvent

class LoadingScreenModule : AbstractModule() {
	private val loadingScreenBlock = jQuery("#loadingScreen")

	override fun start(): Job? {
		ClientEvent.on<HideLoadingScreen> { loadingScreenBlock.hide() }

		return super.start()
	}
}