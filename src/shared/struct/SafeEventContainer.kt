package shared.struct

class SafeEventContainer<T>(
		val eventToken: SafeEventKey,
		val data: T
)