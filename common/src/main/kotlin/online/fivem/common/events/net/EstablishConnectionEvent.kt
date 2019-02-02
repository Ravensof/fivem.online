package online.fivem.common.events.net

import kotlinx.serialization.Serializable

/**
 * говорит клиенту, что он должен в дальнейшем использовать key для передачи пакетов
 */
@Serializable
class EstablishConnectionEvent(
	val key: Double
)