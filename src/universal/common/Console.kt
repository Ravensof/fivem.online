package universal.common

import universal.modules.web.events.ConsoleLogWeb
import universal.r.*


object Console {
//	fun dir(o: Any) {
//		if (CONSOLE_DIR) {
//			console.log("$CONSOLE_PREFIX[DIR]: ")
//			console.dir(o)
//		}
//	}

	fun error(vararg o: Any?) {
		if (CONSOLE_ERROR) {
			var str = "$CONSOLE_PREFIX[ERROR]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	fun info(vararg o: Any?) {
		if (CONSOLE_INFO) {
			var str = "$CONSOLE_PREFIX[INFO]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	fun log(vararg o: Any?) {
		if (CONSOLE_LOG) {
			var str = "$CONSOLE_PREFIX[LOG]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	fun warn(vararg o: Any?) {
		if (CONSOLE_WARN) {
			var str = "$CONSOLE_PREFIX[WARN]: "
			o.forEach {
				str += it
			}
			console.warn(str)
		}
	}

	fun debug(vararg o: Any?) {
		if (CONSOLE_DEBUG) {
			var str = "$CONSOLE_PREFIX[DEBUG]: "
			o.forEach {
				str += it
			}
			console.log(str)
		}
	}

	fun logWeb(vararg o: Any?) {
		if (CONSOLE_LOG) {
			var str = "$CONSOLE_PREFIX[LOG]: "
			o.forEach {
				str += it
			}
			Event.emit(ConsoleLogWeb(str))
		}
	}

	@Deprecated("check value")
	fun <T> checkValue(functionName: String, functionResult: T, showIf: (T) -> Boolean = { true }): T {
		if (showIf(functionResult)) {
			debug("checkValue: $functionName=$functionResult")
		}
		return functionResult
	}
}

