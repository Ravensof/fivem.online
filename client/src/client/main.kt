package client

import client.extensions.emitNet
import client.extensions.onNet
import client.modules.eventGenerator.EventGenerator
import client.modules.radio.RadioModule
import client.modules.session.SessionModule
import shared.common.Console
import shared.common.Event
import shared.common.on
import shared.events.ClientReady
import shared.events.ConsoleLogEvent
import shared.r.MODULE_FOLDER_NAME
import shared.setTimeout

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

	EventGenerator()

	SessionModule.getInstance()
	RadioModule()

	Console.info("client started")

	Event.emitNet(ClientReady())
}

fun main(args: Array<String>) {
	Console.debug("hello client")

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
