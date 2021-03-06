package online.fivem.client.events

import online.fivem.common.gtav.ProfileSetting

sealed class ProfileSettingUpdatedEvent(
	val profileSetting: ProfileSetting,
	val value: Int
) {

	class AudioMusicLevelInMP(
		volume: Int
	) : ProfileSettingUpdatedEvent(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP, volume)
}