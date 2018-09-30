package universal.modules.player.events

import universal.events.IEvent
import universal.struct.Coords

class PlayerDataSaveEvent(
		val coords: Coords?
) : IEvent()