package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeIPl

fun NativeIPl.request() {
	code.forEach {
		Client.requestIpl(it)
	}
}