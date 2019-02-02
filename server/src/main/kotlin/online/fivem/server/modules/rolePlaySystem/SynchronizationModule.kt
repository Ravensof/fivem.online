package online.fivem.server.modules.rolePlaySystem

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.RequestPackEvent
import online.fivem.common.events.net.SynchronizeEvent
import online.fivem.common.extensions.isNotEmpty
import online.fivem.server.ServerConfig
import online.fivem.server.common.MySQL
import online.fivem.server.entities.Player
import online.fivem.server.modules.basics.MySQLModule
import online.fivem.server.modules.basics.SessionModule
import online.fivem.server.modules.clientEventExchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class SynchronizationModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val requestJob by lazy { requestJob() }
	private val synchronizationJob by lazy { synchronizationJob() }
	private val syncDataChannel = Channel<Pair<Player, SynchronizeEvent>>(GlobalConfig.MAX_PLAYERS)

	private val sessionModule by moduleLoader.onReady<SessionModule>()

	private lateinit var mySQL: MySQL

	override fun onInit() {
		ClientEvent.on<SynchronizeEvent> { playerSrc, synchronizeEvent ->
			launch {
				if (syncDataChannel.isFull) {
					Console.warn("synchronization DataChannel is full")
				}
				val player = sessionModule.getPlayer(playerSrc)
					?: return@launch Console.warn("user with playerSrc=${playerSrc.value} not found in session module")

				syncDataChannel.send(player to synchronizeEvent)
			}
		}
		moduleLoader.on<MySQLModule> { mySQL = it.mySQL }
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
		mySQL.close()

		return super.onStop()
	}

	private fun requestJob() = launch {
		while (isActive) {
			val players = sessionModule.getConnectedPlayers()

			if (!players.isNotEmpty()) {
				delay(SYNCHRONIZATION_PERIOD)
				continue
			}

			val requestPackEvent = RequestPackEvent(syncList)

			for (playerSrc in players) {
				ClientEvent.emit(requestPackEvent, playerSrc)
				delay(SYNCHRONIZATION_PERIOD / players.size)
			}
		}
	}

	private fun synchronizationJob() = launch {
		for (obj in syncDataChannel) {
			val player = obj.first
			val data = obj.second

			data.coordinatesX?.let {
				mySQL.send(
					"""UPDATE characters
							|SET
							|   coord_x=?,
							|   coord_y=?,
							|   coord_z=?,
							|   coord_rotation=?
							|WHERE id=?
							|LIMIT 1
						""".trimMargin(),
					it.x,
					it.y,
					it.z,
					it.rotation,
					player.characterId
				)
			}
		}
	}

	companion object {

		private val syncList = listOf(
			CoordinatesX::class.simpleName!!
		)

		const val SYNCHRONIZATION_PERIOD: Long = 1_000L * ServerConfig.SYNCHRONIZATION_PERIOD_SECONDS
	}
}