package shared

import shared.r.*

object Console {
	fun dir(o: Any) {
		if (CONSOLE_DIR) {
			print("$CONSOLE_PREFIX[DIR]")
			console.dir(o)
		}
	}

	fun error(vararg o: Any?) {
		if (CONSOLE_ERROR) {
			print("$CONSOLE_PREFIX[ERROR]")
			console.error(*o)
		}
	}

	fun info(vararg o: Any?) {
		if (CONSOLE_INFO) {
			print("$CONSOLE_PREFIX[INFO]")
			console.info(*o)
		}
	}

	fun log(vararg o: Any?) {
		if (CONSOLE_LOG) {
			print("$CONSOLE_PREFIX[LOG]")
			console.log(*o)
		}
	}

	fun warn(vararg o: Any?) {
		if (CONSOLE_WARN) {
			print("$CONSOLE_PREFIX[WARN]")
			console.warn(*o)
		}
	}

	fun debug(vararg o: Any?) {
		if (CONSOLE_DEBUG) {
			print("$CONSOLE_PREFIX[DEBUG]")
			console.log(*o)
		}
	}

}

