package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeScreenEffects

fun NativeScreenEffects.start(milliseconds: Int = 0, looped: Boolean = false) {
	Client.startScreenEffect(effect, milliseconds, looped)
}