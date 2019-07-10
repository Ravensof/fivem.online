@file:Suppress("FunctionName", "KDocUnresolvedReference")

package online.fivem

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import online.fivem.common.GlobalConfig
import online.fivem.common.common.EntityId
import online.fivem.common.common.Handle
import online.fivem.common.entities.Coordinates
import online.fivem.common.entities.RGB
import online.fivem.common.entities.Time
import online.fivem.common.gtav.NativeControls
import online.fivem.common.gtav.RadioStation
import online.fivem.enums.PauseMenuState
import online.fivem.extensions.invokeNative

object Natives {

	/**
	 * This native removes a specified weapon from your selected ped.
	 * Weapon Hashes: pastebin.com/0wwDZgkF
	 * Example:
	 * C#:
	 * Function.Call(Hash.REMOVE_WEAPON_FROM_PED, Game.Player.Character, 0x99B507EA);
	 * C++:
	 * WEAPON::REMOVE_WEAPON_FROM_PED(PLAYER::PLAYER_PED_ID(), 0x99B507EA);
	 * The code above removes the knife from the player.
	 */
	fun removeWeaponFromPed(ped: EntityId, weaponHash: String) {
		RemoveWeaponFromPed(ped, weaponHash)
	}

	/**
	 * [16/06/2017 by ins1de] :
	 * Drops the weapon object from selected peds and turns it into a pickup.
	 * Offset defines the next position of the weapon, ammo count is the stored ammo in the pickup (if ammoCount == 0, pickup won't be created)
	 * Default offset values (freemode.c):
	 * if (is_ped_walking(player_ped_id()))
	 * {
	 * vVar1 = {0.6f, 4.7f, -0.1f};
	 * }
	 * else if (is_ped_sprinting(player_ped_id()))
	 * {
	 * vVar1 = {0.6f, 5.7f, -0.1f};
	 * }
	 * else if (is_ped_running(player_ped_id()))
	 * {
	 * vVar1 = {0.6f, 4.7f, -0.1f};
	 * }
	 * else
	 * {
	 * vVar1 = {0.4f, 4.7f, -0.1f};
	 * }
	 */
	fun setPedDropsInventoryWeapon(
		ped: EntityId,
		weaponHash: Number,
		xOffset: Number,
		yOffset: Number,
		zOffset: Number,
		ammoCount: Int
	) {
		SetPedDropsInventoryWeapon(ped, weaponHash, xOffset, yOffset, zOffset, ammoCount)
	}

	fun setPedDropsWeapon(ped: EntityId) {
		SetPedDropsWeapon(ped)
	}

	fun setPedDropsWeaponsWhenDead(ped: EntityId, toggle: Boolean) {
		SetPedDropsWeaponsWhenDead(ped, toggle)
	}

	/**
	 * Gives a weapon to PED with a delay, example:
	 * WEAPON::GIVE_DELAYED_WEAPON_TO_PED(PED::PLAYER_PED_ID(), GAMEPLAY::GET_HASH_KEY("WEAPON_PISTOL"), 1000, false)
	 * ----------------------------------------------------------------------------------------------------------------------------------------
	 * Translation table:
	 * pastebin.com/a39K8Nz8
	 */
	fun giveDelayedWeaponToPed(ped: EntityId, weaponHash: Float, ammoCount: Int, equipNow: Boolean = false) {
		GiveDelayedWeaponToPed(ped, weaponHash, ammoCount, equipNow)
	}

	/**
	 * isHidden - ????
	 * All weapon names (add to the list if something is missing), use GAMEPLAY::GET_HASH_KEY((char *)weaponNames[i]) to get get the hash:
	 * static LPCSTR weaponNames[] = {
	 * "WEAPON_KNIFE", "WEAPON_NIGHTSTICK", "WEAPON_HAMMER", "WEAPON_BAT", "WEAPON_GOLFCLUB",
	 * "WEAPON_CROWBAR", "WEAPON_PISTOL", "WEAPON_COMBATPISTOL", "WEAPON_APPISTOL", "WEAPON_PISTOL50",
	 * "WEAPON_MICROSMG", "WEAPON_SMG", "WEAPON_ASSAULTSMG", "WEAPON_ASSAULTRIFLE",
	 * "WEAPON_CARBINERIFLE", "WEAPON_ADVANCEDRIFLE", "WEAPON_MG", "WEAPON_COMBATMG", "WEAPON_PUMPSHOTGUN",
	 * "WEAPON_SAWNOFFSHOTGUN", "WEAPON_ASSAULTSHOTGUN", "WEAPON_BULLPUPSHOTGUN", "WEAPON_STUNGUN", "WEAPON_SNIPERRIFLE",
	 * "WEAPON_HEAVYSNIPER", "WEAPON_GRENADELAUNCHER", "WEAPON_GRENADELAUNCHER_SMOKE", "WEAPON_RPG", "WEAPON_MINIGUN",
	 * "WEAPON_GRENADE", "WEAPON_STICKYBOMB", "WEAPON_SMOKEGRENADE", "WEAPON_BZGAS", "WEAPON_MOLOTOV",
	 * "WEAPON_FIREEXTINGUISHER", "WEAPON_PETROLCAN", "WEAPON_FLARE", "WEAPON_SNSPISTOL", "WEAPON_SPECIALCARBINE",
	 * "WEAPON_HEAVYPISTOL", "WEAPON_BULLPUPRIFLE", "WEAPON_HOMINGLAUNCHER", "WEAPON_PROXMINE", "WEAPON_SNOWBALL",
	 * "WEAPON_VINTAGEPISTOL", "WEAPON_DAGGER", "WEAPON_FIREWORK", "WEAPON_MUSKET", "WEAPON_MARKSMANRIFLE",
	 * "WEAPON_HEAVYSHOTGUN", "WEAPON_GUSENBERG", "WEAPON_HATCHET", "WEAPON_RAILGUN", "WEAPON_COMBATPDW",
	 * "WEAPON_KNUCKLE", "WEAPON_MARKSMANPISTOL", "WEAPON_FLASHLIGHT", "WEAPON_MACHETE", "WEAPON_MACHINEPISTOL",
	 * "WEAPON_SWITCHBLADE", "WEAPON_REVOLVER", "WEAPON_COMPACTRIFLE", "WEAPON_DBSHOTGUN", "WEAPON_FLAREGUN",
	 * "WEAPON_AUTOSHOTGUN", "WEAPON_BATTLEAXE", "WEAPON_COMPACTLAUNCHER", "WEAPON_MINISMG", "WEAPON_PIPEBOMB",
	 * "WEAPON_POOLCUE", "WEAPON_SWEEPER", "WEAPON_WRENCH"
	 * };
	 * ----------------------------------------------------------------------------------------------------------------------------------------
	 * Translation table:
	 * pastebin.com/a39K8Nz8
	 */
	fun giveWeaponToPed(
		ped: EntityId,
		weaponHash: String,
		ammoCount: Int,
		isHidden: Boolean = false,
		equipNow: Boolean = false
	) {
		GiveWeaponToPed(ped, weaponHash, ammoCount, isHidden, equipNow)
	}

	/**
	 * // Returns the size of the default weapon component clip.
	 * Use it like this:
	 * char cClipSize[32];
	 * Hash cur;
	 * if (WEAPON::GET_CURRENT_PED_WEAPON(playerPed, &amp;cur, 1))
	 * {
	 * if (WEAPON::IS_WEAPON_VALID(cur))
	 * {
	 * int iClipSize = WEAPON::GET_WEAPON_CLIP_SIZE(cur);
	 * sprintf_s(cClipSize, "ClipSize: %.d", iClipSize);
	 * vDrawString(cClipSize, 0.5f, 0.5f);
	 * }
	 * }
	 */
	fun getWeaponClipSize(weaponHash: Number): Int {
		return GetWeaponClipSize(weaponHash)
	}

	/**
	 * WEAPON::GET_AMMO_IN_PED_WEAPON(PLAYER::PLAYER_PED_ID(), a_0)
	 * From decompiled scripts
	 * Returns total ammo in weapon
	 * GTALua Example :
	 * natives.WEAPON.GET_AMMO_IN_PED_WEAPON(plyPed, WeaponHash)
	 */
	fun getAmmoInPedWeapon(ped: EntityId, weaponHash: String): Int {
		return GetAmmoInPedWeapon(ped, weaponHash)
	}

	fun addAmmoToPed(ped: EntityId, weaponHash: String, ammo: Int) {
		AddAmmoToPed(ped, weaponHash, ammo)
	}

	/**
	 * Same as SET_PED_ARMOUR, but ADDS 'amount' to the armor the Ped already has.
	 */
	fun addArmourToPed(ped: EntityId, amount: Number) {
		AddArmourToPed(ped, amount)
	}

	fun getPedArmour(ped: EntityId): Int {
		return GetPedArmour(ped)
	}

	/**
	 * Set modKit to 0 if you plan to call SET_VEHICLE_MOD. That's what the game does. Most body modifications through SET_VEHICLE_MOD will not take effect until this is set to 0.
	 * ---------
	 * Setting the modKit to 0 also seems to load some livery related vehicle information. For example, using GET_LIVERY_NAME() will return NULL if you haven't set the modKit to 0 in advance. As soon as you set it to 0, GET_LIVERY_NAME() will work properly.
	 */
	fun setVehicleModKit(vehicle: EntityId, modKit: Int) {
		SetVehicleModKit(vehicle, modKit)
	}

	fun getVehicleModKit(vehicle: EntityId): Int {
		return GetVehicleModKit(vehicle)
	}

	fun setVehicleExclusiveDriver_2(vehicle: EntityId, ped: EntityId, p2: Number = 0) {
		SetVehicleExclusiveDriver_2(vehicle, ped, p2)
	}

	/**
	 * Ped: The ped to warp.
	 * vehicle: The vehicle to warp the ped into.
	 * Seat_Index: [-1 is driver seat, -2 first free passenger seat]
	 * Moreinfo of Seat Index
	 * DriverSeat = -1
	 * Passenger = 0
	 * Left Rear = 1
	 * RightRear = 2
	 */
	fun setPedIntoVehicle(ped: EntityId, vehicle: EntityId, seatIndex: Int) {
		SetPedIntoVehicle(ped, vehicle, seatIndex)
	}

	/**
	 * from docks_heistb.c4:
	 * AI::GET_IS_TASK_ACTIVE(PLAYER::PLAYER_PED_ID(), 2))
	 * Known Tasks: pastebin.com/2gFqJ3Px
	 */
	fun getIsTaskActive(ped: EntityId, taskNumber: Int): Boolean {
		return GetIsTaskActive(ped, taskNumber) == 1
	}

	fun setPlayerHealthRechargeMultiplier(player: Int, regenRate: Float) {
		SetPlayerHealthRechargeMultiplier(player, regenRate)
	}

	/**
	 * Plays ambient speech. See also _0x444180DB.
	 * ped: The ped to play the ambient speech.
	 * speechName: Name of the speech to play, eg. "GENERIC_HI".
	 * speechParam: Can be one of the following:
	 * SPEECH_PARAMS_STANDARD
	 * SPEECH_PARAMS_ALLOW_REPEAT
	 * SPEECH_PARAMS_BEAT
	 * SPEECH_PARAMS_FORCE
	 * SPEECH_PARAMS_FORCE_FRONTEND
	 * SPEECH_PARAMS_FORCE_NO_REPEAT_FRONTEND
	 * SPEECH_PARAMS_FORCE_NORMAL
	 * SPEECH_PARAMS_FORCE_NORMAL_CLEAR
	 * SPEECH_PARAMS_FORCE_NORMAL_CRITICAL
	 * SPEECH_PARAMS_FORCE_SHOUTED
	 * SPEECH_PARAMS_FORCE_SHOUTED_CLEAR
	 * SPEECH_PARAMS_FORCE_SHOUTED_CRITICAL
	 * SPEECH_PARAMS_FORCE_PRELOAD_ONLY
	 * SPEECH_PARAMS_MEGAPHONE
	 * SPEECH_PARAMS_HELI
	 * SPEECH_PARAMS_FORCE_MEGAPHONE
	 * SPEECH_PARAMS_FORCE_HELI
	 * SPEECH_PARAMS_INTERRUPT
	 * SPEECH_PARAMS_INTERRUPT_SHOUTED
	 * SPEECH_PARAMS_INTERRUPT_SHOUTED_CLEAR
	 * SPEECH_PARAMS_INTERRUPT_SHOUTED_CRITICAL
	 * SPEECH_PARAMS_INTERRUPT_NO_FORCE
	 * SPEECH_PARAMS_INTERRUPT_FRONTEND
	 * SPEECH_PARAMS_INTERRUPT_NO_FORCE_FRONTEND
	 * SPEECH_PARAMS_ADD_BLIP
	 * SPEECH_PARAMS_ADD_BLIP_ALLOW_REPEAT
	 * SPEECH_PARAMS_ADD_BLIP_FORCE
	 * SPEECH_PARAMS_ADD_BLIP_SHOUTED
	 * SPEECH_PARAMS_ADD_BLIP_SHOUTED_FORCE
	 * SPEECH_PARAMS_ADD_BLIP_INTERRUPT
	 * SPEECH_PARAMS_ADD_BLIP_INTERRUPT_FORCE
	 * SPEECH_PARAMS_FORCE_PRELOAD_ONLY_SHOUTED
	 * SPEECH_PARAMS_FORCE_PRELOAD_ONLY_SHOUTED_CLEAR
	 * SPEECH_PARAMS_FORCE_PRELOAD_ONLY_SHOUTED_CRITICAL
	 * SPEECH_PARAMS_SHOUTED
	 * SPEECH_PARAMS_SHOUTED_CLEAR
	 * SPEECH_PARAMS_SHOUTED_CRITICAL
	 * Note: A list of Name and Parameters can be found here pastebin.com/1GZS5dCL
	 * Full list of speeches and voices names by some spanish shitbag: gist.github.com/alexguirre/0af600eb3d4c91ad4f900120a63b8992
	 */
	fun playAmbientSpeech1(ped: EntityId, speechName: String, speechParam: String) {
		PlayAmbientSpeech1(ped, speechName, speechParam)
	}

	fun findKvp(handle: Handle): String? {
		return FindKvp(handle)
	}

	fun endFindKvp(handle: Handle) {
		EndFindKvp(handle)
	}

	fun startFindKvp(prefix: String): Handle {
		return StartFindKvp(prefix)
	}

	fun deleteResourceKvp(key: String) {
		DeleteResourceKvp(key)
	}

	fun setResourceKvp(key: String, value: String) {
		SetResourceKvp(key, value)
	}

	fun setResourceKvpFloat(key: String, value: Float) {
		SetResourceKvpFloat(key, value)
	}

	fun setResourceKvpInt(key: String, value: Int) {
		SetResourceKvpInt(key, value)
	}

	fun getResourceKvpFloat(key: String): Float {
		return GetResourceKvpFloat(key)
	}

	fun getResourceKvpInt(key: String): Int {
		return GetResourceKvpInt(key)
	}

	fun getResourceKvpString(key: String): String? {
		return GetResourceKvpString(key)
	}

	/**
	 * p1 and p2 have no effect
	 * maybe a quick disassembly will tell us what they do
	 * the statement below seems to be false. when I tried it with 2 vehicles:
	 * if p2 is set to true, the both entities won't collide with the other until the distance between them is above 4 meters.
	 */
	//DetachEntity(PlayerPedId(), true, false)
	fun detachEntity(entity: EntityId, p1: Boolean, collision: Boolean) {
		DetachEntity(entity, p1, collision)
	}

	/**
	 * Attaches entity1 to bone (boneIndex) of entity2.
	 * boneIndex - this is different to boneID, use GET_PED_BONE_INDEX to get the index from the ID. use the index for attaching to specific bones. entity1 will be attached to entity2's centre if bone index given doesn't correspond to bone indexes for that entity type.
	 * useSoftPinning - when 2 entities with collision collide and form into a ball they will break the attachment of the entity that they were attached to. Or when an entity is attached far away and then the resets.
	 * collision - controls collision between the two entities (FALSE disables collision).
	 * isPed - pitch doesnt work when false and roll will only work on negative numbers (only peds)
	 * vertexIndex - position of vertex
	 * fixedRot - if false it ignores entity vector
	 */
	//AttachEntityToEntity(PlayerPedId(), GetPlayerPed(GetPlayerFromServerId(draggedBy)), 4103, 11816, 0.48, 0.00, 0.0, 0.0, 0.0, 0.0, false, false, false, false, 2, true)
	fun attachEntityToEntity(
		entity1: EntityId,
		entity2: EntityId,
		boneIndex: Number,
		xPos: Number,
		yPos: Number,
		zPos: Number,
		xRot: Number,
		yRot: Number,
		zRot: Number,
		p9: Boolean,
		useSoftPinning: Boolean,
		collision: Boolean,
		isPed: Boolean,
		vertexIndex: Number,
		fixedRot: Boolean
	) {
		AttachEntityToEntity(
			entity1,
			entity2,
			boneIndex,
			xPos,
			yPos,
			zPos,
			xRot,
			yRot,
			zRot,
			p9,
			useSoftPinning,
			collision,
			isPed,
			vertexIndex,
			fixedRot
		)
	}

	fun networkSetInSpectatorMode(toggle: Boolean, playerPed: EntityId) {
		NetworkSetInSpectatorMode(toggle, playerPed)
	}

	/**
	 * Creates a mobile phone of the specified type.
	 * Possible phone types:
	 * 0 - Default phone / Michael's phone
	 * 1 - Trevor's phone
	 * 2 - Franklin's phone
	 * 4 - Prologue phone
	 * These values represent bit flags, so a value of '3' would toggle Trevor and Franklin's phones together, causing unexpected behavior and most likely crash the game.
	 */
	fun createMobilePhone(phoneId: Int) {
		CreateMobilePhone(phoneId)
	}

	/**
	 * Destroys the currently active mobile phone.
	 */
	fun destroyMobilePhone() {
		DestroyMobilePhone()
	}

	/**
	 * traceType is always 17 in the scripts.
	 * There is other codes used for traceType:
	 * 19 - in jewelry_prep1a
	 * 126 - in am_hunt_the_beast
	 * 256 &amp; 287 - in fm_mission_controller
	 */
	fun hasEntityClearLosToEntity(entity1: EntityId, entity2: EntityId, traceType: Int = 17): Boolean {
		return HasEntityClearLosToEntity(entity1, entity2, traceType) == 1
	}

	fun networkIsPlayerActive(player: Int): Boolean {
		return NetworkIsPlayerActive(player) == 1
	}

	/**
	 * enum IncidentTypes
	 * {
	 * FireDepartment = 3,
	 * Paramedics = 5,
	 * Police = 7,
	 * PedsInCavalcades = 11,
	 * Merryweather = 14
	 * };
	 * As for the 'police' incident, it will call police cars to you, but unlike PedsInCavalcades &amp; Merryweather they
	 * won't start shooting at you unless you shoot first or shoot at them. The top 2 however seem to cancel theirselves
	 * if there is noone dead around you or a fire. I only figured them out as I found out the 3rd param is definately
	 * the amountOfPeople and they called incident 3 in scripts with 4 people (which the firetruck has) and incident 5
	 * with 2 people (which the ambulence has). The 4 param I cant say is radius, but for the pedsInCavalcades and
	 * Merryweather R* uses 0.0f and for the top 3 (Emergency Services) they use 3.0f.
	 * Side Note: It seems calling the pedsInCavalcades or Merryweather then removing it seems to break you from calling
	 * the EmergencyEvents and I also believe pedsInCavalcades. (The V cavalcades of course not IV).
	 *
	 * Side Note 2: I say it breaks as if you call this proper,
	 * if(CREATE_INCIDENT) etc it will return false if you do as I said above.
	 * =====================================================
	 */
	fun createIncident(
		incidentType: Int,
		x: Float,
		y: Float,
		z: Float,
		amountOfPeople: Int,
		radius: Float,
		outIncidentID: Int
	): Number {
		return CreateIncident(incidentType, x, y, z, amountOfPeople, radius, outIncidentID)
	}

	/**
	 * Mixes two weather types. If percentWeather2 is set to 0.0f, then the weather will be entirely of weatherType1, if
	 * it is set to 1.0f it will be entirely of weatherType2. If it's set somewhere in between, there will be a mixture
	 * of weather behaviors. To test, try this in the RPH console, and change the float to different values between 0 and 1:
	 * execute "NativeFunction.Natives.x578C752848ECFA0C(Game.GetHashKey(""RAIN""), Game.GetHashKey(""SMOG""), 0.50f);
	 * Note that unlike most of the other weather natives, this native takes the hash of the weather name, not the plain
	 * string. These are the weather names and their hashes:
	 * CLEAR	0x36A83D84
	 * EXTRASUNNY	0x97AA0A79
	 * CLOUDS	0x30FDAF5C
	 * OVERCAST	0xBB898D2D
	 * RAIN	0x54A69840
	 * CLEARING	0x6DB1A50D
	 * THUNDER	0xB677829F
	 * SMOG	0x10DCF4B5
	 * FOGGY	0xAE737644
	 * XMAS	0xAAC9C895
	 * SNOWLIGHT	0x23FB812B
	 * BLIZZARD	0x27EA2814
	 *  -- [[ OLD INVALID INFO BELOW ]]
	 * Not tested. Based purely on disassembly. Instantly sets the weather to sourceWeather, then transitions to
	 * targetWeather over the specified transitionTime in seconds.
	 * If an invalid hash is specified for sourceWeather, the current weather type will be used.
	 * If an invalid hash is specified for targetWeather, the next weather type will be used.
	 * If an invalid hash is specified for both sourceWeather and targetWeather, the function just changes the transition
	 * time of the current transition.
	 */
	@Deprecated("use Client.setWeatherTypeTransition(weatherType1: NativeWeather, weatherType2: NativeWeather, percentWeather2: Float)")
	fun setWeatherTypeTransition(weatherType1: String, weatherType2: String, percentWeather2: Float) {
		SetWeatherTypeTransition(weatherType1, weatherType2, percentWeather2)
	}

	/**
	 * Forces footstep tracks on all surfaces.
	 */
	fun setForcePedFootstepsTracks(toggle: Boolean) {
		SetForcePedFootstepsTracks(toggle)
	}

	/**
	 * Forces vehicle trails on all surfaces.
	 */
	fun setForceVehicleTrails(toggle: Boolean) {
		SetForceVehicleTrails(toggle)
	}

	/**
	 * The following weatherTypes are used in the scripts:
	 * "CLEAR"
	 * "EXTRASUNNY"
	 * "CLOUDS"
	 * "OVERCAST"
	 * "RAIN"
	 * "CLEARING"
	 * "THUNDER"
	 * "SMOG"
	 * "FOGGY"
	 * "XMAS"
	 * "SNOWLIGHT"
	 * "BLIZZARD"
	 */
	@Deprecated("use NativeWeather.setWeatherTypeNowPersist()")
	fun setWeatherTypeNowPersist(weatherType: String) {
		SetWeatherTypeNowPersist(weatherType)
	}

	/**
	 * The following weatherTypes are used in the scripts:
	 * "CLEAR"
	 * "EXTRASUNNY"
	 * "CLOUDS"
	 * "OVERCAST"
	 * "RAIN"
	 * "CLEARING"
	 * "THUNDER"
	 * "SMOG"
	 * "FOGGY"
	 * "XMAS"
	 * "SNOWLIGHT"
	 * "BLIZZARD"
	 */
	@Deprecated("use NativeWeather.setWeatherTypeNow()")
	fun setWeatherTypeNow(weatherType: String) {
		SetWeatherTypeNow(weatherType)
	}

	/**
	 * The following weatherTypes are used in the scripts:
	 * "CLEAR"
	 * "EXTRASUNNY"
	 * "CLOUDS"
	 * "OVERCAST"
	 * "RAIN"
	 * "CLEARING"
	 * "THUNDER"
	 * "SMOG"
	 * "FOGGY"
	 * "XMAS"
	 * "SNOWLIGHT"
	 * "BLIZZARD"
	 */
	@Deprecated("use NativeWeather.setWeatherTypePersist()")
	fun setWeatherTypePersist(weatherType: String) {
		SetWeatherTypePersist(weatherType)
	}

	fun clearWeatherTypePersist() {
		ClearWeatherTypePersist()
	}

	fun clearOverrideWeather() {
		ClearOverrideWeather()
	}

	/**
	 * Disables all emissive textures and lights like city lights, car lights, cop car lights. Particles still emit light
	 * Used in Humane Labs Heist for EMP.
	 */
	@Deprecated("use API.setBlackOut()")
	fun setBlackout(enable: Boolean) {
		SetBlackout(enable)
	}

	@Deprecated("use NativeWeather.setOverTime(time)")
	fun setWeatherTypeOverTime(weatherType: String, time: Float) {
		SetWeatherTypeOverTime(weatherType, time)
	}

	fun networkSetTalkerProximity(playerId: Int) {
		NetworkSetTalkerProximity(playerId)
	}

	/**
	 * returns true if someone is screaming or talking in a microphone
	 */
	fun networkIsPlayerTalking(player: Int): Boolean {
		return NetworkIsPlayerTalking(player) == 1
	}

	/**
	 * Has the entity1 got a clear line of sight to the other entity2 from the direction entity1 is facing.
	 * This is one of the most CPU demanding BOOL natives in the game; avoid calling this in things like nested for-loops
	 */
	fun hasEntityClearLosToEntityInFront(entity1: EntityId, entity2: EntityId): Boolean {
		return HasEntityClearLosToEntityInFront(entity1, entity2) == 1
	}

	/**
	 * R* uses this to hear all player when spectating.
	 * It allows you to hear other online players when their chat is on none, crew and or friends
	 */
	fun networkOverrideReceiveRestrictions(player: Int, toggle: Boolean) {
		NetworkOverrideReceiveRestrictions(player, toggle)
	}

	/**
	 * Console Hash: 0x6C344AE3
	 * "NETWORK_OVERRIDE_SEND_RESTRICTIONS" is right, but dev-c put a _ by default.
	 * This is used alongside the native,
	 * 'NETWORK_OVERRIDE_RECEIVE_RESTRICTIONS'. Read it's description for more info.
	 */
	fun networkOverrideSendRestrictions(player: Int, toggle: Boolean) {
		NetworkOverrideSendRestrictions(player, toggle)
//		invokeNative("_NETWORK_OVERRIDE_SEND_RESTRICTIONS", player, toggle)
	}

	/**
	 * Convert a world coordinate into its relative screen coordinate.  (WorldToScreen)
	 * Returns a boolean; whether or not the operation was successful. It will return false if the coordinates given are not visible to the rendering camera.
	 * For .NET users...
	 * VB:
	 * Public Shared Function World3DToScreen2d(pos as vector3) As Vector2
	 * Dim x2dp, y2dp As New Native.OutputArgument
	 * Native.Function.Call(Of Boolean)(Native.Hash.GET_SCREEN_COORD_FROM_WORLD_COORD , pos.x, pos.y, pos.z, x2dp, y2dp)
	 * Return New Vector2(x2dp.GetResult(Of Single), y2dp.GetResult(Of Single))
	 * End Function
	 * C#:
	 * Vector2 World3DToScreen2d(Vector3 pos)
	 * {
	 * var x2dp = new OutputArgument();
	 * var y2dp = new OutputArgument();
	 * Function.Call&lt;bool&gt;(Hash.GET_SCREEN_COORD_FROM_WORLD_COORD , pos.X, pos.Y, pos.Z, x2dp, y2dp);
	 * return new Vector2(x2dp.GetResult&lt;float&gt;(), y2dp.GetResult&lt;float&gt;());
	 * }
	 * //USE VERY SMALL VALUES FOR THE SCALE OF RECTS/TEXT because it is dramatically larger on screen than in 3D, e.g '0.05' small.
	 * Used to be called _WORLD3D_TO_SCREEN2D
	 * I thought we lost you from the scene forever. It does seem however that calling SET_DRAW_ORIGIN then your natives, then ending it. Seems to work better for certain things such as keeping boxes around people for a predator missile e.g.
	 */
	fun world3dToScreen2d(worldX: Number, worldY: Number, worldZ: Number): Pair<Float, Float>? {
		val result = World3dToScreen2d(worldX, worldY, worldZ)

		if (result[0] != 1) return null

		return result[1].toFloat() to result[2].toFloat()
	}

	fun getPlayerWantedLevel(player: Int): Int {
		return GetPlayerWantedLevel(player).toInt()
	}

	fun canPedHearPlayer(player: Int, ped: EntityId): Boolean {
		return CanPedHearPlayer(player, ped) == 1
	}

	/**
	 * Returns true if ped1 can see ped2 in their line of vision
	 */
	fun canPedSeePed(ped1: EntityId, ped2: EntityId): Boolean {
		return CanPedSeePed(ped1, ped2) == 1
	}

	/**
	 * Works in Singleplayer too.
	 * Actually has a 4th param (BOOL) that sets byte_14273C46C (in b944) to whatever was passed to p3.
	 */
	fun networkOverrideClockTime(hours: Int, minutes: Int, seconds: Int) {
		NetworkOverrideClockTime(hours, minutes, seconds)
	}

	fun getClockDayOfMonth(): Int {
		return GetClockDayOfMonth()
	}

	fun getClockMonth(): Int {
		return GetClockMonth()
	}

	fun getClockYear(): Int {
		return GetClockYear()
	}

	/**
	 * Gets the current ingame hour, expressed without zeros. (09:34 will be represented as 9)
	 */
	fun getClockHours(): Int {
		return GetClockHours()
	}

	/**
	 * Gets the current ingame clock minute.
	 */
	fun getClockMinutes(): Int {
		return GetClockMinutes()
	}

	/**
	 * Gets the current ingame clock second. Note that ingame clock seconds change really fast since a day in GTA is only 48 minutes in real life.
	 */
	fun getClockSeconds(): Int {
		return GetClockSeconds()
	}

	/**
	 * x/y/z - Location of a vertex (in world coords), presumably.
	 * ----------------
	 * x1, y1, z1     : Coordinates for the first point
	 * x2, y2, z2     : Coordinates for the second point
	 * x3, y3, z3     : Coordinates for the third point
	 * r, g, b, alpha : Color with RGBA-Values
	 * Keep in mind that only one side of the drawn triangle is visible: It's the side, in which the vector-product of the vectors heads to: (b-a)x(c-a) Or (b-a)x(c-b).
	 * But be aware: The function seems to work somehow differently. I have trouble having them drawn in rotated orientation. Try it yourself and if you somehow succeed, please edit this and post your solution.
	 * I recommend using a predefined function to call this.
	 * [VB.NET]
	 * Public Sub DrawPoly(a As Vector3, b As Vector3, c As Vector3, col As Color)
	 * [Function].Call(Hash.DRAW_POLY, a.X, a.Y, a.Z, b.X, b.Y, b.Z, c.X, c.Y, c.Z, col.R, col.G, col.B, col.A)
	 * End Sub
	 * [C#]
	 * public void DrawPoly(Vector3 a, Vector3 b, Vector3 c, Color col)
	 * {
	 * Function.Call(Hash.DRAW_POLY, a.X, a.Y, a.Z, b.X, b.Y, b.Z, c.X, c.Y, c.Z, col.R, col.G, col.B, col.A);
	 * }
	 * BTW: Intersecting triangles are not supported: They overlap in the order they were called.
	 */
	fun drawPoly(
		x1: Number,
		y1: Number,
		z1: Number,
		x2: Number,
		y2: Number,
		z2: Number,
		x3: Number,
		y3: Number,
		z3: Number,
		red: Int,
		green: Int,
		blue: Int,
		alpha: Int = 255
	) {
		DrawPoly(x1, y1, z1, x2, y2, z2, x3, y3, z3, red, green, blue, alpha)
	}

	/**
	 * Sets the tire smoke's color of this vehicle.
	 * vehicle: The vehicle that is the target of this method.
	 * r: The red level in the RGB color code.
	 * g: The green level in the RGB color code.
	 * b: The blue level in the RGB color code.
	 * Note:
	 * setting r,g,b to 0 will give the car independance day tyre smoke
	 */
	fun setVehicleTyreSmokeColor(vehicle: EntityId, r: Int, g: Int, b: Int) {
		SetVehicleTyreSmokeColor(vehicle, r, g, b)
	}

	fun getVehicleTyreSmokeColor(vehicle: EntityId): RGB {
		val color = GetVehicleTyreSmokeColor(vehicle)
		return RGB(color[0], color[1], color[2])
	}

	/**
	 * Sets the color of the neon lights of the specified vehicle.
	 * More info: pastebin.com/G49gqy8b
	 */
	fun setVehicleNeonLightsColour(vehicle: EntityId, r: Int, g: Int, b: Int) {
		SetVehicleNeonLightsColour(vehicle, r, g, b)
	}

	/**
	 * Gets the color of the neon lights of the specified vehicle.
	 * See _SET_VEHICLE_NEON_LIGHTS_COLOUR (0x8E0A582209A62695) for more information
	 */
	fun getVehicleNeonLightsColour(vehicle: EntityId): RGB {
		val color = GetVehicleNeonLightsColour(vehicle)
		return RGB(color[0], color[1], color[2])
	}

	/**
	 * Sets the neon lights of the specified vehicle on/off.
	 * Indices:
	 * 0 = Left
	 * 1 = Right
	 * 2 = Front
	 * 3 = Back
	 */
	fun setVehicleNeonLightEnabled(vehicle: EntityId, index: Int, toggle: Boolean) {
		SetVehicleNeonLightEnabled(vehicle, index, toggle)
	}

	/**
	 * indices:
	 * 0 = Left
	 * 1 = Right
	 * 2 = Front
	 * 3 = Back
	 */
	fun isVehicleNeonLightEnabled(vehicle: EntityId, index: Int): Boolean {
		return IsVehicleNeonLightEnabled(vehicle, index) == 1
	}

	/**
	 * enum WindowTints
	 * {
	 * WINDOWTINT_NONE,
	 * WINDOWTINT_PURE_BLACK,
	 * WINDOWTINT_DARKSMOKE,
	 * WINDOWTINT_LIGHTSMOKE,
	 * WINDOWTINT_STOCK,
	 * WINDOWTINT_LIMO,
	 * WINDOWTINT_GREEN
	 * };
	 */
	fun setVehicleWindowTint(vehicle: EntityId, tint: Int) {
		SetVehicleWindowTint(vehicle, tint)
	}

	fun getVehicleWindowTint(vehicle: EntityId): Int {
		return GetVehicleWindowTint(vehicle)
	}

	/**
	 * Plates:
	 * Blue/White
	 * Yellow/black
	 * Yellow/Blue
	 * Blue/White2
	 * Blue/White3
	 * Yankton
	 */
	fun setVehicleNumberPlateTextIndex(vehicle: EntityId, plateIndex: Int) {
		SetVehicleNumberPlateTextIndex(vehicle, plateIndex)
	}

	/**
	 * Returns the PlateType of a vehicle
	 * Blue_on_White_1 = 3,
	 * Blue_on_White_2 = 0,
	 * Blue_on_White_3 = 4,
	 * Yellow_on_Blue = 2,
	 * Yellow_on_Black = 1,
	 * North_Yankton = 5,
	 */
	fun getVehicleNumberPlateTextIndex(elegy: EntityId): Int {
		return GetVehicleNumberPlateTextIndex(elegy)
	}

	/**
	 * Note: Only seems to work on Emergency Vehicles
	 */
	fun setVehicleLivery(vehicle: EntityId, liveryIndex: Int) {
		SetVehicleLivery(vehicle, liveryIndex)
	}

	fun getVehicleLivery(trailers2: EntityId): Int? {
		return GetVehicleLivery(trailers2).takeIf { it != -1 }
	}

	/**
	 * In b944, there are 50 (0 - 49) mod types.
	 * Sets the vehicle mod.
	 * The vehicle must have a mod kit first.
	 * Any out of range ModIndex is stock.
	 * #Mod Type
	 * Spoilers
	 * Front Bumper
	 * Rear Bumper
	 * Side Skirt
	 * Exhaust
	 * Frame
	 * Grille
	 * Hood
	 * Fender
	 * Right Fender
	 * Roof
	 * Engine
	 * Brakes
	 * Transmission
	 * Horns - 14 (modIndex from 0 to 51)
	 * Suspension
	 * Armor
	 * Front Wheels
	 * Back Wheels - 24 //only for motocycles
	 * Plate holders
	 * Trim Design
	 * Ornaments
	 * Dial Design
	 * Steering Wheel
	 * Shifter Leavers
	 * Plaques
	 * Hydraulics
	 * Livery
	 * ENUMS: pastebin.com/QzEAn02v
	 */
	fun setVehicleMod(vehicle: EntityId, modType: Int, modIndex: Int, customTires: Boolean) {
		SetVehicleMod(vehicle, modType, modIndex, customTires)
	}

	/**
	 * In b944, there are 50 (0 - 49) mod types.
	 * Returns -1 if the vehicle mod is stock
	 */
	fun getVehicleMod(vehicle: EntityId, modType: Int): Int? {
		return GetVehicleMod(vehicle, modType).takeIf { it != -1 }
	}

	/**
	 * Returns the model hash from the entity
	 * Sometimes throws an exception, idk what causes it though.
	 */
	fun getEntityModel(entity: EntityId): Int {
		return GetEntityModel(entity)
	}

	fun setVehicleExtraColours(vehicle: EntityId, pearlescentColor: Int, wheelColor: Int) {
		SetVehicleExtraColours(vehicle, pearlescentColor, wheelColor)
	}

	fun getVehicleExtraColours(vehicle: EntityId): Pair<Int, Int> {
		val colors = GetVehicleExtraColours(vehicle)
		return colors[0] to colors[1]
	}

	/**
	 * colorPrimary &amp; colorSecondary are the paint index for the vehicle.
	 * For a list of valid paint indexes, view: pastebin.com/pwHci0xK
	 * -------------------------------------------------------------------------
	 * Use this to get the number of color indices: pastebin.com/RQEeqTSM
	 * Note: minimum color index is 0, maximum color index is (numColorIndices - 1)
	 */
	fun setVehicleColours(vehicle: EntityId, colorPrimary: Int, colorSecondary: Int = colorPrimary) {
		SetVehicleColours(vehicle, colorPrimary, colorSecondary)
	}

	fun getVehicleColours(vehicle: EntityId): Pair<Int, Int> {
		val colors = GetVehicleColours(vehicle)
		return colors[0] to colors[1]
	}

	/**
	 * Can the player control himself, used to disable controls for player for things like a cutscene.
	 * ---
	 * You can't disable controls with this, use SET_PLAYER_CONTROL(...) for this.
	 */
	fun isPlayerControlOn(player: Int): Boolean {
		return IsPlayerControlOn(player) == 1
	}

	/**
	 * Vehicle power multiplier.
	 * Does not have to be looped each frame. Can be set once.
	 * Values lower than 1f don't work.
	 * Note: If the value is set with GET_RANDOM_FLOAT_IN_RANGE, the vehicle will have an absurdly high ammount of power, and will become almost undrivable for the player or NPCs. The range doesn't seem to matter.
	 * An high value like 10000000000f will visually remove the wheels that apply the power (front wheels for FWD, rear wheels for RWD), but the power multiplier will still apply, and the wheels still work.
	 * ------
	 * value is a percentage bump which affects directly the parameter known as fInitialDriveForce in handling.meta. For example:
	 * VEHICLE::_SET_VEHICLE_ENGINE_POWER_MULTIPLIER(myVehicle, 30.0)
	 * will have this effect: DriveForce *= 1.3
	 */
	fun setVehicleEnginePowerMultiplier(vehicle: EntityId, value: Double) {
		SetVehicleEnginePowerMultiplier(vehicle, value)
	}

	/**
	 * 0: Sport
	 * 1: Muscle
	 * 2: Lowrider
	 * 3: SUV
	 * 4: Offroad
	 * 5: Tuner
	 * 6: Bike Wheels
	 * 7: High End
	 */
	fun setVehicleWheelType(vehicle: EntityId, wheelType: Int) {
		SetVehicleWheelType(vehicle, wheelType)
	}

	/**
	 * Returns an int
	 * Wheel Types:
	 * 0: Sport
	 * 1: Muscle
	 * 2: Lowrider
	 * 3: SUV
	 * 4: Offroad
	 * 5: Tuner
	 * 6: Bike Wheels
	 * 7: High End
	 * Tested in Los Santos Customs
	 */
	fun getVehicleWheelType(vehicle: EntityId): Int {
		return GetVehicleWheelType(vehicle)
	}

	/**
	 * Public Function isVehicleOnAllWheels(vh As Vehicle) As Boolean
	 * Return Native.Function.Call(Of Boolean)(Hash.IS_VEHICLE_ON_ALL_WHEELS, vh)
	 * End Function
	 */
	fun isVehicleOnAllWheels(vehicle: EntityId): Boolean {
		return IsVehicleOnAllWheels(vehicle) == 1
	}

	fun isVehiclePreviouslyOwnedByPlayer(vehicle: EntityId): Boolean {
		return IsVehiclePreviouslyOwnedByPlayer(vehicle) == 1
	}

	/**
	 * 1000 is max health
	 * Begins leaking gas at around 650 health
	 */
	fun setVehiclePetrolTankHealth(vehicle: EntityId, health: Double) {
		SetVehiclePetrolTankHealth(vehicle, health)
	}

	/**
	 * p2 often set to 1000.0 in the decompiled scripts.
	 */
	fun setVehicleBodyHealth(vehicle: EntityId, value: Int) {
		SetVehicleBodyHealth(vehicle, value)
	}

	/**
	 * 1
	 * 2 - CARLOCK_LOCKED (locked)
	 * 3
	 * 4 - CARLOCK_LOCKED_PLAYER_INSIDE (can get in, can't leave)
	 * (maybe, these are leftovers from GTA:VC)
	 * 5
	 * 6
	 * 7
	 * (source: GTA VC miss2 leak, matching constants for 0/2/4, testing)
	 * They use 10 in am_mp_property_int, don't know what it does atm.
	 */
	fun setVehicleDoorsLocked(vehicle: EntityId, doorLockStatus: Int) {
		SetVehicleDoorsLocked(vehicle, doorLockStatus)
	}

	/**
	 * 1000 is max health
	 * Begins leaking gas at around 650 health
	 * -999.90002441406 appears to be minimum health, although nothing special occurs &lt;- false statement
	 * -------------------------
	 * Minimum: -4000
	 * Maximum: 1000
	 * -4000: Engine is destroyed
	 * 0 and below: Engine catches fire and health rapidly declines
	 * 300: Engine is smoking and losing functionality
	 * 1000: Engine is perfect
	 */
	fun setVehicleEngineHealth(vehicle: EntityId, health: Double) {
		SetVehicleEngineHealth(vehicle, health)
	}

	/**
	 * Player won't be able to drive the car or enter it, unless you task him to get into any other seat than the driver one.
	 */
	fun setVehicleUndriveable(vehicle: EntityId, toggle: Boolean) {
		SetVehicleUndriveable(vehicle, toggle)
	}

	/**
	 * Displays the current ROLL axis of the entity [-180.0000/180.0000+]
	 * (Sideways Roll) such as a vehicle tipped on its side
	 */
	fun getEntityRoll(entity: EntityId): Double {
		return GetEntityRoll(entity).toDouble()
	}

	/**
	 * &lt;1.0 - Decreased torque
	 * =1.0 - Default torque
	 * &gt;1.0 - Increased torque
	 * Negative values will cause the vehicle to go backwards instead of forwards while accelerating.
	 * value - is between 0.2 and 1.8 in the decompiled scripts.
	 * This needs to be called every frame to take effect.
	 */
	fun setVehicleEngineTorqueMultiplier(vehicle: EntityId, value: Double) {
		SetVehicleEngineTorqueMultiplier(vehicle, value)
	}

	/**
	 * Sets a handling override for a specific vehicle. Certain handling flags can only be set globally using `SET_HANDLING_FLOAT`, this might require some experimentation.
	 * Example: `SetVehicleHandlingFloat(vehicle, 'CHandlingData', 'fSteeringLock', 360.0)`
	 * @param vehicle The vehicle to set data for.
	 * @param class The handling class to set. Only "CHandlingData" is supported at this time.
	 * @param fieldName The field name to set. These match the keys in `handling.meta`.
	 * @param value The floating-point value to set.
	 */
	fun setVehicleHandlingFloat(vehicle: EntityId, _class: String, fieldName: String, value: Double) {
		SetVehicleHandlingFloat(vehicle, _class, fieldName, value)
	}

	/**
	 * Sets a handling override for a specific vehicle. Certain handling flags can only be set globally using `SET_HANDLING_INT`, this might require some experimentation.
	 * @param vehicle The vehicle to set data for.
	 * @param _class The handling class to set. Only "CHandlingData" is supported at this time.
	 * @param fieldName The field name to set. These match the keys in `handling.meta`.
	 * @param value The integer value to set.
	 */
	fun setVehicleHandlingInt(vehicle: EntityId, _class: String, fieldName: String, value: Int) {
		SetVehicleHandlingInt(vehicle, _class, fieldName, value)
	}

	/**
	 * control - c# works with (int)GTA.Control.CursorY / (int)GTA.Control.CursorX and returns the mouse movement (additive).
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	fun getDisabledControlNormal(inputGroup: Int, control: Int): Int {
		return GetDisabledControlNormal(inputGroup, control)
	}

	fun setVehicleBrakeLights(vehicle: EntityId, toggle: Boolean) {
		SetVehicleBrakeLights(vehicle, toggle)
	}

	/**
	 * Relative can be used for getting speed relative to the frame of the vehicle, to determine for example, if you are going in reverse (-y speed) or not (+y speed).
	 */
	fun getEntitySpeedVector(entity: EntityId, relative: Boolean): Coordinates {
		GetEntitySpeedVector(entity, relative).let {
			return Coordinates(it[0], it[1], it[2])
		}
	}

	/**
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	fun getControlValue(inputGroup: Int, control: Int): Int {
		return GetControlValue(inputGroup, control)
	}

	/**
	 * "To burst tyres VEHICLE::SET_VEHICLE_TYRE_BURST(vehicle, 0, true, 1000.0)
	 * to burst all tyres type it 8 times where p1 = 0 to 7.
	 * p3 seems to be how much damage it has taken. 0 doesn't deflate them, 1000 completely deflates them.
	 * '0 = wheel_lf / bike, plane or jet front
	 * '1 = wheel_rf
	 * '2 = wheel_lm / in 6 wheels trailer, plane or jet is first one on left
	 * '3 = wheel_rm / in 6 wheels trailer, plane or jet is first one on right
	 * '4 = wheel_lr / bike rear / in 6 wheels trailer, plane or jet is last one on left
	 * '5 = wheel_rr / in 6 wheels trailer, plane or jet is last one on right
	 * '45 = 6 wheels trailer mid wheel left
	 * '47 = 6 wheels trailer mid wheel right
	 */
	fun setVehicleTyreBurst(vehicle: EntityId, index: Int, onRim: Boolean, p3: Number) {
		SetVehicleTyreBurst(vehicle, index, onRim, p3)
	}

	/**
	 * Allows you to toggle bulletproof tires.
	 */
	fun setVehicleTyresCanBurst(vehicle: EntityId, toggle: Boolean) {
		SetVehicleTyresCanBurst(vehicle, toggle)
	}

	fun getVehicleTyresCanBurst(vehicle: EntityId): Boolean {
		return GetVehicleTyresCanBurst(vehicle) == 1
	}

	/**
	 * enum MarkerTypes
	 * {
	 * MarkerTypeUpsideDownCone = 0,
	 * MarkerTypeVerticalCylinder = 1,
	 * MarkerTypeThickChevronUp = 2,
	 * MarkerTypeThinChevronUp = 3,
	 * MarkerTypeCheckeredFlagRect = 4,
	 * MarkerTypeCheckeredFlagCircle = 5,
	 * MarkerTypeVerticleCircle = 6,
	 * MarkerTypePlaneModel = 7,
	 * MarkerTypeLostMCDark = 8,
	 * MarkerTypeLostMCLight = 9,
	 * MarkerTypeNumber0 = 10,
	 * MarkerTypeNumber1 = 11,
	 * MarkerTypeNumber2 = 12,
	 * MarkerTypeNumber3 = 13,
	 * MarkerTypeNumber4 = 14,
	 * MarkerTypeNumber5 = 15,
	 * MarkerTypeNumber6 = 16,
	 * MarkerTypeNumber7 = 17,
	 * MarkerTypeNumber8 = 18,
	 * MarkerTypeNumber9 = 19,
	 * MarkerTypeChevronUpx1 = 20,
	 * MarkerTypeChevronUpx2 = 21,
	 * MarkerTypeChevronUpx3 = 22,
	 * MarkerTypeHorizontalCircleFat = 23,
	 * MarkerTypeReplayIcon = 24,
	 * MarkerTypeHorizontalCircleSkinny = 25,
	 * MarkerTypeHorizontalCircleSkinny_Arrow = 26,
	 * MarkerTypeHorizontalSplitArrowCircle = 27,
	 * MarkerTypeDebugSphere = 28,
	 * MarkerTypeDallorSign = 29,
	 * MarkerTypeHorizontalBars = 30,
	 * MarkerTypeWolfHead = 31
	 * };
	 * dirX/Y/Z represent a heading on each axis in which the marker should face, alternatively you can rotate each axis independently with rotX/Y/Z (and set dirX/Y/Z all to 0).
	 * faceCamera - Rotates only the y-axis (the heading) towards the camera
	 * p19 - no effect, default value in script is 2
	 * rotate - Rotates only on the y-axis (the heading)
	 * textureDict - Name of texture dictionary to load texture from (e.g. "GolfPutting")
	 * textureName - Name of texture inside dictionary to load (e.g. "PuttingMarker")
	 * drawOnEnts - Draws the marker onto any entities that intersect it
	 * basically what he said, except textureDict and textureName are totally not char*, or if so, then they are always set to 0/NULL/nullptr in every script I checked, eg:
	 * bj.c: graphics::draw_marker(6, vParam0, 0f, 0f, 1f, 0f, 0f, 0f, 4f, 4f, 4f, 240, 200, 80, iVar1, 0, 0, 2, 0, 0, 0, false);
	 * his is what I used to draw an amber downward pointing chevron "V", has to be redrawn every frame.  The 180 is for 180 degrees rotation around the Y axis, the 50 is alpha, assuming max is 100, but it will accept 255.
	 * GRAPHICS::DRAW_MARKER(2, v.x, v.y, v.z + 2, 0, 0, 0, 0, 180, 0, 2, 2, 2, 255, 128, 0, 50, 0, 1, 1, 0, 0, 0, 0);
	 */
	fun drawMarker(
		markerType: Int,
		posX: Float,
		posY: Float,
		posZ: Float,
		dirX: Float = 0f,
		dirY: Float = 0f,
		dirZ: Float = 0f,
		rotX: Float = 0f,
		rotY: Float = 0f,
		rotZ: Float = 0f,
		scaleX: Double = 1.0,
		scaleY: Double = 1.0,
		scaleZ: Double = 1.0,
		red: Number,
		green: Number,
		blue: Number,
		alpha: Number,
		bobUpAndDown: Boolean = false,
		faceCamera: Boolean = false,
		rotate: Boolean = false,
		textureDict: String? = null,
		textureName: String? = null,
		drawOnEnts: Boolean = false
	) {
		DrawMarker(
			markerType,
			posX,
			posY,
			posZ,
			dirX,
			dirY,
			dirZ,
			rotX,
			rotY,
			rotZ,
			scaleX,
			scaleY,
			scaleZ,
			red,
			green,
			blue,
			alpha,
			bobUpAndDown,
			faceCamera,
			2,
			rotate,
			textureDict,
			textureName,
			drawOnEnts
		)
	}

	/**
	 * Draws a notification above the map and returns the notifications handle
	 * Color syntax:
	 * ~r~ = Red
	 * ~b~ = Blue
	 * ~g~ = Green
	 * ~y~ = Yellow
	 * ~p~ = Purple
	 * ~o~ = Orange
	 * ~c~ = Grey
	 * ~m~ = Darker Grey
	 * ~u~ = Black
	 * ~n~ = New Line
	 * ~s~ = Default White
	 * ~w~ = White
	 * ~h~ = Bold Text
	 * ~nrt~ = ???
	 * Special characters:
	 * ?? = Rockstar Verified Icon (U+00A6:Broken Bar - Alt+0166)
	 * ?? = Rockstar Icon (U+00F7:Division Sign - Alt+0247)
	 * ??? = Rockstar Icon 2 (U+2211:N-Ary Summation)
	 * Example C#:
	 * Function.Call(Hash._ADD_TEXT_COMPONENT_STRING3, "Now I need you to bring the ~b~vehicle~w~ back to me!");
	 * ----
	 * showInBrief==true: the notification will appear in the "Brief/Info" -&gt; "Notifications" tab in the pause menu.
	 * showInBrief==false: the notification will NOT appear in the pause menu.
	 */
	fun drawNotification(blink: Boolean, showInBrief: Boolean): Number {
		return DrawNotification(blink, showInBrief)
	}

	fun getVehicleClass(vehicle: EntityId): Int {
		return GetVehicleClass(vehicle)
	}

	/**
	 * Returns an int
	 * Vehicle Classes:
	 * 0: Compacts
	 * 1: Sedans
	 * 2: SUVs
	 * 3: Coupes
	 * 4: Muscle
	 * 5: Sports Classics
	 * 6: Sports
	 * 7: Super
	 * 8: Motorcycles
	 * 9: Off-road
	 * 10: Industrial
	 * 11: Utility
	 * 12: Vans
	 * 13: Cycles
	 * 14: Boats
	 * 15: Helicopters
	 * 16: Planes
	 * 17: Service
	 * 18: Emergency
	 * 19: Military
	 * 20: Commercial
	 * 21: Trains
	 * char buffer[128];
	 * std::sprintf(buffer, "VEH_CLASS_%i", VEHICLE::GET_VEHICLE_CLASS(vehicle));
	 * char* className = UI::_GET_LABEL_TEXT(buffer);
	 */
	fun setNotificationTextEntry(text: String) {
		SetNotificationTextEntry(text)
	}

	fun isToggleModOn(vehicle: EntityId, modType: Int): Boolean {
		return IsToggleModOn(vehicle, modType) == 1
	}

	/**
	 * Toggles:
	 * UNK17
	 * Turbo
	 * UNK19
	 * Tire Smoke
	 * UNK21
	 * Xenon Headlights
	 */
	fun toggleVehicleMod(vehicle: EntityId, modType: Int, toggle: Boolean) {
		ToggleVehicleMod(vehicle, modType, toggle)
	}

	fun getFrameCount(): Int {
		return GetFrameCount().toInt()
	}

	fun getFrameTime(): Double {
		return GetFrameTime().toDouble()
	}

	/**
	 * Returns the value of CONTROLS::GET_CONTROL_VALUE Normalized (ie a real number value between -1 and 1)
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	fun getControlNormal(inputGroup: Int, control: Int): Int {
		return GetControlNormal(inputGroup, control)
	}

	fun getVehicleMaxSpeed(vehicle: EntityId): Double {
		return GetVehicleMaxSpeed(vehicle).toDouble()
	}

	fun setEntityMaxSpeed(entity: EntityId, speed: Number) {
		SetEntityMaxSpeed(entity, speed)
	}

	/**
	 * Returns the effective handling data of a vehicle as a floating-point value.
	 * Example: `local fSteeringLock = GetVehicleHandlingFloat(vehicle, 'CHandlingData', 'fSteeringLock')`
	 * @param vehicle The vehicle to obtain data for.
	 * @param _class The handling class to get. Only "CHandlingData" is supported at this time.
	 * @param fieldName The field name to get. These match the keys in `handling.meta`.
	 * @return A floating-point value.
	 */
	fun getVehicleHandlingFloat(vehicle: EntityId, _class: String, fieldName: String): Double {
		return GetVehicleHandlingFloat(vehicle, _class, fieldName).toDouble()
	}

	/**
	 * Returns the effective handling data of a vehicle as an integer value.
	 * Example: `local modelFlags = GetVehicleHandlingInt(vehicle, 'CHandlingData', 'strModelFlags')`
	 * @param vehicle The vehicle to obtain data for.
	 * @param _class The handling class to get. Only "CHandlingData" is supported at this time.
	 * @param fieldName The field name to get. These match the keys in `handling.meta`.
	 * @return An integer.
	 */
	fun getVehicleHandlingInt(vehicle: EntityId, _class: String, fieldName: String): Int {
		return GetVehicleHandlingInt(vehicle, _class, fieldName)
	}

	/**
	 * Gets the vehicle the specified Ped is/was in depending on bool value.
	 * [False = CurrentVehicle, True = LastVehicle]
	 */
	fun getVehiclePedIsIn(ped: EntityId, lastVehicle: Boolean = true): EntityId? {
		return GetVehiclePedIsIn(ped, lastVehicle).takeIf { it != 0 }
	}

	/**
	 * After applying the properties to the text (See UI::SET_TEXT_), this will draw the text in the applied position. Also 0.0f &lt; x, y &lt; 1.0f, percentage of the axis.
	 * Used to be known as _DRAW_TEXT
	 */
	fun drawText(x: Number, y: Number) {
		DrawText(x, y)
	}

	/**
	 * ??? Description :
	 * Processes a string and removes the player name(max len 99)
	 * You can use this function to create notifications/subtitles
	 * --------------------------------------------------------------------
	 * ??? Usage(Colors) :
	 * ~r~ = red
	 * ~y~ = yellow
	 * ~g~ = green
	 * ~b~ = light blue
	 * ~w~ = white
	 * ~p~ = purple
	 * ~n~ = new line
	 * --------------------------------------------------------------------
	 * ??? Usage(Input) :
	 * ~INPUT_CONTEXT~ will show button symbol (regarding last input device -&gt; keyboard/gamepad)
	 * example:
	 * string info = "Context action is assigned to ~INPUT_CONTEXT~!";
	 * --------------------------------------------------------------------
	 * ??? Example (C++):
	 * void ShowNotification(char *text)
	 * {
	 * UI::_SET_NOTIFICATION_TEXT_ENTRY("STRING");
	 * UI::ADD_TEXT_COMPONENT_SUBSTRING_PLAYER_NAME(text);
	 * UI::_DRAW_NOTIFICATION(FALSE, FALSE); // if first param = 1, the message flashes 1 or 2 times
	 * }
	 * ??? Colors example :
	 * string red = "~r~Red test";
	 * string white_and_yellow = "~w~White and ~y~yellow";
	 * string text_with_double_line = "First line.~n~Second line";
	 * This native (along with 0x5F68520888E69014 and 0x94CF4AC034C9C986) do not actually filter anything. They simply add the provided text (as of 944)
	 * Used to be known as _ADD_TEXT_COMPONENT_STRING
	 */
	fun addTextComponentString(text: String) {
		AddTextComponentString(text)
	}

	/**
	 * This native (along with 0x6C188BE134E074AA and 0x94CF4AC034C9C986) do not actually filter anything. They simply add the provided text (as of 944)
	 * did you even check the disassembly?
	 * &gt; Do you even lift bro? The PLAYER_NAME and WEBSITE natives are the correct names, it doesn't matter if they're filtered or not. Blame R* for that matter. Hashes don't lie, and it's extremely unlikely the validated names are collisions (what are the odds??)
	 */
	fun addTextComponentString3(p0: String) {
		AddTextComponentString3(p0)
	}

	/**
	 * The following were found in the decompiled script files:
	 * STRING, TWOSTRINGS, NUMBER, PERCENTAGE, FO_TWO_NUM, ESMINDOLLA, ESDOLLA, MTPHPER_XPNO, AHD_DIST, CMOD_STAT_0, CMOD_STAT_1, CMOD_STAT_2, CMOD_STAT_3, DFLT_MNU_OPT, F3A_TRAFDEST, ES_HELP_SOC3
	 * ESDOLLA
	 * ESMINDOLLA - cash (negative)
	 * Used to be known as _SET_TEXT_ENTRY
	 */
	fun setTextEntry(text: String) {
		SetTextEntry(text)
	}

	fun setTextOutline() {
		SetTextOutline()
	}

	fun setTextDropShadow() {
		SetTextDropShadow()
	}

	fun setTextEdge(p0: Number, r: Int, g: Int, b: Int, a: Int) {
		SetTextEdge(p0, r, g, b, a)
	}

	/**
	 * distance - shadow distance in pixels, both horizontal and vertical
	 * r, g, b, a
	 */
	fun setTextDropShadow(distance: Number, r: Int, g: Int, b: Int, a: Int) {
		SetTextDropshadow(distance, r, g, b, a)
	}

	fun setTextColour(red: Int, green: Int, blue: Int, alpha: Int) {
		SetTextColour(red, green, blue, alpha)
	}

	/**
	 * Size range : 0f to 1.0f
	 */
	fun setTextScale(scale: Double, size: Double) {
		SetTextScale(scale, size)
	}

	fun setTextProportional(p0: Boolean) {
		SetTextProportional(p0)
	}

	/**
	 * fonts that mess up your text where made for number values/misc stuff
	 */
	fun setTextFont(fontType: Int) {
		SetTextFont(fontType)
	}

	fun setVehicleHighGear(vehicle: EntityId, gear: Int) {
		SetVehicleHighGear(vehicle, gear)
	}

	fun getVehicleHighGear(vehicle: EntityId): Int {
		return GetVehicleHighGear(vehicle)
	}

	/**
	 * playLength - is how long to play the effect for in milliseconds. If 0, it plays the default length
	 * if loop is true, the effect wont stop until you call _STOP_SCREEN_EFFECT on it. (only loopable effects)
	 * Example and list of screen FX: www.pastebin.com/dafBAjs0
	 */
	fun startScreenEffect(effectName: String, milliseconds: Int = 0, looped: Boolean = false) {
		StartScreenEffect(effectName, milliseconds, looped)
	}

	/**
	 * Does not take effect immediately, unfortunately.
	 * profileSetting seems to only be 936, 937 and 938 in scripts
	 * gtaforums.com/topic/799843-stats-profile-settings/
	 */
	//todo test
	suspend fun setProfileSetting(profileSetting: Int, value: Int) {
		N_0x68f01422be1d838f(profileSetting, value)

		while (getProfileSetting(profileSetting) != value) {
			delay(250)
		}
	}

	/**
	 * SET_VEHICLE_BOOST_ACTIVE(vehicle, 1, 0);
	 * SET_VEHICLE_BOOST_ACTIVE(vehicle, 0, 0);
	 * Will give a boost-soundeffect.
	 */
	fun setVehicleBoostActive(vehicle: EntityId, Toggle: Boolean) {
		SetVehicleBoostActive(vehicle, Toggle)
	}

	/**
	 * SCALE: Setting the speed to 30 would result in a speed of roughly 60mph, according to speedometer.
	 * Speed is in meters per second
	 * You can convert meters/s to mph here:
	 * http://www.calculateme.com/Speed/MetersperSecond/ToMilesperHour.htm
	 */
	fun setVehicleForwardSpeed(vehicle: EntityId, speed: Number) {
		SetVehicleForwardSpeed(vehicle, speed)
	}

	/**
	 * Quick disassembly and test seems to indicate that this native gets the Ped currently using the specified door.
	 */
	fun getPedUsingVehicleDoor(vehicle: EntityId, index: Int): EntityId {
		return GetPedUsingVehicleDoor(vehicle, index)
	}

	fun getNumberOfVehicleDoors(vehicle: EntityId): Int {
		return GetNumberOfVehicleDoors(vehicle)
	}

	/**
	 * doorIndex:
	 * 0 = Front Right Door
	 * 1 = Front Left Door
	 * 2 = Back Right Door
	 * 3 = Back Left Door
	 * 4 = Hood
	 * 5 = Trunk
	 * Changed last paramater from CreateDoorObject To NoDoorOnTheFloor because when on false, the door object is created,and not created when on true...the former parameter name was counter intuitive...(by Calderon)
	 */
	fun setVehicleDoorBroken(vehicle: EntityId, doorIndex: Int, deleteDoor: Boolean) {
		SetVehicleDoorBroken(vehicle, doorIndex, deleteDoor)
	}

	/**
	 * doorID starts at 0, not seeming to skip any numbers. Four door vehicles intuitively range from 0 to 3.
	 */
	fun isVehicleDoorDamaged(vehicle: EntityId, index: Int): Boolean {
		return IsVehicleDoorDamaged(vehicle, index) == 1
	}

	fun setVehicleClutch(vehicle: EntityId, clutch: Number) {
		SetVehicleClutch(vehicle, clutch)
	}

	fun getVehicleClutch(vehicle: EntityId): Number {
		return GetVehicleClutch(vehicle)
	}

	fun setVehicleAsNoLongerNeeded(vehicle: EntityId) {
		SetVehicleAsNoLongerNeeded(vehicle)
	}

	fun isVehicleWanted(vehicle: EntityId): Boolean {
		return IsVehicleWanted(vehicle) == 1
	}

	/**
	 * Sets the wanted state of this vehicle.
	 */
	fun setVehicleIsWanted(vehicle: EntityId, state: Boolean) {
		SetVehicleIsWanted(vehicle, state)
	}

	fun isVehicleEngineStarting(vehicle: EntityId): Boolean {
		return IsVehicleEngineStarting(vehicle) == 1
	}

	fun isVehicleAlarmSet(vehicle: EntityId): Boolean {
		return IsVehicleAlarmSet(vehicle) == 1
	}

	/**
	 * Adjusts the offset of the specified wheel relative to the wheel's axle center.
	 * Needs to be called every frame in order to function properly, as GTA will reset the offset otherwise.
	 * This function can be especially useful to set the track width of a vehicle, for example:
	 * ```
	 * function SetVehicleFrontTrackWidth(vehicle, width)
	 * SetVehicleWheelXOffset(vehicle, 0, -width/2)
	 * SetVehicleWheelXOffset(vehicle, 1, width/2)
	 * end
	 * ```
	 */
	fun setVehicleWheelXOffset(vehicle: EntityId, wheelIndex: Int, offset: Number) {
		SetVehicleWheelXOffset(vehicle, wheelIndex, offset)
	}

	fun setVehicleWheelXrot(vehicle: EntityId, wheelIndex: Int, value: Number) {
		SetVehicleWheelXrot(vehicle, wheelIndex, value)
	}

	/**
	 * Returns the offset of the specified wheel relative to the wheel's axle center.
	 */
	fun getVehicleWheelXOffset(vehicle: EntityId, wheelIndex: Int): Number {
		return GetVehicleWheelXOffset(vehicle, wheelIndex)
	}

	fun getVehicleWheelXrot(vehicle: EntityId, wheelIndex: Int): Number {
		return GetVehicleWheelXrot(vehicle, wheelIndex)
	}

	fun getVehicleNumberOfWheels(vehicle: EntityId): Int {
		return GetVehicleNumberOfWheels(vehicle)
	}

	fun getVehicleAlarmTimeLeft(vehicle: EntityId): Int {
		return GetVehicleAlarmTimeLeft(vehicle)
	}

	fun setVehicleAlarm(vehicle: EntityId, state: Boolean) {
		SetVehicleAlarm(vehicle, state)
	}

	fun setVehicleAlarmTimeLeft(vehicle: EntityId, time: Int) {
		SetVehicleAlarmTimeLeft(vehicle, time)
	}

	/**
	 * Whether or not another player is allowed to take control of the entity
	 */
	fun setNetworkIdCanMigrate(netId: Int, toggle: Boolean) {
		SetNetworkIdCanMigrate(netId, toggle)
	}

	fun setNetworkIdExistsOnAllMachines(netId: Int, toggle: Boolean) {
		SetNetworkIdExistsOnAllMachines(netId, toggle)
	}

	fun networkGetNetworkIdFromEntity(entity: EntityId): Int {
		return NetworkGetNetworkIdFromEntity(entity)
	}

	fun networkGetPlayerLoudness(playerId: Int): Float {
		return NetworkGetPlayerLoudness(playerId).toFloat()
	}

	fun setVehicleHasBeenOwnedByPlayer(vehicle: EntityId, owned: Boolean = true) {
		SetVehicleHasBeenOwnedByPlayer(vehicle, owned)
	}

	/**
	 * Sets a vehicle on the ground on all wheels.  Returns whether or not the operation was successful.
	 * sfink: This has an additional param(Vehicle vehicle, float p1) which is always set to 5.0f in the b944 scripts.
	 */
	fun setVehicleOnGroundProperly(vehicle: EntityId): Number {
		return SetVehicleOnGroundProperly(vehicle)
	}

	/**
	 * Gets speed of a wheel at the tyre.
	 * Max number of wheels can be retrieved with the native GET_VEHICLE_NUMBER_OF_WHEELS.
	 * @return An integer.
	 */
	fun getVehicleWheelSpeed(vehicle: EntityId, wheelIndex: Int): Int {
		return GetVehicleWheelSpeed(vehicle, wheelIndex).toInt()
	}

	fun getVehicleWheelHealth(vehicle: EntityId, wheelIndex: Int): Number {
		return GetVehicleWheelHealth(vehicle, wheelIndex)
	}

	fun setVehicleWheelHealth(vehicle: EntityId, wheelIndex: Int, health: Number) {
		SetVehicleWheelHealth(vehicle, wheelIndex, health)
	}

	fun endFindPickup(findHandle: Handle) {
		EndFindPickup(findHandle)
	}

	fun findFirstPickup(): Pair<Handle, EntityId> {
		val data = FindFirstPickup()
		return data[0] to data[1]
	}

	fun findNextPickup(findHandle: Handle): Pair<Boolean, Int> {
		val data = FindNextPickup(findHandle)
		return (data[0] == 1) to data[1] as Int
	}

	fun endFindPed(findHandle: Handle) {
		EndFindPed(findHandle)
	}

	fun findFirstPed(): Pair<Handle, EntityId> {
		val data = FindFirstPed()
		return data[0] to data[1]
	}

	fun findNextPed(findHandle: Handle): Pair<Boolean, Int> {
		val data = FindNextPed(findHandle)
		return (data[0] == 1) to data[1] as Int
	}

	fun endFindObject(findHandle: Handle) {
		EndFindObject(findHandle)
	}

	fun findFirstObject(): Pair<Handle, EntityId> {
		val data = FindFirstObject()
		return data[0] to data[1]
	}

	fun findNextObject(findHandle: Handle): Pair<Boolean, Int> {
		val data = FindNextObject(findHandle)
		return (data[0] == 1) to data[1] as Int
	}

	fun endFindVehicle(findHandle: Handle) {
		EndFindVehicle(findHandle)
	}

	fun findFirstVehicle(): Pair<Handle, EntityId> {
		val data = FindFirstVehicle()
		return data[0] to data[1]
	}

	fun findNextVehicle(findHandle: Handle): Pair<Boolean, Int> {
		val data = FindNextVehicle(findHandle)
		return (data[0] == 1) to data[1] as Int
	}

	/**
	 * Draws a rectangle on the screen.
	 * -x: The relative X point of the center of the rectangle. (0.0-1.0, 0.0 is the left edge of the screen, 1.0 is the right edge of the screen)
	 * -y: The relative Y point of the center of the rectangle. (0.0-1.0, 0.0 is the top edge of the screen, 1.0 is the bottom edge of the screen)
	 * -width: The relative width of the rectangle. (0.0-1.0, 1.0 means the whole screen width)
	 * -height: The relative height of the rectangle. (0.0-1.0, 1.0 means the whole screen height)
	 * -R: Red part of the color. (0-255)
	 * -G: Green part of the color. (0-255)
	 * -B: Blue part of the color. (0-255)
	 * -A: Alpha part of the color. (0-255, 0 means totally transparent, 255 means totally opaque)
	 * The total number of rectangles to be drawn in one frame is apparently limited to 399.
	 */
	fun drawRect(x: Double, y: Double, width: Double, height: Double, r: Short, g: Short, b: Short, a: Short = 255) {
		DrawRect(x, y, width, height, r, g, b, a)
	}

	fun doesEntityExist(entity: EntityId): Boolean {
		return DoesEntityExist(entity) == 1
	}

	/**
	 * p1 is always 0 in the scripts.
	 * p1 = check if vehicle is on fire
	 */
	fun isVehicleDriveable(vehicle: EntityId, isOnFireCheck: Boolean = false): Boolean {
		return IsVehicleDriveable(vehicle, isOnFireCheck) == 1
	}

	/**
	 * Dirt level 0..15
	 */
	fun getVehicleDirtLevel(vehicle: EntityId): Int {
		return GetVehicleDirtLevel(vehicle)
	}

	/**
	 * You can't use values greater than 15.0
	 * You can see why here: pastebin.com/Wbn34fGD
	 * Also, R* does (float)(rand() % 15) to get a random dirt level when generating a vehicle.
	 */
	fun setVehicleDirtLevel(vehicle: EntityId, dirtLevel: Int) {
		SetVehicleDirtLevel(vehicle, dirtLevel)
	}

	/**
	 * thisScriptCheck - can be destroyed if it belongs to the calling script.
	 */
	suspend fun createVehicle(
		modelHash: Int,
		x: Float,
		y: Float,
		z: Float,
		heading: Float,
		isNetwork: Boolean = true,
		thisScriptCheck: Boolean = false
	): Int = try {
		requestModel(modelHash)
		while (!hasModelLoaded(modelHash)) {
			delay(100)
		}
		CreateVehicle(modelHash, x, y, z, heading, isNetwork, thisScriptCheck)
	} finally {
		setModelAsNoLongerNeeded(modelHash)
	}

	/**
	 * Turns the desired ped into a cop. If you use this on the player ped, you will become almost invisible to cops dispatched for you. You will also report your own crimes, get a generic cop voice, get a cop-vision-cone on the radar, and you will be unable to shoot at other cops. SWAT and Army will still shoot at you. Toggling ped as "false" has no effect; you must change p0's ped model to disable the effect.
	 */
	fun setPedAsCop(ped: EntityId) {
		SetPedAsCop(ped, true)
	}

	/**
	 * This native converts its past string to hash. It is hashed using jenkins one at a time method.
	 * ----------
	 * The string is first converted to lowercase before feeding it to joaat.
	 * As a result, it makes this native case-insensitive.
	 * For example: "zentorno", "ZENTORNO" and "Zentorno" produce the same hash.
	 */
	fun getHashKey(string: String): Long {
		return GetHashKey(string).toLong()
	}

	/**
	 * Used to prepare a scene where the surrounding sound is muted or a bit changed. This does not play any sound.
	 * List of all usable scene names found in b617d. Sorted alphabetically and identical names removed: pastebin.com/MtM9N9CC
	 */
	@Deprecated("use NativeAudioScenes.{scene}.play()")
	fun startAudioScene(scene: String): Boolean {
		return StartAudioScene(scene) == 1
	}

	@Deprecated("use NativeAudioScenes.{scene}.stop()")
	fun stopAudioScene(scene: String) {
		StopAudioScene(scene)
	}

	fun setFrontendRadioActive(active: Boolean) {
		SetFrontendRadioActive(active)
	}

	fun setClockDate(day: Int, month: Int, year: Int) {
		SetClockDate(day, month, year)
	}

	/**
	 * SET_CLOCK_TIME(12, 34, 56);
	 */
	@Deprecated("not working? use object Date")
	fun setClockTime(hour: Int, minute: Int, second: Int) {
		SetClockTime(hour, minute, second)
	}

	fun sendNuiMessage(obj: Any): Boolean {
		val jsonString = JSON.stringify(obj)

		return SendNuiMessage(jsonString) == 1
	}

	/**
	 * Sends a message to the `loadingScreen` NUI frame, which contains the HTML page referenced in `loadscreen` resources.
	 * @param jsonString The JSON-encoded message.
	 * @return A success value.
	 */
	fun sendLoadingScreenMessage(obj: Any): Boolean {
		return SendLoadingScreenMessage(JSON.stringify(obj)) == 1
	}

	fun networkGetServerTime(): Time {
		return NetworkGetServerTime()
	}

	/**
	 * возвращает переменную metadataKey из __resource.lua в папке resourceName
	 */
	/**
	 * Gets the metadata value at a specified key/index from a resource's manifest.
	 * See also: [Resource manifest](https://wiki.fivem.net/wiki/Resource_manifest)
	 * @param resourceName The resource name.
	 * @param metadataKey The key in the resource manifest.
	 * @param index The value index, in a range from [0..GET_NUM_RESOURCE_METDATA-1].
	 */
	fun getResourceMetadata(resourceName: String, metadataKey: String, index: Int): String? {
		return GetResourceMetadata(resourceName, metadataKey, index)
	}

	/**
	 * gtaforums.com/topic/799843-stats-profile-settings/
	 */
	fun getProfileSetting(profileSetting: Int): Int {
		return GetProfileSetting(profileSetting)
	}

	fun addTextEntry(entryKey: String, entryText: String) {
		AddTextEntry(entryKey, entryText)
	}

	/**
	 * Gets the amount of metadata values with the specified key existing in the specified resource's manifest.
	 * See also: [Resource manifest](https://wiki.fivem.net/wiki/Resource_manifest)
	 * @param resourceName The resource name.
	 * @param metadataKey The key to look up in the resource manifest.
	 */
	fun getNumResourceMetadata(resourceName: String, metadataKey: String): Int? {
		return GetNumResourceMetadata(resourceName, metadataKey)
	}

	/**
	 * p1 = !IS_ENTITY_DEAD
	 */
	fun getEntityCoords(entity: EntityId, alive: Boolean = true): Coordinates {
		val coords = GetEntityCoords(entity, alive)

		val x = coords[0]
		val y = coords[1]
		val z = coords[2]

		return Coordinates(x, y, z)
	}

	/**
	 * Gets the entity's forward vector.
	 */
	fun getEntityForwardVector(entity: EntityId): Array<Float> {
		return GetEntityForwardVector(entity)
	}

	/**
	 * Gets the X-component of the entity's forward vector.
	 */
	fun getEntityForwardX(entity: EntityId): Float {
		return GetEntityForwardX(entity)
	}

	/**
	 * Gets the Y-component of the entity's forward vector.
	 */
	fun getEntityForwardY(entity: EntityId): Float {
		return GetEntityForwardY(entity)
	}

	/**
	 * 0.0-360.0 degrees
	 */
	/**
	 * Returns the heading of the entity in degrees. Also know as the "Yaw" of an entity.
	 */
	fun getEntityHeading(entity: EntityId): Float {
		return GetEntityHeading(entity)
	}

	fun setEntityHeading(entity: EntityId, heading: Float) {
		SetEntityHeading(entity, heading)
	}

	/**
	 * Gets ID of vehicle player using. It means it can get ID at any interaction with vehicle. Enter\exit for example. And that means it is faster than GET_VEHICLE_PED_IS_IN but less safe.
	 */
	fun getVehiclePedIsUsing(ped: EntityId): EntityId? {
		return GetVehiclePedIsUsing(ped).takeIf { it != 0 }
	}

	fun setVehicleNextGear(vehicle: EntityId, gear: Int) {
		invokeNative("SET_VEHICLE_NEXT_GEAR", vehicle, gear)
	}

	fun getVehicleNextGear(vehicle: Int): Int {
		return GetVehicleNextGear(vehicle)
	}

	fun setVehicleCurrentGear(vehicle: EntityId, gear: Int) {
		invokeNative("SET_VEHICLE_CURRENT_GEAR", vehicle, gear)
	}

	fun getVehicleCurrentGear(vehicle: EntityId): Int {
		return GetVehicleCurrentGear(vehicle)
	}

	//0.0-1.0
	fun getVehicleCurrentRpm(vehicle: EntityId): Double {
		return GetVehicleCurrentRpm(vehicle)
	}

	fun setVehicleCurrentRpm(vehicle: EntityId, rpm: Double) {
		SetVehicleCurrentRpm(vehicle, rpm)
	}

	fun getVehicleDashboardSpeed(vehicle: EntityId): Double {
		return GetVehicleDashboardSpeed(vehicle)
	}

	fun getVehicleEngineTemperature(vehicle: EntityId): Float {
		return GetVehicleEngineTemperature(vehicle)
	}

	fun setVehicleEngineTemperature(vehicle: EntityId, temperature: Float) {
		SetVehicleEngineTemperature(vehicle, temperature)
	}

	fun getVehicleFuelLevel(vehicle: EntityId): Double {
		return GetVehicleFuelLevel(vehicle).toDouble()
	}

	fun getVehicleHandbrake(vehicle: EntityId): Boolean {
		return GetVehicleHandbrake(vehicle) == 1
	}

	fun setVehicleHandbrake(vehicle: EntityId, toggle: Boolean) {
		SetVehicleHandbrake(vehicle, toggle)
	}

	/**
	 * Simply returns whatever is passed to it (Regardless of whether the handle is valid or not).
	 */
	fun getVehicleIndexFromEntityIndex(entity: EntityId): Int {
		return GetVehicleIndexFromEntityIndex(entity)
	}

	/**
	 * Sets a vehicle's license plate text.  8 chars maximum.
	 * Example:
	 * Ped playerPed = PLAYER::PLAYER_PED_ID();
	 * Vehicle veh = PED::GET_VEHICLE_PED_IS_USING(playerPed);
	 * char *plateText = "KING";
	 * VEHICLE::SET_VEHICLE_NUMBER_PLATE_TEXT(veh, plateText);
	 */
	fun setVehicleNumberPlateText(vehicle: EntityId, plateText: String) {
		SetVehicleNumberPlateText(vehicle, plateText)
	}

	/**
	 * Returns the license plate text from a vehicle.  8 chars maximum.
	 */
	fun getVehicleNumberPlateText(vehicle: EntityId): String? {
		return GetVehicleNumberPlateText(vehicle)
	}

	fun getVehicleOilLevel(vehicle: EntityId): Float {
		return GetVehicleOilLevel(vehicle).toFloat()
	}

	/**
	 * 1000 is max health
	 * Begins leaking gas at around 650 health
	 */
	fun getVehiclePetrolTankHealth(vehicle: EntityId): Double {
		return GetVehiclePetrolTankHealth(vehicle).toDouble()
	}

	// no such entity warning
	fun getVehicleTurboPressure(vehicle: EntityId): Int? {
		val value = invokeNative("GET_VEHICLE_TURBO_PRESSURE", vehicle)

		return if (value == false) null else value as Int
	}

	fun setVehicleTurboPressure(vehicle: EntityId, pressure: Int) {
		SetVehicleTurboPressure(vehicle, pressure)
	}

	/**
	 * Returns true when in a vehicle, false whilst entering/exiting.
	 */
	fun getIsVehicleEngineRunning(vehicle: EntityId): Boolean {
		return GetIsVehicleEngineRunning(vehicle) == 1
	}

	/**
	 * Starts or stops the engine on the specified vehicle.
	 * vehicle: The vehicle to start or stop the engine on.
	 * value: true to turn the vehicle on; false to turn it off.
	 * instantly: if true, the vehicle will be set to the state immediately; otherwise, the current driver will physically turn on or off the engine.
	 * --------------------------------------
	 * from what I've tested when I do this to a helicopter the propellers turn off after the engine has started. so is there any way to keep the heli propellers on?
	 * --------------------------------------
	 * And what's with BOOL otherwise, what does it do???
	 */
	fun setVehicleEngineOn(vehicle: EntityId, value: Boolean, instantly: Boolean, otherwise: Boolean = true) {
		SetVehicleEngineOn(vehicle, value, instantly, otherwise)
	}

	/**
	 * Returns true when in a vehicle, false whilst entering/exiting.
	 */
	fun isVehicleEngineOn(vehicle: EntityId): Boolean {
		return IsVehicleEngineOn(vehicle) == 1
	}

	/**
	 * 2 seems to disable getting vehicle in modshop
	 */
	fun getVehicleDoorLockStatus(vehicle: EntityId): Int {
		return GetVehicleDoorLockStatus(vehicle)
	}

	/**
	 * Returns 1000.0 if the function is unable to get the address of the specified vehicle or if it's not a vehicle.
	 * Minimum: -4000
	 * Maximum: 1000
	 * -4000: Engine is destroyed
	 * 0 and below: Engine catches fire and health rapidly declines
	 * 300: Engine is smoking and losing functionality
	 * 1000: Engine is perfect
	 */
	fun getVehicleEngineHealth(vehicle: EntityId): Double {
		return GetVehicleEngineHealth(vehicle).toDouble()
	}

	/**
	 * result is in meters per second
	 * ------------------------------------------------------------
	 * So would the conversion to mph and km/h, be along the lines of this.
	 * float speed = GET_ENTITY_SPEED(veh);
	 * float kmh = (speed * 3.6);
	 * float mph = (speed * 2.236936);
	 */
	fun getEntitySpeed(entity: EntityId): Double {
		return GetEntitySpeed(entity)
	}

	/**
	 * -1 (driver) &lt;= index &lt; GET_VEHICLE_MAX_NUMBER_OF_PASSENGERS(vehicle)
	 */
	fun getPedInVehicleSeat(vehicle: EntityId, index: Int): EntityId? {//todo check return of empty seat
		return GetPedInVehicleSeat(vehicle, index)
	}

	/**
	 * количество ПАССАЖИРСКИХ мест в машине
	 */
	fun getVehicleMaxNumberOfPassengers(vehicle: EntityId): Int {
		return GetVehicleMaxNumberOfPassengers(vehicle)
	}

	/**
	 * max level 65
	 */
	fun setVehicleFuelLevel(vehicle: EntityId, level: Number) {
		SetVehicleFuelLevel(vehicle, level)
	}

	/**
	 * max level 1
	 */
	fun setVehicleOilLevel(vehicle: EntityId, level: Number) {
		SetVehicleOilLevel(vehicle, level)
	}

	/**
	 * Control Groups:
	 * enum InputGroups
	 * {
	 * INPUTGROUP_MOVE = 0,
	 * INPUTGROUP_LOOK = 1,
	 * INPUTGROUP_WHEEL = 2,
	 * };
	 * 0, 1 and 2 used in the scripts.
	 */

	val defaultControlGroup = NativeControls.Groups.MOVE

	/**
	 * Control Groups:
	 * enum InputGroups
	 * {
	 * INPUTGROUP_MOVE = 0,
	 * INPUTGROUP_LOOK = 1,
	 * INPUTGROUP_WHEEL = 2,
	 * INPUTGROUP_CELLPHONE_NAVIGATE = 3,
	 * INPUTGROUP_CELLPHONE_NAVIGATE_UD = 4,
	 * INPUTGROUP_CELLPHONE_NAVIGATE_LR = 5,
	 * INPUTGROUP_FRONTEND_DPAD_ALL = 6,
	 * INPUTGROUP_FRONTEND_DPAD_UD = 7,
	 * INPUTGROUP_FRONTEND_DPAD_LR = 8,
	 * INPUTGROUP_FRONTEND_LSTICK_ALL = 9,
	 * INPUTGROUP_FRONTEND_RSTICK_ALL = 10,
	 * INPUTGROUP_FRONTEND_GENERIC_UD = 11,
	 * INPUTGROUP_FRONTEND_GENERIC_LR = 12,
	 * INPUTGROUP_FRONTEND_GENERIC_ALL = 13,
	 * INPUTGROUP_FRONTEND_BUMPERS = 14,
	 * INPUTGROUP_FRONTEND_TRIGGERS = 15,
	 * INPUTGROUP_FRONTEND_STICKS = 16,
	 * INPUTGROUP_SCRIPT_DPAD_ALL = 17,
	 * INPUTGROUP_SCRIPT_DPAD_UD = 18,
	 * INPUTGROUP_SCRIPT_DPAD_LR = 19,
	 * INPUTGROUP_SCRIPT_LSTICK_ALL = 20,
	 * INPUTGROUP_SCRIPT_RSTICK_ALL = 21,
	 * INPUTGROUP_SCRIPT_BUMPERS = 22,
	 * INPUTGROUP_SCRIPT_TRIGGERS = 23,
	 * INPUTGROUP_WEAPON_WHEEL_CYCLE = 24,
	 * INPUTGROUP_FLY = 25,
	 * INPUTGROUP_SUB = 26,
	 * INPUTGROUP_VEH_MOVE_ALL = 27,
	 * INPUTGROUP_CURSOR = 28,
	 * INPUTGROUP_CURSOR_SCROLL = 29,
	 * INPUTGROUP_SNIPER_ZOOM_SECONDARY = 30,
	 * INPUTGROUP_VEH_HYDRAULICS_CONTROL = 31,
	 * MAX_INPUTGROUPS = 32,
	 * INPUTGROUP_INVALID = 33
	 * };
	 * 0, 1 and 2 used in the scripts.
	 */
	@Deprecated("use NativeControls.Keys.isControlEnabled")
	fun isControlEnabled(
		inputGroup: Int = defaultControlGroup.index,
		control: Int
	): Boolean {
		return IsControlEnabled(inputGroup, control) == 1
	}

	@Deprecated("use NativeControls.Keys.isControlJustPressed")
	fun isControlJustPressed(
		inputGroup: Int = defaultControlGroup.index,
		control: Int
	): Boolean {
		return IsControlJustPressed(inputGroup, control) == 1
	}

	@Deprecated("use NativeControls.Keys.isControlJustReleased")
	fun isControlJustReleased(
		inputGroup: Int = defaultControlGroup.index,
		control: Int
	): Boolean {
		return IsControlJustReleased(inputGroup, control) == 1
	}

	/**
	 * index always is 2 for xbox 360 controller and razerblade
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	@Deprecated("use NativeControls.Keys.isControlPressed")
	fun isControlPressed(
		inputGroup: Int = defaultControlGroup.index,
		control: Int
	): Boolean {
		return IsControlPressed(inputGroup, control) == 1
	}

	/**
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	@Deprecated("use NativeControls.Keys.isControlReleased")
	fun isControlReleased(
		inputGroup: Int = defaultControlGroup.index,
		control: Int
	): Boolean {
		return IsControlReleased(inputGroup, control) == 1
	}

	/**
	 * Returns:
	 * 5
	 * 10
	 * 15
	 * 20
	 * 25
	 * 30
	 * 35
	 */
	fun getPauseMenuState(): PauseMenuState {
		val state = GetPauseMenuState()

		return PauseMenuState.values().find { it.code == state } ?: PauseMenuState.DISABLED
	}

	/**
	 * If useZ is false, only the 2D plane (X-Y) will be considered for calculating the distance.
	 * Consider using this faster native instead: SYSTEM::VDIST - DVIST always takes in consideration the 3D coordinates.
	 */
	fun getDistanceBetweenCoords(coords1: Coordinates, coords2: Coordinates, useZ: Boolean = true): Float {
		return GetDistanceBetweenCoords(
			coords1.x,
			coords1.y,
			coords1.z,
			coords2.x,
			coords2.y,
			coords2.z,
			useZ
		).toFloat()
	}

	/**
	 * control values and meaning: github.com/crosire/scripthookvdotnet/blob/dev_v3/source/scripting/Controls.cs
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 * Control values from the decompiled scripts: 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,
	 * 28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,53,5
	 * 4,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,
	 * 79,80,81,82,85,86,87,88,89,90,91,92,93,95,96,97,98,99,100,101,102,103,105,
	 * 107,108,109,110,111,112,113,114,115,116,117,118,119,123,126,129,130,131,132,
	 * 133,134,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,
	 * 153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,171,172
	 * ,177,187,188,189,190,195,196,199,200,201,202,203,205,207,208,209,211,212,213, 217,219,220,221,225,226,230,234,235,236,237,238,239,240,241,242,243,244,257,
	 * 261,262,263,264,265,270,271,272,273,274,278,279,280,281,282,283,284,285,286,
	 * 287,288,289,337.
	 * Example: CONTROLS::DISABLE_CONTROL_ACTION(2, 19, true) disables the switching UI from appearing both when using a keyboard and Xbox 360 controller. Needs to be executed each frame.
	 * Control group 1 and 0 gives the same results as 2. Same results for all players.
	 */
	@Deprecated("use NativeControls.Keys.disableControlAction")
	fun disableControlAction(
		inputGroup: Int,
		control: Int,
		disable: Boolean = true
	) {
		DisableControlAction(inputGroup, control, disable)
	}

	/**
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	@Deprecated("use NativeControls.Keys.isDisabledControlJustPressed")
	fun isDisabledControlJustPressed(
		inputGroup: Int,
		control: Int
	): Boolean {
		return IsDisabledControlJustPressed(inputGroup, control) == 1
	}

	/**
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	@Deprecated("use NativeControls.Keys.isDisabledControlJustReleased")
	fun isDisabledControlJustReleased(
		inputGroup: Int,
		control: Int
	): Boolean {
		return IsDisabledControlJustReleased(inputGroup, control) == 1
	}

	/**
	 * 0, 1 and 2 used in the scripts. 0 is by far the most common of them.
	 */
	@Deprecated("use NativeControls.Keys.isDisabledControlPressed")
	fun isDisabledControlPressed(
		inputGroup: Int,
		control: Int
	): Boolean {
		return IsDisabledControlPressed(inputGroup, control) == 1
	}

	/**
	 * Return height (z-dimension) above ground.
	 * Example: The pilot in a titan plane is 1.844176 above ground.
	 * How can i convert it to meters?
	 * Everything seems to be in meters, probably this too.
	 */
	fun getEntityHeightAboveGround(entity: EntityId): Float {
		return GetEntityHeightAboveGround(entity)
	}

	fun setNuiFocus(hasFocus: Boolean, hasCursor: Boolean = hasFocus) {
		SetNuiFocus(hasFocus, hasCursor)
	}

	/**
	 * Reads the contents of a text file in a specified resource.
	 * If executed on the client, this file has to be included in `files` in the resource manifest.
	 * Example: `local data = LoadResourceFile("devtools", "data.json")`
	 * @param resourceName The resource name.
	 * @param fileName The file in the resource.
	 * @return The file contents
	 */
	fun loadResourceFile(resourceName: String = GlobalConfig.MODULE_NAME, fileName: String): String {
		return LoadResourceFile(resourceName, fileName)
	}

	/**
	 * Fades the screen in.
	 * duration: The time the fade should take, in milliseconds.
	 */
	suspend fun doScreenFadeIn(duration: Int) {
		DoScreenFadeIn(duration)
		while (!isScreenFadedIn() && !isScreenFadingOut()) {
			delay(25)
		}
	}

	fun isScreenFadedIn(): Boolean {
		return IsScreenFadedIn() == 1
	}

	/**
	 * Fades the screen out.
	 * duration: The time the fade should take, in milliseconds.
	 */
	@Deprecated("use api.doScreenFadeOutAsync")
	suspend fun doScreenFadeOut(duration: Int) {
		DoScreenFadeOut(duration)
		while (!isScreenFadedOut() && !isScreenFadingIn()) {
			delay(25)
		}
	}

	fun isScreenFadedOut(): Boolean {
		return IsScreenFadedOut() == 1
	}

	fun isScreenFadingOut(): Boolean {
		return IsScreenFadingOut() == 1
	}

	fun isScreenFadingIn(): Boolean {
		return IsScreenFadingIn() == 1
	}

	/**
	 * Seems related to vehicle health, like the one in IV.
	 * Max 1000, min 0.
	 * Vehicle does not necessarily explode or become undrivable at 0.
	 */
	fun getVehicleBodyHealth(vehicle: EntityId): Int {
		return GetVehicleBodyHealth(vehicle).toInt()
	}

	/**
	 * Returns an integer value of entity's current health.
	 * Example of range for ped:
	 * - Player [0 to 200]
	 * - Ped [100 to 200]
	 * - Vehicle [0 to 1000]
	 * - Object [0 to 1000]
	 * Health is actually a float value but this native casts it to int.
	 * In order to get the actual value, do:
	 * float health = *(float *)(entityAddress + 0x280);
	 */
	fun getEntityHealth(entity: EntityId): Int {
		return GetEntityHealth(entity)
	}

	/**
	 * health &gt;= 0
	 */
	fun setEntityHealth(entity: EntityId, health: Int) {
		SetEntityHealth(entity, health)
	}

	fun setPedCanRagdollFromPlayerImpact(ped: EntityId, toggle: Boolean) {
		SetPedCanRagdollFromPlayerImpact(ped, toggle)
	}

	/**
	 * Causes Ped to ragdoll on collision with any object (e.g Running into trashcan). If applied to player you will sometimes trip on the sidewalk.
	 */
	fun setPedRagdollOnCollision(ped: EntityId, toggle: Boolean) {
		SetPedRagdollOnCollision(ped, toggle)
	}

	/**
	 * time1- Time Ped is in ragdoll mode(ms)
	 * time2- Unknown time, in milliseconds
	 * ragdollType-
	 * 0 : Normal ragdoll
	 * 1 : Falls with stiff legs/body
	 * 2 : Narrow leg stumble(may not fall)
	 * 3 : Wide leg stumble(may not fall)
	 * p4, p5, p6- No idea. In R*'s scripts they are usually either "true, true, false" or "false, false, false".
	 * EDIT 3/11/16: unclear what 'mircoseconds' mean-- a microsecond is 1000x a ms, so time2 must be 1000x time1?  more testing needed.  -sob
	 * Edit Mar 21, 2017: removed part about time2 being the microseconds version of time1. this just isn't correct. time2 is in milliseconds, and time1 and time2 don't seem to be connected in any way.
	 */
	fun setPedToRagdoll(
		ped: EntityId,
		time1: Int = 1000,
		time2: Int = 1000,
		ragdollType: Short = 0,
		p4: Boolean = false,
		p5: Boolean = false,
		p6: Boolean = false
	): Number {
		return SetPedToRagdoll(ped, time1 * 1_000, time2, ragdollType, p4, p5, p6)
	}

	/**
	 * p7 is always 1 in the scripts. Set to 1, an area around the destination coords for the moved entity is cleared from other entities.
	 * Often ends with 1, 0, 0, 1); in the scripts. It works.
	 * Axis - Invert Axis Flags
	 */
	fun setEntityCoords(
		entity: EntityId,
		xPos: Number,
		yPos: Number,
		zPos: Number,
		xAxis: Boolean = false,
		yAxis: Boolean = false,
		zAxis: Boolean = false,
		clearArea: Boolean = false
	) {
		SetEntityCoords(entity, xPos, yPos, zPos, xAxis, yAxis, zAxis, clearArea)
	}

	/**
	 * Flags used in the scripts: 0,4,16,24,32,56,60,64,128,134,256,260,384,512,640,768,896,900,952,1024,1280,2048,2560
	 * Note to people who needs this with camera mods, etc.:
	 * Flags(0, 4, 16, 24, 32, 56, 60, 64, 128, 134, 512, 640, 1024, 2048, 2560)
	 * - Disables camera rotation as well.
	 * Flags(256, 260, 384, 768, 896, 900, 952, 1280)
	 * [ translation: cameraRotation = flags &amp; (1 &lt;&lt; 8) - sfink]
	 */
	@Deprecated("use api.lockControl")
	fun setPlayerControl(player: Int, toggle: Boolean, flags: Number = 0) {
		SetPlayerControl(player, toggle, flags)
	}

	fun isEntityVisible(entity: EntityId): Boolean {
		return IsEntityVisible(entity) == 1
	}

	fun getPlayerPed(id: Int): EntityId? {
		return GetPlayerPed(id).takeIf { it != 0 }
	}

	fun setEntityVisible(entity: EntityId, toggle: Boolean) {
		SetEntityVisible(entity, toggle, false)
	}

	/**
	 * Gets a value indicating whether the specified ped is in any vehicle.
	 * If 'atGetIn' is false, the function will not return true until the ped is sitting in the vehicle and is about to close the door. If it's true, the function returns true the moment the ped starts to get onto the seat (after opening the door). Eg. if false, and the ped is getting into a submersible, the function will not return true until the ped has descended down into the submersible and gotten into the seat, while if it's true, it'll return true the moment the hatch has been opened and the ped is about to descend into the submersible.
	 */
	fun isPedInAnyVehicle(ped: EntityId, atGetIn: Boolean = false): Boolean {
		return IsPedInAnyVehicle(ped, atGetIn) == 1
	}

	fun setEntityCollision(entity: EntityId, toggle: Boolean, keepPhysics: Boolean = true) {
		SetEntityCollision(entity, toggle, keepPhysics)
	}

	/**
	 * No, this should be called SET_ENTITY_KINEMATIC. It does more than just "freeze" it's position.
	 * ^Rockstar Devs named it like that, Now cry about it.
	 */
	fun freezeEntityPosition(entity: EntityId, toggle: Boolean) {
		FreezeEntityPosition(entity, toggle)
	}

	/**
	 * Returns all player indices for 'active' physical players known to the client.
	 * The data returned adheres to the following layout:
	 * ```
	 * [127, 42, 13, 37]
	 * ```
	 * @return An object containing a list of player indices.
	 */
	fun getActivePlayers(): Array<Int> {
		return GetActivePlayers()
	}

	/**
	 * Simply sets you as invincible (Health will not deplete).
	 * Use 0x733A643B5B0C53C1 instead if you want Ragdoll enabled, which is equal to:
	 * *(DWORD *)(playerPedAddress + 0x188) |= (1 &lt;&lt; 9);
	 */
	fun setPlayerInvincible(player: Int, toggle: Boolean) {
		SetPlayerInvincible(player, toggle)
	}

	/**
	 * Returns the Player's Invincible status.
	 * This function will always return false if 0x733A643B5B0C53C1 is used to set the invincibility status. To always get the correct result, use this:
	 * bool IsPlayerInvincible(Player player)
	 * {
	 * auto addr = getScriptHandleBaseAddress(GET_PLAYER_PED(player));
	 * if (addr)
	 * {
	 * DWORD flag = *(DWORD *)(addr + 0x188);
	 * return ((flag &amp; (1 &lt;&lt; 8)) != 0) || ((flag &amp; (1 &lt;&lt; 9)) != 0);
	 * }
	 * return false;
	 * }
	 * ============================================================
	 * This has bothered me for too long, whoever may come across this, where did anyone ever come up with this made up hash? 0x733A643B5B0C53C1 I've looked all over old hash list, and this nativedb I can not find that PC hash anywhere. What native name is it now or was it?
	 */
	fun getPlayerInvincible(player: Int): Boolean {
		return GetPlayerInvincible(player) == 1
	}

	fun isPedFatallyInjured(ped: EntityId): Boolean {
		return IsPedFatallyInjured(ped) == 1
	}

	fun clearPedTasks(ped: EntityId) {
		ClearPedTasks(ped)
	}

	/**
	 * Immediately stops the pedestrian from whatever it's doing. They stop fighting, animations, etc. they forget what they were doing.
	 */
	fun clearPedTasksImmediately(ped: EntityId) {
		ClearPedTasksImmediately(ped)
	}

	/**
	 * This returns YOUR 'identity' as a Player type.
	 * Always returns 0 in story mode.
	 */
	fun getPlayerId(): Int {
		return PlayerId()
	}

	/**
	 * Returns current player ped
	 */
	fun getPlayerPedId(): EntityId {
		return PlayerPedId()
	}

	/**
	 * IPL list: pastebin.com/iNGLY32D
	 */
	fun requestIpl(iplName: String) {
		RequestIpl(iplName)
	}

	/**
	 * Request a model to be loaded into memory
	 * Looking it the disassembly, it seems like it actually returns the model if it's already loaded.
	 */
	private suspend fun requestModel(hash: Int) {
		RequestModel(hash)
		while (!hasModelLoaded(hash)) {
			delay(25)
		}
	}

	/**
	 * Checks if the specified model has loaded into memory.
	 */
	fun hasModelLoaded(hash: Int): Boolean {
		return HasModelLoaded(hash) == 1
	}

	/**
	 * p2 should be FALSE, otherwise it seems to always return FALSE
	 * Bool does not check if the weapon is current equipped, unfortunately.
	 */
	fun hasPedGotWeapon(ped: EntityId, weaponHash: String): Boolean {
		return HasPedGotWeapon(ped, weaponHash, false) == 1
	}

	/**
	 * Make sure to request the model first and wait until it has loaded.
	 */
	suspend fun setPlayerModel(player: Int, hash: Int) = try {
		requestModel(hash)
		SetPlayerModel(player, hash)
	} finally {
		setModelAsNoLongerNeeded(hash)
	}

	/**
	 * Unloads model from memory
	 */
	private fun setModelAsNoLongerNeeded(hash: Int) {
		SetModelAsNoLongerNeeded(hash)
	}

	//непонятно при каких условиях будет работать
	fun requestCollisionAtCoordinates(x: Number, y: Number, z: Number): Number {
		return RequestCollisionAtCoord(x, y, z)
	}

	/**
	 * Axis - Invert Axis Flags
	 */
	fun setEntityCoordsNoOffset(
		entity: EntityId,
		xPos: Number,
		yPos: Number,
		zPos: Number,
		xAxis: Boolean = false,
		yAxis: Boolean = false,
		zAxis: Boolean = true
	) {
		SetEntityCoordsNoOffset(entity, xPos, yPos, zPos, xAxis, yAxis, zAxis)
	}

	fun networkResurrectLocalPlayer(
		x: Number,
		y: Number,
		z: Number,
		heading: Number,
		changeTime: Boolean = true
	) {
		NetworkResurrectLocalPlayer(x, y, z, heading, true, changeTime)
	}

	/**
	 * setting the last params to false it does that same so I would suggest its not a toggle
	 */
	fun removeAllPedWeapons(ped: EntityId) {
		RemoveAllPedWeapons(ped, true)
	}

	/**
	 * This executes at the same as speed as PLAYER::SET_PLAYER_WANTED_LEVEL(player, 0, false);
	 * PLAYER::GET_PLAYER_WANTED_LEVEL(player); executes in less than half the time. Which means that it's worth first checking if the wanted level needs to be cleared before clearing. However, this is mostly about good code practice and can important in other situations. The difference in time in this example is negligible.
	 */
	fun clearPlayerWantedLevel(player: Int) {
		ClearPlayerWantedLevel(player)
	}

	fun hasCollisionLoadedAroundEntity(entity: EntityId): Boolean {
		return HasCollisionLoadedAroundEntity(entity) == 1
	}

	fun shutdownLoadingScreen() {
		ShutdownLoadingScreen()
	}

	/**
	 * Returns active radio station name
	 */
	fun getRadioStation(): RadioStation? {
		return GetPlayerRadioStationName()?.let { code ->
			RadioStation
				.values()
				.find { it.code == code }
		}
	}

	fun getPlayerRadioStationIndex(): Int? {
		return GetPlayerRadioStationIndex().takeIf { it != 255 }
	}


	fun getAudibleMusicTrackTextId(): Int {
		return GetAudibleMusicTrackTextId()
	}

	/**
	 * Sets whether or not `SHUTDOWN_LOADING_SCREEN` automatically shuts down the NUI frame for the loading screen. If this is enabled,
	 * you will have to manually invoke `SHUTDOWN_LOADING_SCREEN_NUI` whenever you want to hide the NUI loading screen.
	 * @param manualShutdown TRUE to manually shut down the loading screen NUI.
	 */
	fun setManualShutdownLoadingScreenNui(manualShutdown: Boolean) {
		SetManualShutdownLoadingScreenNui(manualShutdown)
	}

	/**
	 * Returns true if the player is currently switching, false otherwise.
	 * (When the camera is in the sky moving from Trevor to Franklin for example)
	 */
	fun isPlayerSwitchInProgress(): Boolean {
		return IsPlayerSwitchInProgress() == 1
	}

	/**
	 * fucks up on mount chilliad
	 */
	suspend fun switchOutPlayer(ped: EntityId) {
		if (isPlayerSwitchInProgress()) return

		var result = false
		do {
			withTimeoutOrNull(5_000) {
				SwitchOutPlayer(ped, 0, 1)
				while (getPlayerSwitchState() != 5) {
					delay(500)
				}
				result = true
			}
		} while (!result)
	}

	suspend fun switchInPlayer(ped: EntityId) {
		var result = false
		do {
			withTimeoutOrNull(5_000) {
				SwitchInPlayer(ped)
				while (getPlayerSwitchState() != 12) {
					delay(500)
				}
				result = true
			}
		} while (!result)
	}

	fun setCloudHatOpacity(opacity: Number) {
		SetCloudHatOpacity(opacity)
	}

	/**
	 * I think this works, but seems to prohibit switching to other weapons (or accessing the weapon wheel)
	 */
	fun hideHudAndRadarThisFrame() {
		HideHudAndRadarThisFrame()
	}

	/**
	 * Sets the on-screen drawing origin for draw-functions (which is normally x=0,y=0 in the upper left corner of the screen) to a world coordinate.
	 * From now on, the screen coordinate which displays the given world coordinate on the screen is seen as x=0,y=0.
	 * Example in C#:
	 * Vector3 boneCoord = somePed.GetBoneCoord(Bone.SKEL_Head);
	 * Function.Call(Hash.SET_DRAW_ORIGIN, boneCoord.X, boneCoord.Y, boneCoord.Z, 0);
	 * Function.Call(Hash.DRAW_SPRITE, "helicopterhud", "hud_corner", -0.01, -0.015, 0.013, 0.013, 0.0, 255, 0, 0, 200);
	 * Function.Call(Hash.DRAW_SPRITE, "helicopterhud", "hud_corner", 0.01, -0.015, 0.013, 0.013, 90.0, 255, 0, 0, 200);
	 * Function.Call(Hash.DRAW_SPRITE, "helicopterhud", "hud_corner", -0.01, 0.015, 0.013, 0.013, 270.0, 255, 0, 0, 200);
	 * Function.Call(Hash.DRAW_SPRITE, "helicopterhud", "hud_corner", 0.01, 0.015, 0.013, 0.013, 180.0, 255, 0, 0, 200);
	 * Function.Call(Hash.CLEAR_DRAW_ORIGIN);
	 * Result: www11.pic-upload.de/19.06.15/bkqohvil2uao.jpg
	 * If the pedestrian starts walking around now, the sprites are always around her head, no matter where the head is displayed on the screen.
	 * This function also effects the drawing of texts and other UI-elements.
	 * The effect can be reset by calling GRAPHICS::CLEAR_DRAW_ORIGIN().
	 */
	fun setDrawOrigin(x: Number, y: Number, z: Number, p3: Int) {
		SetDrawOrigin(x, y, z, p3)
	}

	fun getPlayerSwitchState(): Int {
		return GetPlayerSwitchState()
	}

	/**
	 * Shuts down the `loadingScreen` NUI frame, similarly to `SHUTDOWN_LOADING_SCREEN`.
	 */
	fun shutdownLoadingScreenNui() {
		ShutdownLoadingScreenNui()
	}

	fun getGameTimer(): Int {
		return GetGameTimer()
	}

	/**
	 * Resets the screen's draw-origin which was changed by the function GRAPHICS::SET_DRAW_ORIGIN(...) back to x=0,y=0.
	 * See GRAPHICS::SET_DRAW_ORIGIN(...) for further information.
	 */
	fun clearDrawOrigin() {
		ClearDrawOrigin()
	}

	fun getNumberOfPlayers(): Int {
		return GetNumberOfPlayers()
	}

	class ModelLoadingTimeoutException(message: String) : Exception(message)
}