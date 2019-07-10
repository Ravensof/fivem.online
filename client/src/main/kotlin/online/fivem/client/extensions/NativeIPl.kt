package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeIPl

fun NativeIPl.request() {
	code.forEach {
		Natives.requestIpl(it)
	}
}