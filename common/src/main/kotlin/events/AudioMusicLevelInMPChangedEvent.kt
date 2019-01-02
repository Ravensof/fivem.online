package online.fivem.common.events

import online.fivem.common.gtav.ProfileSetting

class AudioMusicLevelInMPChangedEvent(
	volume: Int
) : ProfileSettingUpdatedEvent(ProfileSetting.AUDIO_MUSIC_LEVEL_IN_MP, volume)