@file:Suppress("DEPRECATION")

package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeAudioScenes

fun NativeAudioScenes.start() {
	Natives.startAudioScene(name)
}

fun NativeAudioScenes.stop() {
	Natives.stopAudioScene(name)
}