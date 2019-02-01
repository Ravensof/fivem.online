package online.fivem.common

import online.fivem.common.common.KSerializer
import online.fivem.common.events.net.SpawnVehicleEvent

fun initSerializableClasses() {
	KSerializer.add(
		SpawnVehicleEvent::class to SpawnVehicleEvent.serializer()
	)
}