package online.fivem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.fivem.common.GlobalConfig
import online.fivem.extensions.Native
import online.fivem.server.entities.PlayerIdentifiers
import online.fivem.server.entities.PlayerSrc
import online.fivem.server.gtav.enums.ResourceState

object Natives {
	suspend fun startResource(resourceName: String) = withContext(Dispatchers.Native) {
		StartResource(resourceName)
	}

	suspend fun stopResource(resourceName: String) = withContext(Dispatchers.Native) {
		StopResource(resourceName)
	}

	/**
	 * Returns the physical on-disk path of the specified resource.
	 * @param resourceName The name of the resource.
	 * @return The resource directory name, possibly without trailing slash.
	 */
	suspend fun getResourcePath(resourceName: String) = withContext(Dispatchers.Native) {
		return@withContext GetResourcePath(resourceName)
	}

	/**
	 * Returns the current state of the specified resource.
	 * @param resourceName The name of the resource.
	 * @return The resource state. One of `"missing", "started", "starting", "stopped", "stopping", "uninitialized" or "unknown"`.
	 */
	suspend fun getResourceState(resourceName: String): ResourceState = withContext(Dispatchers.Native) {
		val code = GetResourceState(resourceName)
		return@withContext ResourceState.values().find { it.code == code } ?: ResourceState.UNKNOWN
	}

	suspend fun on(eventName: String, callback: Function<*>) = withContext(Dispatchers.Native) {
		online.fivem.on(eventName, callback)
	}

	suspend fun emitNet(eventName: String, playerSrc: Int, data: Any) = withContext(Dispatchers.Native) {
		emitNet(eventName, playerSrc.toString(), data)
	}

//	suspend fun onNet(eventName: String, callback: (PlayerSrc, Any) -> Unit) = withContext(Dispatchers.Native) {
//		Exports.onNet(eventName, callback)
//	}

	suspend fun getRegisteredCommands() = withContext(Dispatchers.Native) {
		return@withContext GetRegisteredCommands()
	}

	suspend fun registerCommand(
		commandName: String,
		restricted: Boolean = false,
		handler: (Int, Array<String>, String) -> Unit

	) = withContext(Dispatchers.Native) {
		RegisterCommand(commandName, handler, restricted)
	}

	//не работает для бинарных файлов
	suspend fun loadResourceFile(resourceName: String, fileName: String): String? = withContext(Dispatchers.Native) {
		return@withContext LoadResourceFile(resourceName, fileName)
	}

	suspend fun loadFile(fileName: String): String? = withContext(Dispatchers.Native) {
		return@withContext loadResourceFile(GlobalConfig.MODULE_NAME, fileName)
	}

	suspend fun saveResourceFile(resourceName: String, fileName: String, data: String) =
		withContext(Dispatchers.Native) {
			return@withContext SaveResourceFile(resourceName, fileName, data, data.length) == 1
		}

	suspend fun saveFile(fileName: String, data: String): Boolean = withContext(Dispatchers.Native) {
		return@withContext SaveResourceFile(GlobalConfig.MODULE_NAME, fileName, data, data.length) == 1
	}

	//наверное, возвращает сколько всего игроков, включая подключающихся. != кол-ву игроков в одной сессии
	suspend fun countPlayersOnline(): Int = withContext(Dispatchers.Native) {
		return@withContext GetNumPlayerIndices().toInt()
	}

	suspend fun getPlayerIP(playerSrc: PlayerSrc): String? = withContext(Dispatchers.Native) {
		return@withContext GetPlayerEndpoint(playerSrc.value.toString())
	}

	suspend fun getPlayerPing(playerSrc: PlayerSrc): Int = withContext(Dispatchers.Native) {
		return@withContext GetPlayerPing(playerSrc.value.toString()).toInt()
	}

	suspend fun getPlayers(): List<PlayerSrc> = withContext(Dispatchers.Native) {
		val playersList = mutableListOf<PlayerSrc>()

		for (i in 1..GlobalConfig.MAX_PLAYERS) {

			if (GetPlayerEndpoint(i.toString()).isNotEmpty()) {
				playersList.add(PlayerSrc(i))
			}
		}

		return@withContext playersList
	}

	suspend fun getPlayerIdentifiers(playerSrc: PlayerSrc): PlayerIdentifiers = withContext(Dispatchers.Native) {

		var steam: String? = null
		var license: String? = null
		var ip: String? = null
		var discord: String? = null

		for (i in 0 until getNumPlayerIdentifiers(playerSrc)) {

			val identifier = GetPlayerIdentifier(playerSrc.value.toString(), i).split(":")

			if (identifier.size == 2) {
				when (identifier[0]) {
					"steam" -> steam = identifier[1]
					"license" -> license = identifier[1]
					"ip" -> ip = identifier[1]
					"discord" -> discord = identifier[1]
				}
			}
		}

		return@withContext PlayerIdentifiers(
			steam = steam,
			license = license,
			ip = ip,
			name = getPlayerName(playerSrc),
			discord = discord
		)
	}

	suspend fun getPlayerIdentifiers(source: Int): PlayerIdentifiers = withContext(Dispatchers.Native) {
		return@withContext getPlayerIdentifiers(PlayerSrc(source))
	}

	suspend fun getPlayerName(playerSrc: PlayerSrc): String? = withContext(Dispatchers.Native) {
		return@withContext GetPlayerName(playerSrc.value.toString())
	}

	suspend fun dropPlayer(playerSrc: PlayerSrc, reason: String) = withContext(Dispatchers.Native) {
		DropPlayer(playerSrc.value.toString(), reason)
	}

//	suspend fun dropPlayer(playerSrc: Int, reason: String) = withContext(Dispatchers.Native) {
//		DropPlayer(playerSrc.toString(), reason)
//	}

	suspend fun executeCommand(commandString: String) = withContext(Dispatchers.Native) {
		ExecuteCommand(commandString)
	}

	suspend fun getCurrentResourceName(): String = withContext(Dispatchers.Native) {
		return@withContext GetCurrentResourceName()
	}

	private suspend fun getNumPlayerIdentifiers(playerSrc: PlayerSrc): Int = withContext(Dispatchers.Native) {
		return@withContext GetNumPlayerIdentifiers(playerSrc.value.toString()).toInt()
	}
}