package online.fivem.common.events

/**
 * говорит клиенту, что он должен в дальнейшем использовать key для передачи пакетов
 */
class EstablishConnectionEvent(
	val key: Double
)