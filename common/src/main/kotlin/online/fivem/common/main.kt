package online.fivem.common

import online.fivem.common.common.KSerializer
import online.fivem.common.events.InternetRadioChangedEvent
import online.fivem.common.events.InternetRadioStopEvent
import online.fivem.common.events.InternetRadioVolumeChangeEvent
import online.fivem.common.events.net.*
import online.fivem.common.events.nui.*
import online.fivem.common.other.SerializerInterface

val Serializer: SerializerInterface = online.fivem.common.common.Serializer()

fun main() {
	initKSerializer()

}

fun initKSerializer() {

	if (Serializer !is KSerializer) return

	Serializer.add(
		//client-server
		EstablishConnectionEvent::class to EstablishConnectionEvent.serializer(),
		ImReadyEvent::class to ImReadyEvent.serializer(),
		SyncEvent::class to SyncEvent.serializer(),

		SpawnPlayerEvent::class to SpawnPlayerEvent.serializer(),
		SpawnVehicleEvent::class to SpawnVehicleEvent.serializer(),

//		RolePlaySystemSaveEvent::class to RolePlaySystemSaveEvent.serializer(),
		ServerSideSynchronizationEvent::class to ServerSideSynchronizationEvent.serializer(),

		StopResourceEvent::class to StopResourceEvent.serializer(),
		ErrorReportEvent::class to ErrorReportEvent.serializer(),

		//nui
		BlackOutEvent::class to BlackOutEvent.serializer(),
		CancelBlackOutEvent::class to CancelBlackOutEvent.serializer(),
		DebugNUITextEvent::class to DebugNUITextEvent.serializer(),
		NuiEmulateKeyDownEvent::class to NuiEmulateKeyDownEvent.serializer(),
		NuiEmulateKeyJustPressedEvent::class to NuiEmulateKeyJustPressedEvent.serializer(),
		NuiEmulateKeyUpEvent::class to NuiEmulateKeyUpEvent.serializer(),
		PlaySoundEvent::class to PlaySoundEvent.serializer(),
		PrefetchFileEvent::class to PrefetchFileEvent.serializer(),
		ShowGuiEvent::class to ShowGuiEvent.serializer(),

		SpeedometerDisableEvent::class to SpeedometerDisableEvent.serializer(),
		SpeedometerEnableEvent::class to SpeedometerEnableEvent.serializer(),
		SpeedometerUpdateEvent::class to SpeedometerUpdateEvent.serializer(),

		InternetRadioChangedEvent::class to InternetRadioChangedEvent.serializer(),
		InternetRadioStopEvent::class to InternetRadioStopEvent.serializer(),
		InternetRadioVolumeChangeEvent::class to InternetRadioVolumeChangeEvent.serializer(),

		LocalStorageEvent.Post::class to LocalStorageEvent.Post.serializer(),
		LocalStorageEvent.Request::class to LocalStorageEvent.Request.serializer(),
		LocalStorageEvent.Response::class to LocalStorageEvent.Response.serializer()
	)
}