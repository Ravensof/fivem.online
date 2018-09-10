package client

import client.extensions.emitNet
import client.extensions.onNet
import shared.Event
import shared.events.ConsoleLogEvent

fun start() {

	Event.on { event: ConsoleLogEvent ->
		console.log("client received message \"${event.message}\"")
	}
	Event.onNet { event: ConsoleLogEvent ->
		console.log("client received message \"${event.message}\" from server")
	}

//	Event.onNet<ClientRegisteredEvent> {
//		console.log("client registered as player " + it.player.id)
//		Engine(it.player)
//	}


//	Event.emitNet(ConsoleLogEvent("hello from player"))

	Event.emitNet("test")


	console.log("client started")
}

fun main(args: Array<String>) {
	console.log("hello client")

	start()


//	on("onClientResourceStart"){resource: String->
//		if(resource== MODULE_FOLDER_NAME){
//			start()
//		}else
//			println(resource)
//	}
}
