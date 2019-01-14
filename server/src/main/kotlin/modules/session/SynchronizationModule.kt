package online.fivem.server.modules.session

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.SynchronizeEvent
import online.fivem.common.events.net.RequestPackEvent
import online.fivem.common.extensions.isNotEmpty
import online.fivem.server.ServerConfig
import online.fivem.server.common.MySQL
import online.fivem.server.entities.Player
import online.fivem.server.modules.basics.MySQLModule
import online.fivem.server.modules.clientEventExchanger.ClientEvent
import kotlin.coroutines.CoroutineContext

class SynchronizationModule : AbstractModule(), CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()

	private val requestJob by lazy { requestJob() }
	private val synchronizationJob by lazy { synchronizationJob() }
	private val syncDataChannel = Channel<Pair<Player, Array<*>>>(GlobalConfig.MAX_PLAYERS)

	private val sessionModule by moduleLoader.onReady<SessionModule>()

	private lateinit var mySQL: MySQL

	override fun init() {
		ClientEvent.on<SynchronizeEvent> { playerSrc, synchronizeEvent ->
			launch {
				if (syncDataChannel.isFull) {
					Console.warn("synchronization DataChannel is full")
				}
				val player = sessionModule.getPlayer(playerSrc)
					?: return@launch Console.warn("user with playerSrc=${playerSrc.value} not found in session module")

				syncDataChannel.send(player to synchronizeEvent.data)
			}
		}
		moduleLoader.on<MySQLModule> { mySQL = it.mySQL }
	}

	@ExperimentalCoroutinesApi
	override fun start(): Job? {
		synchronizationJob.start()
		requestJob.start()

		return super.start()
	}

	override fun stop(): Job? {
		requestJob.cancel()
		synchronizationJob.cancel()
		mySQL.close()

		return super.stop()
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

			obj.second.forEach { data ->
				when (data) {
					is CoordinatesX -> {
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
							data.x,
							data.y,
							data.z,
							data.rotation,
							player.characterId
						)
					}
				}
			}
		}
	}

	companion object {

		private val syncList = arrayOf(
			CoordinatesX::class.simpleName!!
		)

		const val SYNCHRONIZATION_PERIOD: Long = 1_000L * ServerConfig.SYNCHRONIZATION_PERIOD_SECONDS
	}
}