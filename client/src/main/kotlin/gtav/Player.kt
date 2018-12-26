package online.fivem.client.gtav

import online.fivem.common.common.Console
import online.fivem.common.gtav.RadioStation


class Player(val id: Int) {

	fun getPed(): Int {
		return Console.checkValue("GetPlayerPed(playerId: Int)", GetPlayerPed(id))
	}

	companion object {

		fun getPed(playerId: Int = -1): Int? {
			return GetPlayerPed(playerId).takeIf { it != 0 }
		}

		fun getId(): Int {
			return GetPlayerIndex()
		}

		fun isInVehicle(): Boolean {
			return IsPlayerVehicleRadioEnabled() == 1
		}

		fun getRadioStation(): RadioStation? {
			return GetPlayerRadioStationName()?.let { RadioStation.valueOf(it) }
		}

		fun isRadioEnabled(): Boolean {
			return getRadioStation() != null
		}
	}
}

//private external fun GetPlayerAdvancedModifierPrivileges(p0: number): string;
//private external fun N_0xcd67ad041a394c9c(p0: number): string;
//private external fun GetContentUserId(p0: number): string;

//private external fun GetPlayerCurrentStealthNoise(player: number): number;

//private external fun GetPlayerFromServerId(serverId: number): number;

/**
 * Returns the group ID the player is member of.
 */
//private external fun GetPlayerGroup(player: number): number;

//private external fun GetPlayerHasReserveParachute(player: number): number;

/**
 * Called 5 times in the scripts. All occurrences found in b617d, sorted alphabetically and identical lines removed:
 * AUDIO::GET_PLAYER_HEADSET_SOUND_ALTERNATE("INOUT", 0.0);
 * AUDIO::GET_PLAYER_HEADSET_SOUND_ALTERNATE("INOUT", 1.0);
 */
//private external fun GetPlayerHeadsetSoundAlternate(p0: string, p1: number)

/**
 * Returns the same as PLAYER_ID and NETWORK_PLAYER_ID_TO_INT
 */
private external fun GetPlayerIndex(): Int

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
//private external fun GetPlayerInvincible(player: number): number;

//private external fun GetPlayerMaxArmour(player: number): number;

/**
 * Returns the players name
 */
//private external fun GetPlayerName(player: number): string;

//private external fun GetPlayerParachutePackTintIndex(player: number, tintIndex: number)

//private external fun GetPlayerParachuteSmokeTrailColor(player: number): [number, number, number];

/**
 * Tints:
 * None = -1,
 * Rainbow = 0,
 * Red = 1,
 * SeasideStripes = 2,
 * WidowMaker = 3,
 * Patriot = 4,
 * Blue = 5,
 * Black = 6,
 * Hornet = 7,
 * AirFocce = 8,
 * Desert = 9,
 * Shadow = 10,
 * HighAltitude = 11,
 * Airbone = 12,
 * Sunrise = 13,
 */
//private external fun GetPlayerParachuteTintIndex(player: number, tintIndex: number)

/**
 * returns the players ped used in many functions
 */
private external fun GetPlayerPed(playerId: Int): Int

//private external fun GetPlayerPedIsFollowing(ped: number): number;

/**
 * Does the same like PLAYER::GET_PLAYER_PED<br/>
 */
//private external fun GetPlayerPedScriptIndex(Player: number): number;

//private external fun GetPlayerRadioStationGenre(): number;

/**
 * Returns 255 (radio off index) if the function fails.
 */
//private external fun GetPlayerRadioStationIndex(): number;

/**
 * Returns active radio station name
 */
private external fun GetPlayerRadioStationName(): String?

/**
 * Tints:
 * None = -1,
 * Rainbow = 0,
 * Red = 1,
 * SeasideStripes = 2,
 * WidowMaker = 3,
 * Patriot = 4,
 * Blue = 5,
 * Black = 6,
 * Hornet = 7,
 * AirFocce = 8,
 * Desert = 9,
 * Shadow = 10,
 * HighAltitude = 11,
 * Airbone = 12,
 * Sunrise = 13,
 */
//private external fun GetPlayerReserveParachuteTintIndex(player: number, index: number)

/**
 * Returns RGB color of the player
 */
//private external fun GetPlayerRgbColour(Player: number): [number, number, number];

//private external fun GetPlayerServerId(player: number): number;

//private external fun GetPlayerShortSwitchState(): number;

//private external fun GetPlayerSprintStaminaRemaining(player: number): number;

//private external fun GetPlayerSprintTimeRemaining(player: number): number;

//private external fun GetPlayerSwitchState(): number;

//private external fun GetPlayerSwitchType(): number;

/**
 * Assigns the handle of locked-on melee target to *entity that you pass it.
 * Returns false if no entity found.
 */
//private external fun GetPlayerTargetEntity(player: number, entity: number): number;

/**
 * Gets the player's team.
 * Does nothing in singleplayer.
 */
external fun GetPlayerTeam(player: Int): Int

//private external fun GetPlayerUnderwaterTimeRemaining(player: number): number;

//private external fun GetPlayerWantedCentrePosition(player: number): number[];

//private external fun GetPlayerWantedLevel(player: number): number;

/**
 * Alternative: GET_VEHICLE_PED_IS_IN(PLAYER_PED_ID(), 1);
 */
//private external fun GetPlayersLastVehicle(): number;

/**
 * Return true while player is being arrested / busted.
 * If atArresting is set to 1, this function will return 1 when player is being arrested (while player is putting his hand up, but still have control)
 * If atArresting is set to 0, this function will return 1 only when the busted screen is shown.
 */
//private external fun IsPlayerBeingArrested(player: number, atArresting: boolean): number;

/**
 * Returns true when the player is not able to control the cam i.e. when running a benchmark test, switching the player or viewing a cutscene.
 * Note: I am not 100% sure if the native actually checks if the cam control is disabled but it seems promising.
 */
//private external fun IsPlayerCamControlDisabled(): number;
/**
 * Returns true when the player is not able to control the cam i.e. when running a benchmark test, switching the player or viewing a cutscene.
 * Note: I am not 100% sure if the native actually checks if the cam control is disabled but it seems promising.
 */
//private external fun N_0x7c814d2fb49f40c0(): number;

/**
 * Returns TRUE if the player ('s ped) is climbing at the moment.
 */
//private external fun IsPlayerClimbing(player: number): number;

/**
 * Can the player control himself, used to disable controls for player for things like a cutscene.
 * ---
 * You can't disable controls with this, use SET_PLAYER_CONTROL(...) for this.
 */
//private external fun IsPlayerControlOn(player: number): number;

//private external fun IsPlayerDead(player: number): number;

/**
 * Gets a value indicating whether the specified player is currently aiming freely.
 */
//private external fun IsPlayerFreeAiming(player: number): number;

/**
 * Gets a value indicating whether the specified player is currently aiming freely at the specified entity.
 */
//private external fun IsPlayerFreeAimingAtEntity(player: number, entity: number): number;

//private external fun IsPlayerFreeForAmbientTask(player: number): number;

//private external fun IsPlayerInCutscene(player: number): number;

/**
 * this function is hard-coded to always return 0.
 */
//private external fun IsPlayerLoggingInNp(): number;

/**
 * Returns TRUE if the game is in online mode and FALSE if in offline mode.
 * This is an alias for NETWORK_IS_SIGNED_ONLINE.
 */
//private external fun IsPlayerOnline(): number;

/**
 * Checks whether the specified player has a Ped, the Ped is not dead, is not injured and is not arrested.
 */
//private external fun IsPlayerPlaying(player: number): number;

//private external fun IsPlayerPressingHorn(player: number): number;

//private external fun IsPlayerReadyForCutscene(player: number): number;

/**
 * Returns true if the player is riding a train.
 */
//private external fun IsPlayerRidingTrain(player: number): number;

//private external fun IsPlayerScriptControlOn(player: number): number;

/**
 * Returns true if the player is currently switching, false otherwise.
 * (When the camera is in the sky moving from Trevor to Franklin for example)
 */
//private external fun IsPlayerSwitchInProgress(): number;
/**
 * Returns true if the player is currently switching, false otherwise.
 * (When the camera is in the sky moving from Trevor to Franklin for example)
 */
//private external fun N_0xd9d2cfff49fab35f(): number;

//private external fun IsPlayerTargettingAnything(player: number): number;

//private external fun IsPlayerTargettingEntity(player: number, entity: number): number;

//private external fun IsPlayerTeleportActive(): number;

private external fun IsPlayerVehicleRadioEnabled(): Int

//private external fun N_0x5f43d83fd6738741(): number;

//private external fun IsPlayerWantedLevelGreater(player: number, wantedLevel: number): number;