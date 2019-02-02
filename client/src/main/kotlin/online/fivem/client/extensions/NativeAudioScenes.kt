@file:Suppress("DEPRECATION")

package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeAudioScenes

fun NativeAudioScenes.start() {
	Client.startAudioScene(name)
}

fun NativeAudioScenes.stop() {
	Client.stopAudioScene(name)
}