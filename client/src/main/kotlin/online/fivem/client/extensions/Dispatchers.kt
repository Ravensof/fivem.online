package online.fivem.client.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import online.fivem.client.common.NativeDispatcher


val Dispatchers.Native: CoroutineDispatcher by lazy { NativeDispatcher() }
