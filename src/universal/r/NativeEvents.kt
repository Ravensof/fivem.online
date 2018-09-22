package universal.r

object NativeEvents {

	/**
	 * @link http://docs.fivem.net/events/client-events/
	 */
	object Client {

		//Default events

		/**
		 * onClientResourceStart(string resourceName)
		 * Parameters
		 * resourceName - A string containing the name of the resource that was started.
		 */
		const val RESOURCE_START = "onClientResourceStart"

		/**
		 * onClientResourceStop(string resourceName)
		 *
		 * Parameters
		 * resourceName - A string containing the name of the resource that was stopped.
		 */
		const val RESOURCE_STOP = "onClientResourceStop"

		//spawnmanager events (resource events)

		/**
		 * playerSpawned(object spawnInfo)
		 *
		 * Parameters
		 * spawnInfo - An object containing the following information:
		 * (float) x - The x coordinate of where the player spawned to.
		 * (float) y - The y coordinate of where the player spawned to.
		 * (float) z - The z coordinate of where the player spawned to.
		 * (float) heading - The heading that the player is facing when spawned.
		 * (int) idx - The spawnpoint index.
		 * (Hash) model - The ped model hash the player spawned as.
		 * Example
		 * {
		 *  "z":111.5291,
		 *  "y":197.7201,
		 *  "x":466.8401,
		 *  "heading":291.71,
		 *  "idx":6,
		 *  "model":1631478380
		 * }
		 */
		const val PLAYER_SPAWNED = "playerSpawned"

		//mapmanager events (resource events)

		/**
		 * onClientMapStart(string resourceName)
		 *
		 * Parameters
		 * resourceName - The name of the resource/map that started.
		 */
		const val MAP_START = "onClientMapStart"

		/**
		 * onClientGameTypeStart(string resourceName)
		 *
		 * Parameters
		 * resourceName - The name of the resource/gametype that started.
		 */
		const val GAME_TYPE_START = "onClientGameTypeStart"

		/**
		 * onClientMapStop(string resourceName)
		 *
		 * Parameters
		 * resourceName - The name of the resource/map that stopped.
		 */
		const val MAP_STOP = "onClientMapStop"

		/**
		 * onClientGameTypeStop(string resourceName)
		 *
		 * Parameters
		 * resourceName - The name of the resource/gametype that stopped.
		 */
		const val GAME_TYPE_STOP = "onClientGameTypeStop"

		/**
		 * getMapDirectives(function add)
		 *
		 * Parameters
		 * add - A function used to add things like spawnpoints and vehicle generators.
		 */
		const val GET_MAP_DIRECTIVES = "getMapDirectives"


		// baseevents (resource events)

		/**
		 * onPlayerDied(player deadPlayer, string deathReason)
		 *
		 * Parameters
		 * deadPlayer - The client ID of the player who died.
		 * deathReason - A string containing the reason why the player died.
		 */
		const val PLAYER_DIED = "onPlayerDied"//(player deadPlayerId, string deathReason)

		/**
		 * onPlayerKilled(player deadPlayer, player killer, string deathReason)
		 *
		 * Parameters
		 * deadPlayer - The client ID of the player who died.
		 * killer - The client ID of the player who killed the deadPlayer player.
		 * deathReason - A string containing the reason why the player died.
		 */
		const val PLAYER_KILLED = "onPlayerKilled"//(player deadPlayerId, player killer, string deathReason)


		//sessionmanager events (resource events)

		const val PLAYER_ACTIVATED = "playerActivated"//()

		const val SESSION_INITIALIZED = "sessionInitialized"//()

		//chat events (resource events)

		/**
		 * chatMessage(string author, array color, string text)
		 *
		 * Triggering this event allows you to send a chat message to this client.
		 * Color syntax: {255, 255, 255} ( {r, g, b} )
		 */
		const val CHAT_MESSAGE = "chatMessage"//(string author, array color, string text)

		/**
		 * chat:addMessage(object message)
		 *
		 * Triggering this event allows you to send a chat message to this client.
		 * Message object structure:
		 * message = {
		 *  color = color,
		 *  multiline = true,
		 *  args = {author, otherArgs...}
		 * }
		 */
		const val CHAT_ADD_MESSAGE = "chat:addMessage"//(object message)


		/**
		 * chat:addTemplate(string templateID, string htmlString)
		 *
		 * Triggering this event allows you to add a template to be used with chat:addMessage.
		 * Example:
		 * TriggerEvent('chat:addTemplate', 'tweet', "<img src='data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+Cjxz%0D%0AdmcKICB2aWV3Ym94PSIwIDAgMjAwMCAxNjI1LjM2IgogIHdpZHRoPSIyMDAwIgogIGhlaWdodD0i%0D%0AMTYyNS4zNiIKICB2ZXJzaW9uPSIxLjEiCiAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAv%0D%0Ac3ZnIj4KICA8cGF0aAogICAgZD0ibSAxOTk5Ljk5OTksMTkyLjQgYyAtNzMuNTgsMzIuNjQgLTE1%0D%0AMi42Nyw1NC42OSAtMjM1LjY2LDY0LjYxIDg0LjcsLTUwLjc4IDE0OS43NywtMTMxLjE5IDE4MC40%0D%0AMSwtMjI3LjAxIC03OS4yOSw0Ny4wMyAtMTY3LjEsODEuMTcgLTI2MC41Nyw5OS41NyBDIDE2MDku%0D%0AMzM5OSw0OS44MiAxNTAyLjY5OTksMCAxMzg0LjY3OTksMCBjIC0yMjYuNiwwIC00MTAuMzI4LDE4%0D%0AMy43MSAtNDEwLjMyOCw0MTAuMzEgMCwzMi4xNiAzLjYyOCw2My40OCAxMC42MjUsOTMuNTEgLTM0%0D%0AMS4wMTYsLTE3LjExIC02NDMuMzY4LC0xODAuNDcgLTg0NS43MzksLTQyOC43MiAtMzUuMzI0LDYw%0D%0ALjYgLTU1LjU1ODMsMTMxLjA5IC01NS41NTgzLDIwNi4yOSAwLDE0Mi4zNiA3Mi40MzczLDI2Ny45%0D%0ANSAxODIuNTQzMywzNDEuNTMgLTY3LjI2MiwtMi4xMyAtMTMwLjUzNSwtMjAuNTkgLTE4NS44NTE5%0D%0ALC01MS4zMiAtMC4wMzksMS43MSAtMC4wMzksMy40MiAtMC4wMzksNS4xNiAwLDE5OC44MDMgMTQx%0D%0ALjQ0MSwzNjQuNjM1IDMyOS4xNDUsNDAyLjM0MiAtMzQuNDI2LDkuMzc1IC03MC42NzYsMTQuMzk1%0D%0AIC0xMDguMDk4LDE0LjM5NSAtMjYuNDQxLDAgLTUyLjE0NSwtMi41NzggLTc3LjIwMywtNy4zNjQg%0D%0ANTIuMjE1LDE2My4wMDggMjAzLjc1LDI4MS42NDkgMzgzLjMwNCwyODQuOTQ2IC0xNDAuNDI5LDEx%0D%0AMC4wNjIgLTMxNy4zNTEsMTc1LjY2IC01MDkuNTk3MiwxNzUuNjYgLTMzLjEyMTEsMCAtNjUuNzg1%0D%0AMSwtMS45NDkgLTk3Ljg4MjgsLTUuNzM4IDE4MS41ODYsMTE2LjQxNzYgMzk3LjI3LDE4NC4zNTkg%0D%0ANjI4Ljk4OCwxODQuMzU5IDc1NC43MzIsMCAxMTY3LjQ2MiwtNjI1LjIzOCAxMTY3LjQ2MiwtMTE2%0D%0ANy40NyAwLC0xNy43OSAtMC40MSwtMzUuNDggLTEuMiwtNTMuMDggODAuMTc5OSwtNTcuODYgMTQ5%0D%0ALjczOTksLTEzMC4xMiAyMDQuNzQ5OSwtMjEyLjQxIgogICAgc3R5bGU9ImZpbGw6IzAwYWNlZCIv%0D%0APgo8L3N2Zz4K' height='16'> <b>{0}</b>: {1}")
		 * TriggerEvent('chat:addMessage', { templateId = 'tweet', multiline = true, args = { 'Blu', 'tianshee was mean to me today 🙁' } })
		 */
		const val CHAT_ADD_TEMPLATE = "chat:addTemplate"


		/**
		 * chat:addSuggestion(string commandName, string commandDescription, object commandParameters)
		 *
		 * Triggering this event allows you to add command suggestions to your chat. Example usage:
		 * -- Note, the command has to start with `/`.
		 *
		 * TriggerEvent('chat:addSuggestion', '/command', 'help text', {
		 *  { name="paramName1", help="param description 1" },
		 * { name="paramName2", help="param description 2" }
		 * })
		 */
		const val CHAT_ADD_SUGGESTION = "chat:addSuggestion"//(string commandName, string commandDescription, object commandParameters)

		/**
		 * chat:removeSuggestion(string commandName)
		 *
		 * Triggering this allows you to remove any existing command suggestions for the specified command.
		 */
		const val CHAT_REMOVE_SUGGESTION = "chat:removeSuggestion"//(string commandName)

		/**
		 * chat:clear()
		 *
		 * Clears the chat messages/history and the sent messages history buffer.
		 */
		const val CHAT_CLEAR = "chat:clear"//()
	}

	object Server {
		/**
		 * rconCommand(string command, table commandArguments)
		 * Parameters
		 * command - A string containing the command name that was executed.
		 * commandArguments - A table/array containing all arguments passed to the command.
		 */
		const val RCON_COMMAND = "rconCommand"

		/**
		 * playerDropped(playerSource player, string disconnectReason)
		 *
		 * Parameters
		 * player - The source/player that disconnected.
		 * disconnectReason - The reason why the player has disconnected.
		 */
		const val PLAYER_DROPPED = "playerDropped"

		/**
		 * playerConnecting(string playerName, function setKickReason, source tempPlayer)
		 *
		 * Parameters
		 * playerName - The player name of the player connecting.
		 * setKickReason - A function used to defer connections and/or kick players with a custom message.
		 * tempPlayer - A temporary player source which can be used to get things like Player Identifiers and much more.
		 */
		const val PLAYER_CONNECTING = "playerConnecting"

		const val RESOURCE_START = "onServerResourceStart"//resourceName: String
	}
}