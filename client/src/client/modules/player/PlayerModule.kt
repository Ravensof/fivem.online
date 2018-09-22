package client.modules.player

import client.common.Player
import client.extensions.orZero
import client.modules.AbstractModule
import client.modules.session.events.SafeEventsReady
import universal.common.Event
import universal.extensions.onNull

class PlayerModule private constructor() : AbstractModule() {

	init {
		Event.on<SafeEventsReady> {
			onSafeEventsReady()
		}

		val ped = Player.getPed().orZero()

//		setInterval(1000) {
//			Console.debug(getEntityForwardVector(ped))
//			Console.debug("${getEntityForwardX(ped)} ${getEntityForwardY(ped)} ${getEntityHeading(ped)}")
//			Console.debug("${getEntityCoords(ped)}")
//		}
	}

	private fun onSafeEventsReady() {
//		setInterval(SAVE_INTERVAL){
//			Player.getPed()?.let {ped->
//				Event.emitSafeNet(PlayerDataSave(
//						coords = Client.getEntityCoords(ped)
//				))
//			}
//		}
	}

	companion object {

		private const val SAVE_INTERVAL = 60_000

		private var instance: PlayerModule? = null

		fun getInstance(): PlayerModule {
			instance.onNull {
				instance = PlayerModule()
			}

			return instance!!
		}
	}
}