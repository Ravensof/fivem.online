package online.fivem.common

import online.fivem.common.events.net.*
import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent
import online.fivem.common.events.nui.*
import online.fivem.common.other.SerializerInterface

private val kSerializer = online.fivem.common.common.KSerializer()
val Serializer: SerializerInterface = kSerializer //online.fivem.common.common.Serializer()


private fun main() {
	kSerializer.add(
		//net
		RolePlaySystemSaveEvent::class to RolePlaySystemSaveEvent.serializer(),

		AcceptEvent::class to AcceptEvent.serializer(),
		ClientSideSynchronizationEvent::class to ClientSideSynchronizationEvent.serializer(),
		ErrorReportEvent::class to ErrorReportEvent.serializer(),
		EstablishConnectionEvent::class to EstablishConnectionEvent.serializer(),
		ImReadyEvent::class to ImReadyEvent.serializer(),
		ServerSideSynchronizationEvent::class to ServerSideSynchronizationEvent.serializer(),
		SpawnPlayerEvent::class to SpawnPlayerEvent.serializer(),
		StopResourceEvent::class to StopResourceEvent.serializer(),
		//nui
		BlackOutEvent::class to BlackOutEvent.serializer(),
		CancelBlackOutEvent::class to CancelBlackOutEvent.serializer(),
		DebugNUITextEvent::class to DebugNUITextEvent.serializer(),

		InternetRadioModuleEvent.Changed::class to InternetRadioModuleEvent.Changed.serializer(),
		InternetRadioModuleEvent.Stop::class to InternetRadioModuleEvent.Stop.serializer(),
		InternetRadioModuleEvent.VolumeChanged::class to InternetRadioModuleEvent.VolumeChanged.serializer(),

//		NuiEmulateKeyDownEvent::class,
//		NuiEmulateKeyJustPressedEvent::class,
//		NuiEmulateKeyUpEvent::class,

		PlaySoundEvent::class to PlaySoundEvent.serializer(),
		PrefetchFileEvent::class to PrefetchFileEvent.serializer(),
		ShowGuiEvent::class to ShowGuiEvent.serializer(),

		SpeedometerModuleEvent.Enable::class to SpeedometerModuleEvent.Enable.serializer(),
		SpeedometerModuleEvent.Disable::class to SpeedometerModuleEvent.Disable.serializer(),
		SpeedometerModuleEvent.Update::class to SpeedometerModuleEvent.Update.serializer(),

		WebStorageEvent.Post::class to WebStorageEvent.Post.serializer(),
		WebStorageEvent.Request::class to WebStorageEvent.Request.serializer(),
		WebStorageEvent.Response::class to WebStorageEvent.Response.serializer()
	)
}