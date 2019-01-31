package online.fivem.nui.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.nui.PrefetchFileEvent
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.extensions.prefetch
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class BasicsModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun onInit() {
		ClientEvent.on<PrefetchFileEvent> { Html.prefetch(it.files.map { Html.nuiResourcesLink(it) }) }

		moduleLoader.add(GUIModule(coroutineContext))
		moduleLoader.add(PlaySoundModule())
	}
}