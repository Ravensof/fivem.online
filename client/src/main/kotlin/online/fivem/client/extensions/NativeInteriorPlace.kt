package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeInteriorPlace

fun NativeInteriorPlace.request() {
	code.forEach {
		Client.requestIpl(it)
	}
}