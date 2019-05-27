package online.fivem.nui.modules.basics

import online.fivem.common.common.Html
import online.fivem.common.events.nui.PrefetchFileEvent
import online.fivem.nui.common.AbstractNuiModule
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.extensions.prefetch
import online.fivem.nui.modules.client_event_exchanger.ClientEvent

class BasicsModule : AbstractNuiModule() {

	override suspend fun onInit() {

		ClientEvent.on<PrefetchFileEvent> {
			Html.prefetch(it.files.map {
				Html.nuiResourcesLink(it)
			})
		}

		moduleLoader.add(GUIModule(coroutineContext))
		moduleLoader.add(PlaySoundModule(coroutineContext))
		moduleLoader.add(LocalStorageModule(coroutineContext))
	}
}