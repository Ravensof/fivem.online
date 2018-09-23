
import client.extensions.emitNet
import client.extensions.onNet
import client.modules.eventGenerator.EventGenerator
import client.modules.player.PlayerModule
import client.modules.radio.RadioModule
import client.modules.session.SessionModule
import client.modules.web.WebModule
import fivem.common.FiveM
import fivem.common.on

import universal.common.Console
import universal.common.Event
import universal.common.setTimeout
import universal.events.ClientReady
import universal.events.ConsoleLogEvent


fun start() {

	Event.on { event: ConsoleLogEvent ->
		Console.log("client received message \"${event.message}\"")
	}
	Event.onNet { event: ConsoleLogEvent ->
		Console.log("client received message \"${event.message}\" from server")
	}

//	Event.onNet<ClientRegisteredEvent> {
//		console.log("client registered as player " + it.player.id)
//		Engine(it.player)
//	}

	EventGenerator.getInstance()

	SessionModule.getInstance()
	RadioModule.getInstance()
	PlayerModule.getInstance()
	WebModule.getInstance()

	Console.info("client started")

	Event.emitNet(ClientReady())
}

fun main(args: Array<String>) {
	Console.debug("hello client")

	MODULE_FOLDER_NAME = FiveM.getCurrentResourceName()

	var resourceLoaded = false

	//без таймаута через раз exports работает. проверка в onClientResourceStart ничего не дает
	setTimeout(20000) {
		if (!resourceLoaded)
			start()
	}

	on("onClientResourceStart") { resource: String ->
		if (resource == MODULE_FOLDER_NAME) {
			resourceLoaded = true
			start()
		}
	}
}
