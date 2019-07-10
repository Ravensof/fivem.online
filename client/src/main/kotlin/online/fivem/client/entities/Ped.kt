package online.fivem.client.entities

import online.fivem.Natives
import online.fivem.client.common.GlobalCache
import online.fivem.common.common.EntityId
import online.fivem.common.gtav.NativeTask
import online.fivem.common.gtav.NativeWeapon

class Ped private constructor(
	entity: EntityId
) : Entity(entity) {

	var armour: Int
		get() = Natives.getPedArmour(entity)
		set(value) = Natives.addArmourToPed(entity, value - armour)

	val weapon = Weapons(this)

	init {
		if (!Natives.doesEntityExist(entity)) throw PedDoesntExistsException("ped $entity doesnt exists")
	}

	fun isTryingToGetInAnyVehicle() =
		Natives.isPedInAnyVehicle(entity) != (Natives.getVehiclePedIsUsing(entity) != null)

	fun isInAVehicle() = Natives.isPedInAnyVehicle(entity, false)

	fun getVehicleIsInteracted(): Vehicle? {
		Natives.getVehiclePedIsUsing(entity)?.let { entity ->
			return Vehicle.newInstance(entity)
		}

		return null
	}

	fun getVehicleIsIn(lastVehicle: Boolean = false): Vehicle? {
		return Natives.getVehiclePedIsIn(entity, lastVehicle)?.let { Vehicle.newInstance(it) }
	}

	fun isTaskActive(task: NativeTask) = Natives.getIsTaskActive(entity, task.number)

	fun clearTasks() = Natives.clearPedTasks(entity)

	fun clearTasksImmediately() = Natives.clearPedTasksImmediately(entity)

	fun dropWeapon() {
		Natives.setPedDropsWeapon(entity)
	}

	fun setIntoVehicle(vehicle: EntityId, seatIndex: Int) {
		Natives.setPedIntoVehicle(entity, vehicle, seatIndex)
	}

	fun setDropsWeaponsWhenDead(toggle: Boolean) {
		Natives.setPedDropsWeaponsWhenDead(entity, toggle)
	}

	fun hasGotWeapon(weapon: NativeWeapon): Boolean {
		return Natives.hasPedGotWeapon(entity, weapon.code)
	}

	fun giveWeapon(weapon: NativeWeapon, ammo: Int, isHidden: Boolean = false, equipNow: Boolean = false) =
		giveWeapon(weapon.code, ammo, isHidden, equipNow)

	fun giveWeapon(weapon: String, ammo: Int, isHidden: Boolean = false, equipNow: Boolean = false) {
		Natives.giveWeaponToPed(entity, weapon, ammo, isHidden, equipNow)
	}

	fun getWeapons(): List<Weapons.Weapon> {
		return NativeWeapon.values().filter { it != NativeWeapon.WEAPON_UNARMED }.mapNotNull { weapon[it] }
	}

	suspend fun switchOut() = Natives.switchOutPlayer(entity)

	suspend fun switchIn() = Natives.switchInPlayer(entity)

	fun removeAllWeapons() = Natives.removeAllPedWeapons(entity)

	class PedDoesntExistsException(message: String) : Exception(message)

	class Weapons(private val ped: Ped) {
		operator fun get(weapon: NativeWeapon): Weapon? {
			if (!ped.hasGotWeapon(weapon)) return null

			return Weapon(ped, weapon)
		}

		class Weapon(private val ped: Ped, val weapon: NativeWeapon) {

//			val clipSize = Natives.getWeaponClipSize(weapon.hash)

			var ammo
				get() = Natives.getAmmoInPedWeapon(ped.entity, weapon.code)
				set(value) = Natives.addAmmoToPed(ped.entity, weapon.code, value - ammo)

//			fun drop(){
//				Natives.setPedDropsInventoryWeapon(ped.entity, weapon.code)
//			}

			fun remove() {
				Natives.removeWeaponFromPed(ped.entity, weapon.code)
			}
		}
	}

	companion object {

		fun newInstance(entity: EntityId): Ped {
			GlobalCache.getPed(entity)?.let {
				return it
			}

			val ped = Ped(entity)

			GlobalCache.putPed(ped)

			return ped
		}
	}
}