package universal.modules.gui.events

import universal.events.IEvent

class WebReceiverReady(
		val moduleFolderName: String,
		val resourcesURL: String
) : IEvent()