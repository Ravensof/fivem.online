package server.modules.player

import server.modules.AbstractModule
import server.modules.session.extensions.onSafeNet
import server.structs.PlayerSrc
import universal.common.Event
import universal.extensions.onNull
import universal.modules.player.struct.PlayerDataSave


class PlayerModule private constructor() : AbstractModule() {

	init {
		Event.onSafeNet { playerSrc: PlayerSrc, data: PlayerDataSave ->

		}
	}

	companion object {
		private var instance: PlayerModule? = null

		fun getInstance(): PlayerModule {
			instance.onNull {
				instance = PlayerModule()
			}

			return instance!!
		}
	}
}