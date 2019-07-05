package online.fivem.server.modules.basics

import external.nodejs.mysql.Pool
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.common.Event
import online.fivem.common.events.net.ClientSideSynchronizationEvent
import online.fivem.common.events.net.ServerSideSynchronizationEvent
import online.fivem.common.extensions.repeatJob
import online.fivem.server.ServerConfig
import online.fivem.server.common.AbstractServerModule
import online.fivem.server.entities.Player
import online.fivem.server.entities.PlayerSrc
import online.fivem.server.events.PlayerConnectedEvent
import online.fivem.server.gtav.Natives
import online.fivem.server.modules.basics.mysql.MySQLModule
import online.fivem.server.modules.client_event_exchanger.ClientEvent
import kotlin.js.Date

class SynchronizationModule(
	private val sessionModule: SessionModule,
	private val mySQLModule: MySQLModule
) : AbstractServerModule() {

	private var syncData = ServerSideSynchronizationEvent(serverTime = 0.0)

	private val syncDataChannel = Channel<Pair<Player, ClientSideSynchronizationEvent>>(GlobalConfig.MAX_PLAYERS)

	private lateinit var mySQL: Pool

	init {
		ClientEvent.on<ClientSideSynchronizationEvent> { player, synchronizeEvent ->
			syncDataChannel.send(player to synchronizeEvent)
		}

		Event.on<PlayerConnectedEvent> { syncDataFor(it.player.playerSrc) }
	}

	override fun onStartAsync() = async {
		sessionModule.waitForStart()
		mySQLModule.waitForStart()

		mySQL = mySQLModule.pool

		startPlayerDataListener()
		startSynchronize()
	}

	private fun syncDataFor(playerSrc: PlayerSrc) = launch {
		syncData.serverTime = Date.now() + Natives.getPlayerPing(playerSrc)
		ClientEvent.emit(syncData, playerSrc)
	}

	private fun startSynchronize() = repeatJob(SYNCHRONIZATION_PERIOD) {
		val modules =
			moduleLoader.getLoadedModules().filter { it is AbstractServerModule }
				.unsafeCast<List<AbstractServerModule>>()

		val players = sessionModule.getConnectedPlayers()

		if (players.isEmpty()) return@repeatJob

		val syncData = ServerSideSynchronizationEvent(serverTime = 0.0)

		launch {
			modules.forEach {
				launch {
					it.onSync(syncData)?.join()
				}
			}
		}.join()

		this.syncData = syncData

		for (playerSrc in players) {
			syncDataFor(playerSrc)
			delay(SYNCHRONIZATION_PERIOD / players.size)
		}
	}

	private fun startPlayerDataListener() = launch {
		for (obj in syncDataChannel) {

			val player = obj.first
			val data = obj.second

			moduleLoader.getLoadedModules().forEach { module ->
				if (module !is AbstractServerModule) return@forEach

				launch {
					module.onSync(player, data)?.join()
				}
			}
		}
	}

	companion object {
		const val SYNCHRONIZATION_PERIOD: Long = 1_000L * ServerConfig.SYNCHRONIZATION_PERIOD_SECONDS
	}
}