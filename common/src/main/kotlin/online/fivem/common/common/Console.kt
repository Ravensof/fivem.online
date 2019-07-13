package online.fivem.common.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import online.fivem.common.GlobalConfig
import online.fivem.common.GlobalConfig.CONSOLE_PREFIX
import online.fivem.common.GlobalConfig.SERVER_TIMEZONE
import kotlin.coroutines.CoroutineContext
import kotlin.js.Console
import kotlin.js.Date

@Suppress("ConstantConditionIf")
object Console : Console, CoroutineScope {

	override val coroutineContext: CoroutineContext = createSupervisorJob()

	private val channel = Channel<Message>(32)

	init {
		launch {
			for (data in channel) {
				print(data)
			}
		}
	}

	private fun print(message: Message) {
		val obj = if (GlobalConfig.concatConsoleOutput) {
			arrayOf(message.prefix + " " + message.obj.joinToString(separator = "\r\n"))
		} else {
			arrayOf(message.prefix) + message.obj
		}

		when (message) {
			is Message.Debug -> console.log(*obj)

			is Message.Info -> console.info(*obj)

			is Message.Warning -> console.warn(*obj)

			is Message.Error -> console.error(*obj)

			else -> console.log(*obj)
		}
	}

	override fun dir(o: Any) {
		TODO("not implemented")
	}

	override fun error(vararg o: Any?) {
		launch { channel.send(Message.Error("${getTimeStamp()}$CONSOLE_PREFIX[ERROR]: ", *o)) }
	}

	override fun info(vararg o: Any?) {
		launch { channel.send(Message.Info("${getTimeStamp()}$CONSOLE_PREFIX[INFO]: ", *o)) }
	}

	override fun log(vararg o: Any?) {
		launch { channel.send(Message.Log("${getTimeStamp()}$CONSOLE_PREFIX[LOG]: ", *o)) }

	}

	override fun warn(vararg o: Any?) {
		launch { channel.send(Message.Warning("${getTimeStamp()}$CONSOLE_PREFIX[WARN]: ", *o)) }

	}

	fun debug(vararg o: Any?) {
		if (!GlobalConfig.SHOW_CONSOLE_DEBUG) return

		launch { channel.send(Message.Debug("${getTimeStamp()}$CONSOLE_PREFIX[DEBUG]: ", *o)) }

	}

	private fun getTimeStamp(): String {
		val date = Date(Date.now() + VDate.HOUR * SERVER_TIMEZONE)

		return "[" +
//				"${date.getUTCFullYear()}." +
				"${date.getUTCMonth() + 1}." +
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

	private sealed class Message(val prefix: String, vararg val obj: Any?) {

		class Log(prefix: String, vararg obj: Any?) : Message(prefix, *obj)
		class Info(prefix: String, vararg obj: Any?) : Message(prefix, *obj)
		class Debug(prefix: String, vararg obj: Any?) : Message(prefix, *obj)
		class Warning(prefix: String, vararg obj: Any?) : Message(prefix, *obj)
		class Error(prefix: String, vararg obj: Any?) : Message(prefix, *obj)
	}
}

