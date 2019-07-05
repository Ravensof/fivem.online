package online.fivem.server.gtav.enums

enum class ResourceState(val code: String) {
	MISSING("missing"),
	STARTED("started"),
	STARTING("starting"),
	STOPPED("stopped"),
	STOPPING("stopping"),
	UNINITIALIZED("uninitialized"),
	UNKNOWN("unknown"),
}