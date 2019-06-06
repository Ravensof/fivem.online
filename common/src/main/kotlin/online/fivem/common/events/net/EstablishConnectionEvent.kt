package online.fivem.common.events.net

import online.fivem.common.other.Serializable

/**
 * говорит клиенту, что он должен в дальнейшем использовать key для передачи пакетов
 */

@kotlinx.serialization.Serializable
class EstablishConnectionEvent(
	val key: Double
) : Serializable()