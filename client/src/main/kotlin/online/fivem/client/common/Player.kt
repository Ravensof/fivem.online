package online.fivem.client.common

import online.fivem.Natives
import online.fivem.client.entities.Ped

class Player(
	val id: Int
) {
	var ped = getPed()
		private set

	var isInvincible: Boolean
		get() = Natives.getPlayerInvincible(id)
		set(value) = Natives.setPlayerInvincible(id, value)

	fun clearWantedLevel() = Natives.clearPlayerWantedLevel(id)

	suspend fun setModel(hash: Int) {
		Natives.setPlayerModel(id, hash)
		ped = getPed()
	}

	fun networkGetLoudness() = Natives.networkGetPlayerLoudness(id)

	private fun getPed() =
		Ped.newInstance(
			Natives.getPlayerPed(id) ?: throw IllegalStateException("player's ped can't be null")
		)
}