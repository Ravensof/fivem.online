package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.common.EntityId
import online.fivem.common.gtav.NativeVehicleMod

fun NativeVehicleMod.setOn(vehicle: EntityId, value: Int) {
	when (this) {
		NativeVehicleMod.TURBO, NativeVehicleMod.SMOKE_ENABLED, NativeVehicleMod.XENON -> {
			Natives.toggleVehicleMod(vehicle, id, value != 0)
		}

		NativeVehicleMod.SPOILERS,
		NativeVehicleMod.FRONT_BUMPER,
		NativeVehicleMod.REAR_BUMPER,
		NativeVehicleMod.SIDE_SKIRT,
		NativeVehicleMod.EXHAUST,
		NativeVehicleMod.FRAME,
		NativeVehicleMod.GRILLE,
		NativeVehicleMod.HOOD,
		NativeVehicleMod.FENDER,
		NativeVehicleMod.RIGHT_FENDER,
		NativeVehicleMod.ROOF,

		NativeVehicleMod.ENGINE,
		NativeVehicleMod.BRAKES,
		NativeVehicleMod.TRANSMISSION,
		NativeVehicleMod.HORNS,
		NativeVehicleMod.SUSPENSION,
		NativeVehicleMod.ARMOR,

		NativeVehicleMod.FRONT_WHEELS,
		NativeVehicleMod.BACK_WHEELS,

		NativeVehicleMod.PLATE_HOLDER,
		NativeVehicleMod.VANITY_PLATE,
		NativeVehicleMod.TRIM_A,
		NativeVehicleMod.ORNAMENTS,
		NativeVehicleMod.DASHBOARD,
		NativeVehicleMod.DIAL,
		NativeVehicleMod.DOORS_PEAKER,
		NativeVehicleMod.SEATS,
		NativeVehicleMod.STEERING_WHEEL,
		NativeVehicleMod.SHIFTER_LEAVERS,
		NativeVehicleMod.A_PLATE,
		NativeVehicleMod.SPEAKERS,
		NativeVehicleMod.TRUNK,
		NativeVehicleMod.HYDROLIC,
		NativeVehicleMod.ENGINE_BLOCK,
		NativeVehicleMod.AIR_FILTER,
		NativeVehicleMod.STRUTS,
		NativeVehicleMod.ARCH_COVER,
		NativeVehicleMod.AERIALS,
		NativeVehicleMod.TRIM_B,
		NativeVehicleMod.TANK,
		NativeVehicleMod.WINDOWS -> {
			Natives.setVehicleMod(vehicle, id, value, false)
		}

		NativeVehicleMod.LIVERY -> {
			Natives.setVehicleMod(vehicle, id, value, false)
			Natives.setVehicleLivery(vehicle, value)
		}
	}
}

fun NativeVehicleMod.getOn(vehicle: EntityId): Int? {
	return when (this) {
		NativeVehicleMod.TURBO, NativeVehicleMod.SMOKE_ENABLED, NativeVehicleMod.XENON -> {
			if (Natives.isToggleModOn(vehicle, id)) 1 else 0
		}

		NativeVehicleMod.SPOILERS,
		NativeVehicleMod.FRONT_BUMPER,
		NativeVehicleMod.REAR_BUMPER,
		NativeVehicleMod.SIDE_SKIRT,
		NativeVehicleMod.EXHAUST,
		NativeVehicleMod.FRAME,
		NativeVehicleMod.GRILLE,
		NativeVehicleMod.HOOD,
		NativeVehicleMod.FENDER,
		NativeVehicleMod.RIGHT_FENDER,
		NativeVehicleMod.ROOF,

		NativeVehicleMod.ENGINE,
		NativeVehicleMod.BRAKES,
		NativeVehicleMod.TRANSMISSION,
		NativeVehicleMod.HORNS,
		NativeVehicleMod.SUSPENSION,
		NativeVehicleMod.ARMOR,

		NativeVehicleMod.FRONT_WHEELS,
		NativeVehicleMod.BACK_WHEELS,

		NativeVehicleMod.PLATE_HOLDER,
		NativeVehicleMod.VANITY_PLATE,
		NativeVehicleMod.TRIM_A,
		NativeVehicleMod.ORNAMENTS,
		NativeVehicleMod.DASHBOARD,
		NativeVehicleMod.DIAL,
		NativeVehicleMod.DOORS_PEAKER,
		NativeVehicleMod.SEATS,
		NativeVehicleMod.STEERING_WHEEL,
		NativeVehicleMod.SHIFTER_LEAVERS,
		NativeVehicleMod.A_PLATE,
		NativeVehicleMod.SPEAKERS,
		NativeVehicleMod.TRUNK,
		NativeVehicleMod.HYDROLIC,
		NativeVehicleMod.ENGINE_BLOCK,
		NativeVehicleMod.AIR_FILTER,
		NativeVehicleMod.STRUTS,
		NativeVehicleMod.ARCH_COVER,
		NativeVehicleMod.AERIALS,
		NativeVehicleMod.TRIM_B,
		NativeVehicleMod.TANK,
		NativeVehicleMod.WINDOWS -> {
			Natives.getVehicleMod(vehicle, id)
		}

		NativeVehicleMod.LIVERY -> {
			Natives.getVehicleLivery(vehicle)
		}
	}
}