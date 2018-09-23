package universal.modules.player.events

import universal.struct.Coords

data class PlayerDataSaveEvent(
		val coords: Coords?
)