package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.common.EntityId
import online.fivem.common.gtav.NativeVehicles

fun NativeVehicles.Mod.setOn(vehicle: EntityId, value: Int) {
	when (this) {
		NativeVehicles.Mod.TURBO, NativeVehicles.Mod.SMOKE_ENABLED, NativeVehicles.Mod.XENON -> {
			Client.toggleVehicleMod(vehicle, id, value != 0)
		}

		NativeVehicles.Mod.SPOILERS,
		NativeVehicles.Mod.FRONT_BUMPER,
		NativeVehicles.Mod.REAR_BUMPER,
		NativeVehicles.Mod.SIDE_SKIRT,
		NativeVehicles.Mod.EXHAUST,
		NativeVehicles.Mod.FRAME,
		NativeVehicles.Mod.GRILLE,
		NativeVehicles.Mod.HOOD,
		NativeVehicles.Mod.FENDER,
		NativeVehicles.Mod.RIGHT_FENDER,
		NativeVehicles.Mod.ROOF,

		NativeVehicles.Mod.ENGINE,
		NativeVehicles.Mod.BRAKES,
		NativeVehicles.Mod.TRANSMISSION,
		NativeVehicles.Mod.HORNS,
		NativeVehicles.Mod.SUSPENSION,
		NativeVehicles.Mod.ARMOR,

		NativeVehicles.Mod.FRONT_WHEELS,
		NativeVehicles.Mod.BACK_WHEELS,

		NativeVehicles.Mod.PLATE_HOLDER,
		NativeVehicles.Mod.VANITY_PLATE,
		NativeVehicles.Mod.TRIM_A,
		NativeVehicles.Mod.ORNAMENTS,
		NativeVehicles.Mod.DASHBOARD,
		NativeVehicles.Mod.DIAL,
		NativeVehicles.Mod.DOORS_PEAKER,
		NativeVehicles.Mod.SEATS,
		NativeVehicles.Mod.STEERING_WHEEL,
		NativeVehicles.Mod.SHIFTER_LEAVERS,
		NativeVehicles.Mod.A_PLATE,
		NativeVehicles.Mod.SPEAKERS,
		NativeVehicles.Mod.TRUNK,
		NativeVehicles.Mod.HYDROLIC,
		NativeVehicles.Mod.ENGINE_BLOCK,
		NativeVehicles.Mod.AIR_FILTER,
		NativeVehicles.Mod.STRUTS,
		NativeVehicles.Mod.ARCH_COVER,
		NativeVehicles.Mod.AERIALS,
		NativeVehicles.Mod.TRIM_B,
		NativeVehicles.Mod.TANK,
		NativeVehicles.Mod.WINDOWS -> {
			Client.setVehicleMod(vehicle, id, value, false)
		}

		NativeVehicles.Mod.LIVERY -> {
			Client.setVehicleMod(vehicle, id, value, false)
			Client.setVehicleLivery(vehicle, value)
		}
	}
}

fun NativeVehicles.Mod.getOn(vehicle: EntityId): Int? {
	return when (this) {
		NativeVehicles.Mod.TURBO, NativeVehicles.Mod.SMOKE_ENABLED, NativeVehicles.Mod.XENON -> {
			if (Client.isToggleModOn(vehicle, id)) 1 else 0
		}

		NativeVehicles.Mod.SPOILERS,
		NativeVehicles.Mod.FRONT_BUMPER,
		NativeVehicles.Mod.REAR_BUMPER,
		NativeVehicles.Mod.SIDE_SKIRT,
		NativeVehicles.Mod.EXHAUST,
		NativeVehicles.Mod.FRAME,
		NativeVehicles.Mod.GRILLE,
		NativeVehicles.Mod.HOOD,
		NativeVehicles.Mod.FENDER,
		NativeVehicles.Mod.RIGHT_FENDER,
		NativeVehicles.Mod.ROOF,

		NativeVehicles.Mod.ENGINE,
		NativeVehicles.Mod.BRAKES,
		NativeVehicles.Mod.TRANSMISSION,
		NativeVehicles.Mod.HORNS,
		NativeVehicles.Mod.SUSPENSION,
		NativeVehicles.Mod.ARMOR,

		NativeVehicles.Mod.FRONT_WHEELS,
		NativeVehicles.Mod.BACK_WHEELS,

		NativeVehicles.Mod.PLATE_HOLDER,
		NativeVehicles.Mod.VANITY_PLATE,
		NativeVehicles.Mod.TRIM_A,
		NativeVehicles.Mod.ORNAMENTS,
		NativeVehicles.Mod.DASHBOARD,
		NativeVehicles.Mod.DIAL,
		NativeVehicles.Mod.DOORS_PEAKER,
		NativeVehicles.Mod.SEATS,
		NativeVehicles.Mod.STEERING_WHEEL,
		NativeVehicles.Mod.SHIFTER_LEAVERS,
		NativeVehicles.Mod.A_PLATE,
		NativeVehicles.Mod.SPEAKERS,
		NativeVehicles.Mod.TRUNK,
		NativeVehicles.Mod.HYDROLIC,
		NativeVehicles.Mod.ENGINE_BLOCK,
		NativeVehicles.Mod.AIR_FILTER,
		NativeVehicles.Mod.STRUTS,
		NativeVehicles.Mod.ARCH_COVER,
		NativeVehicles.Mod.AERIALS,
		NativeVehicles.Mod.TRIM_B,
		NativeVehicles.Mod.TANK,
		NativeVehicles.Mod.WINDOWS -> {
			Client.getVehicleMod(vehicle, id)
		}

		NativeVehicles.Mod.LIVERY -> {
			Client.getVehicleLivery(vehicle)
		}
	}
}