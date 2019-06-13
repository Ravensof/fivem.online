package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeTextEntries

fun NativeTextEntries.addText(text: String) {
	Client.addTextEntry(code, text)
}