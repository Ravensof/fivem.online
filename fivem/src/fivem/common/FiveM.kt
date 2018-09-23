package fivem.common

object FiveM {
	fun getCurrentResourceName(): String {
		return GetCurrentResourceName()
	}
}

private external fun GetCurrentResourceName(): String