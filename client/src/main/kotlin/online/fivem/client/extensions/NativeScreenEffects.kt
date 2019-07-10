package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeScreenEffects

fun NativeScreenEffects.start(milliseconds: Int = 0, looped: Boolean = false) {
	Natives.startScreenEffect(code, milliseconds, looped)
}