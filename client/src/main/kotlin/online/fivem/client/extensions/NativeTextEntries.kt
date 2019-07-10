package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeTextEntries

fun NativeTextEntries.addText(text: String) {
	Natives.addTextEntry(code, text)
}