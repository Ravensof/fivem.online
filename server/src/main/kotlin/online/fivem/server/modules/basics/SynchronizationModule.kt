package online.fivem.server.modules.basics

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.common.Event
import online.fivem.common.common.VDate
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.net.ClientSideSynchronizeEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.extensions.isNotEmpty
import online.fivem.common.extensions.orZero
import online.fivem.server.ServerConfig
import online.fivem.server.entities.Player
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.extensions.send
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.client_event_exchanger.ClientEvent
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class SynchronizationModule(override val coroutineContext: CoroutineContext) : AbstractModule() {

	val date = VDate()
	val syncData = ServerSideSynchronizationEvent(serverTime = 0.0)

	private val requestJob by lazy { requestJob() }
	private val synchronizationJob by lazy { synchronizationJob() }
	private val syncDataChannel = Channel<Pair<Player, ClientSideSynchronizeEvent>>(GlobalConfig.MAX_PLAYERS)

	private val sessionModule by moduleLoader.delegate<SessionModule>()

	private val mySQL by moduleLoader.delegate<MySQLModule>()

	override fun onInit() {
		ClientEvent.on<ClientSideSynchronizeEvent> { player, synchronizeEvent ->

			if (syncDataChannel.isFull) {
				Console.warn("synchronization DataChannel is full")
			}

			launch {
				syncDataChannel.send(player to synchronizeEvent)
			}
		}

		Event.on<PlayerConnectedEvent> { syncDataFor(it.player.playerSrc) }
	}

	@ExperimentalCoroutinesApi
	override fun onStart(): Job? {
		synchronizationJob.start()
		requestJob.start()

		return super.onStart()
	}

	override fun onStop(): Job? {
		requestJob.cancel()
		synchronizationJob.cancel()

		return null
//		Console.info("saving player data")
//
//		return launch {
//			val players = sessionModule.getConnectedPlayers()
//
//			val syncJob = launch {
//				withTimeoutOrNull(15_000) {
//					repeat(players.size) {
//						val data = syncDataChannel.receive()
//						saveData(data.first, data.second)
//					}
//				}
//			}
//
//			for (playerSrc in players) {
//				syncDataFor(playerSrc)
//			}
//
//			syncJob.join()
//		}
	}

	private fun syncDataFor(playerSrc: PlayerSrc) {
		syncData.serverTime = Date.now() + Natives.getPlayerPing(playerSrc).orZero()
		ClientEvent.emit(syncData, playerSrc)
	}

	private fun requestJob() = launch {
		while (isActive) {
			val players = sessionModule.getConnectedPlayers()

			if (!players.isNotEmpty()) {
				delay(SYNCHRONIZATION_PERIOD)
				continue
			}

			for (playerSrc in players) {
				syncDataFor(playerSrc)
				delay(SYNCHRONIZATION_PERIOD / players.size)
			}
		}
	}

	private fun synchronizationJob() = launch {
		for (obj in syncDataChannel) {
			saveData(obj.first, obj.second)
		}
	}

	private suspend fun saveData(player: Player, data: ClientSideSynchronizeEvent) {

		data.coordinatesX?.let {
			mySQL.connection.send(
				"""UPDATE characters
							|SET
							|   coord_x=?,
							|   coord_y=?,
							|   coord_z=?,
							|   coord_rotation=?
							|WHERE id=?
							|LIMIT 1
						""".trimMargin(),
				arrayOf(
					it.x,
					it.y,
					it.z,
					it.rotation,
					player.characterId
				)
			)
		}
	}

	companion object {
		const val SYNCHRONIZATION_PERIOD: Long = 1_000L * ServerConfig.SYNCHRONIZATION_PERIOD_SECONDS
	}
}