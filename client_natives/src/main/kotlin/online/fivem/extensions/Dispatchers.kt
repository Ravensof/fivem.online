package online.fivem.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import online.fivem.common.NativeDispatcher


val Dispatchers.Native: CoroutineDispatcher by lazy { NativeDispatcher() }
