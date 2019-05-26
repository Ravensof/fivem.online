package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.GlobalConfig.CONSOLE_PREFIX
import kotlin.coroutines.CoroutineContext
import kotlin.js.Console
import kotlin.js.Date

object Console : Console, CoroutineScope {
	override val coroutineContext: CoroutineContext = Job()
	private val channel = Channel<String>(32)

	init {
		launch {
			for (data in channel) {
				console.log(data)
			}
		}
	}

	override fun dir(o: Any) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun error(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_ERROR) {
			var str = "${getTimeStamp()}$CONSOLE_PREFIX[ERROR]: "
			o.forEach {
				str += it
			}
			launch { channel.send(str) }
		}
	}

	override fun info(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_INFO) {
			var str = "${getTimeStamp()}$CONSOLE_PREFIX[INFO]: "
			o.forEach {
				str += it
			}
			launch { channel.send(str) }
		}
	}

	override fun log(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_LOG) {
			var str = "${getTimeStamp()}$CONSOLE_PREFIX[LOG]: "
			o.forEach {
				str += it
			}
			launch { channel.send(str) }
		}
	}

	override fun warn(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_WARN) {
			var str = "${getTimeStamp()}$CONSOLE_PREFIX[WARN]: "
			o.forEach {
				str += it
			}
			launch { channel.send(str) }
		}
	}

	fun debug(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_DEBUG) {
			var str = "${getTimeStamp()}$CONSOLE_PREFIX[DEBUG]: "
			o.forEach {
				str += it
			}
			launch { channel.send(str) }
		}
	}

	private fun getTimeStamp(): String {
		val date = Date()

		return "[" +
//				"${date.getUTCFullYear()}." +
				"${date.getUTCMonth()}." +
				"${date.getUTCDate()}/" +
				"${date.getUTCHours()}:${date.getUTCMinutes()}:${date.getUTCSeconds()}.${date.getUTCMilliseconds()}]"
	}

	@Deprecated("check value")
	fun <T> checkValue(functionName: String, functionResult: T, showIf: (T) -> Boolean = { true }): T {
		if (showIf(functionResult)) {
//			logWeb("checkValue: $functionName=$functionResult")
			log("checkValue: $functionName=$functionResult")
		}
		return functionResult
	}
}

