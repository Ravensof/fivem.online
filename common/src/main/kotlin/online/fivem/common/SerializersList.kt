package online.fivem.common

import kotlinx.serialization.KSerializer
import online.fivem.common.events.net.*
import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent
import online.fivem.common.events.net.sync.VehiclesSyncClientEvent
import online.fivem.common.events.net.sync.VehiclesSyncServerEvent
import online.fivem.common.events.nui.*
import kotlin.reflect.KClass

internal val SerializersList: Map<KClass<*>, KSerializer<*>> = mapOf(
	//sync
	VehiclesSyncClientEvent::class to VehiclesSyncClientEvent.serializer(),
	VehiclesSyncServerEvent::class to VehiclesSyncServerEvent.serializer(),

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

	SpawnVehiclesCommand::class to SpawnVehiclesCommand.serializer(),
	VehiclesSpawnedEvent::class to VehiclesSpawnedEvent.serializer(),
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
	SpeedometerModuleEvent.SlowUpadate::class to SpeedometerModuleEvent.SlowUpadate.serializer(),

	WebStorageEvent.Post::class to WebStorageEvent.Post.serializer(),
	WebStorageEvent.Request::class to WebStorageEvent.Request.serializer(),
	WebStorageEvent.Response::class to WebStorageEvent.Response.serializer()
)