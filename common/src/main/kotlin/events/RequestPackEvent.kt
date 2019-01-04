package online.fivem.common.events

import kotlin.reflect.KClass

class RequestPackEvent(
	val kClasses: List<KClass<*>>
)