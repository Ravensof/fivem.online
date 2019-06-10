package online.fivem.client.entities

import online.fivem.client.common.GlobalCache
import online.fivem.client.gtav.Client
import online.fivem.common.common.EntityId
import online.fivem.common.gtav.NativeTask
import online.fivem.common.gtav.NativeWeapons

class Ped private constructor(
	entity: EntityId
) : Entity(entity) {

	var armour: Int
		get() = Client.getPedArmour(entity)
		set(value) = Client.addArmourToPed(entity, value - armour)

	val weapon = Weapons(this)

	init {
		if (!Client.doesEntityExist(entity)) throw PedDoesntExistsException("ped $entity doesnt exists")
	}

	fun isTryingToGetInAnyVehicle() = Client.isPedInAnyVehicle(entity) != (Client.getVehiclePedIsUsing(entity) != null)

	fun isInAVehicle() = Client.isPedInAnyVehicle(entity, false)

	fun getVehicleIsInteracted(): Vehicle? {
		Client.getVehiclePedIsUsing(entity)?.let { entity ->
			return Vehicle.newInstance(entity)
		}

		return null
	}

	fun getVehicleIsIn(lastVehicle: Boolean = false): Vehicle? {
		return Client.getVehiclePedIsIn(entity, lastVehicle)?.let { Vehicle.newInstance(it) }
	}

	fun isTaskActive(task: NativeTask) = Client.getIsTaskActive(entity, task.number)

	fun clearTasks() = Client.clearPedTasks(entity)

	fun clearTasksImmediately() = Client.clearPedTasksImmediately(entity)

	fun dropWeapon() {
		Client.setPedDropsWeapon(entity)
	}

	fun setIntoVehicle(vehicle: EntityId, seatIndex: Int) {
		Client.setPedIntoVehicle(entity, vehicle, seatIndex)
	}

	fun setDropsWeaponsWhenDead(toggle: Boolean) {
		Client.setPedDropsWeaponsWhenDead(entity, toggle)
	}

	fun hasGotWeapon(weapon: NativeWeapons): Boolean {
		return Client.hasPedGotWeapon(entity, weapon.name)
	}

	fun giveWeapon(weapon: NativeWeapons, ammo: Int, isHidden: Boolean = false, equipNow: Boolean = false) =
		giveWeapon(weapon.name, ammo, isHidden, equipNow)

	fun giveWeapon(weapon: String, ammo: Int, isHidden: Boolean = false, equipNow: Boolean = false) {
		Client.giveWeaponToPed(entity, weapon, ammo, isHidden, equipNow)
	}

	fun getWeapons(): List<Weapons.Weapon> {
		return NativeWeapons.values().filter { it != NativeWeapons.WEAPON_UNARMED }.mapNotNull { weapon[it] }
	}

	fun removeAllWeapons() = Client.removeAllPedWeapons(entity)

	class PedDoesntExistsException(message: String) : Exception(message)

	class Weapons(private val ped: Ped) {
		operator fun get(weapon: NativeWeapons): Weapon? {
			if (!ped.hasGotWeapon(weapon)) return null

			return Weapon(ped, weapon)
		}

		class Weapon(private val ped: Ped, val weapon: NativeWeapons) {

//			val clipSize = Client.getWeaponClipSize(weapon.hash)

			var ammo
				get() = Client.getAmmoInPedWeapon(ped.entity, weapon.name)
				set(value) = Client.addAmmoToPed(ped.entity, weapon.name, value - ammo)

//			fun drop(){
//				Client.setPedDropsInventoryWeapon(ped.entity, weapon.name)
//			}

			fun remove() {
				Client.removeWeaponFromPed(ped.entity, weapon.name)
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