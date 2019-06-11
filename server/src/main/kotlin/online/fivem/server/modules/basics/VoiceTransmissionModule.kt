package online.fivem.server.modules.basics

import external.nodejs.express.App
import external.nodejs.http.HTTP
import external.nodejs.requireNodeJSModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig.WEBRTC_PORT
import online.fivem.common.common.Console
import online.fivem.common.extensions.forEach
import online.fivem.server.common.AbstractServerModule

class VoiceTransmissionModule : AbstractServerModule() {

	override fun onStart(): Job? {

		val pauseChannel = Channel<Unit>()

		Console.debug("require easyrtc..")
		// Load required modules
		val http = requireNodeJSModule("http").unsafeCast<HTTP>()            // http server core module
		val express = requireNodeJSModule("express")           // web framework external module
		val serveStatic = requireNodeJSModule("serve-static")  // serve static files
		val socketIo = requireNodeJSModule("socket.io")        // web socket external module
		val easyrtc = requireNodeJSModule("easyrtc")   // EasyRTC external module

		// Set process name
		//		process.title = "node-easyrtc";

		// Setup and configure Express http server. Expect a subfolder called "static" to be the web root.
		val app = express().unsafeCast<App>()
		app.use(serveStatic("static", object {
			val index = arrayOf("index.html")
		}))

		// Start Express http server on port 8080
		val webServer = http.createServer(app)

		// Start Socket.io so it attaches itself to Express server
		val socketServer = socketIo.listen(webServer, object {/*"log level":1*/ })

		easyrtc.setOption("logLevel", "debug")

		// Overriding the default easyrtcAuth listener, only so we can directly access its callback
		easyrtc.events.on(
			"easyrtcAuth",
			fun(socket: dynamic, easyrtcid: dynamic, msg: dynamic, socketCallback: dynamic, callback: dynamic) {
				easyrtc.events.defaultListeners.easyrtcAuth(
					socket,
					easyrtcid,
					msg,
					socketCallback,
					fun(err: dynamic, connectionObj: dynamic) {
						if (err || !msg.msgData || !msg.msgData.credential || !connectionObj) {
							callback(err, connectionObj)
							return
						}

						connectionObj.setField("credential", msg.msgData.credential, object {
							val isShared = false
						})

						Console.log(
							"[" + easyrtcid + "] Credential saved!",
							connectionObj.getFieldValueSync("credential")
						)

						callback(err, connectionObj)
					})
			})

		// To test, lets print the credential to the console for every room join!
		easyrtc.events.on(
			"roomJoin",
			fun(connectionObj: dynamic, roomName: dynamic, roomParameter: dynamic, callback: dynamic) {
				Console.log(
					"[" + connectionObj.getEasyrtcid() + "] Credential retrieved!",
					connectionObj.getFieldValueSync("credential")
				)
				easyrtc.events.defaultListeners.roomJoin(connectionObj, roomName, roomParameter, callback);
			})

		// Start EasyRTC server
		var rtc = easyrtc.listen(app, socketServer, null, fun(err: dynamic, rtcRef: dynamic) {
			Console.log("Initiated")

			rtcRef.events.on(
				"roomCreate",
				fun(
					appObj: dynamic,
					creatorConnectionObj: dynamic,
					roomName: String,
					roomOptions: dynamic,
					callback: dynamic
				) {
					Console.log("roomCreate fired! Trying to create: $roomName")

					appObj.events.defaultListeners.roomCreate(
						appObj,
						creatorConnectionObj,
						roomName,
						roomOptions,
						callback
					)
				})
		})

		webServer.listen(PORT) { error ->
			pauseChannel.close()

			error?.let {
				throw Exception(it)
			}

			Console.log("listening on http://localhost:$PORT")
		}

		return launch {
			pauseChannel.forEach { }
		}
	}

	companion object {
		const val PORT: Int = WEBRTC_PORT
	}
}

