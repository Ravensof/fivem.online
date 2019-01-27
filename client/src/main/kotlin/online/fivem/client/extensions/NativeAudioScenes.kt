package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeAudioScenes

fun NativeAudioScenes.start() {
	@Suppress("DEPRECATION")
	Client.startAudioScene(name)
}

fun NativeAudioScenes.stop() {
	@Suppress("DEPRECATION")
	Client.stopAudioScene(name)
}