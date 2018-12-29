package online.fivem.common.common

import online.fivem.common.GlobalConfig
import online.fivem.common.GlobalConfig.CONSOLE_PREFIX
import kotlin.js.Console

object Console : Console {
	override fun dir(o: Any) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun error(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_ERROR) {
			var str = "$CONSOLE_PREFIX[ERROR]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	override fun info(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_INFO) {
			var str = "$CONSOLE_PREFIX[INFO]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	override fun log(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_LOG) {
			var str = "$CONSOLE_PREFIX[LOG]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	override fun warn(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_WARN) {
			var str = "$CONSOLE_PREFIX[WARN]: "
			o.forEach {
				str += it
			}
			console.warn(str)
		}
	}

	fun debug(vararg o: Any?) {
		if (GlobalConfig.SHOW_CONSOLE_DEBUG) {
			var str = "$CONSOLE_PREFIX[DEBUG]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

//	fun logWeb(vararg o: Any?) {
//		if (GlobalConfig.SHOW_CONSOLE_LOG) {
//			var str = ""
//			o.forEach {
//				str += it
//			}
//			Event.emit(ConsoleLogWebEvent(str))
//		}
//	}
//
//	fun warnWeb(vararg o: Any?) {
//		if (GlobalConfig.SHOW_CONSOLE_LOG) {
//			var str = ""
//			o.forEach {
//				str += it
//			}
//			Event.emit(ConsoleWarnWebEvent(str))
//		}
//	}
//
//	@Deprecated("check value")
//	fun <T> checkValue(functionName: String, functionResult: T, showIf: (T) -> Boolean = { true }): T {
//		if (showIf(functionResult)) {
//			logWeb("checkValue: $functionName=$functionResult")
//		}
//		return functionResult
//	}
}

