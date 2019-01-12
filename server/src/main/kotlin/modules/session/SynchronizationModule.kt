package online.fivem.server.modules.session

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import online.fivem.common.GlobalConfig
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Console
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.entities.PlayerSrc
import online.fivem.common.events.SynchronizeEvent
import online.fivem.common.events.net.RequestPackEvent
import online.fivem.common.extensions.isNotEmpty
import online.fivem.server.ServerConfig
import online.fivem.server.common.MySQL
import online.fivem.server.modules.basics.MySQLModule
import online.fivem.server.modules.clientEventExchanger.ClientEvent

class SynchronizationModule : AbstractModule() {

	private val requestJob by lazy { requestJob() }
	private val synchronizationJob by lazy { synchronizationJob() }
	private val syncDataChannel = Channel<Pair<PlayerSrc, Array<*>>>(GlobalConfig.MAX_PLAYERS)

	private val sessionModule by moduleLoader.onReady<SessionModule>()

	private lateinit var mySQL: MySQL

	override fun init() {
		ClientEvent.on<SynchronizeEvent> { playerSrc, synchronizeEvent ->
			GlobalScope.launch {
				if (syncDataChannel.isFull) {
					Console.warn("synchronization DataChannel is full")
				}
				syncDataChannel.send(playerSrc to synchronizeEvent.data)
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

	private fun requestJob() = GlobalScope.launch {
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

	private fun synchronizationJob() = GlobalScope.launch {
		for (obj in syncDataChannel) {
			val playerSrc = obj.first

			val player = sessionModule.getPlayer(playerSrc)
				?: return@launch Console.warn("user with playerSrc=${playerSrc.value} not found in session module")

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