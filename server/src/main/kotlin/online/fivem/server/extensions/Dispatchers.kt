package online.fivem.server.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import online.fivem.server.common.NativeDispatcher

val Dispatchers.Native: CoroutineDispatcher by lazy { NativeDispatcher() }
