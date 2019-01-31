package online.fivem.common

import online.fivem.common.common.KSerializer
import online.fivem.common.entities.CoordinatesX
import online.fivem.common.events.net.*

fun initSerializableClasses() {
	KSerializer.add(
		TestEvent::class to TestEvent.serializer(),

		BlackOutEvent::class to BlackOutEvent.serializer(),
		CancelBlackOutEvent::class to CancelBlackOutEvent.serializer(),
		ImReadyEvent::class to ImReadyEvent.serializer(),
		RequestPackEvent::class to RequestPackEvent.serializer(),
		CoordinatesX::class to CoordinatesX.serializer()
//		SpawnPlayerEvent::class to SpawnPlayerEvent.serializer(),
//		SynchronizeEvent::class to SynchronizeEvent.serializer()
	)
}