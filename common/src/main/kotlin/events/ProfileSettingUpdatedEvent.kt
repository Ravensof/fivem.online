package online.fivem.common.events

import online.fivem.common.gtav.ProfileSetting

open class ProfileSettingUpdatedEvent(
	val profileSetting: ProfileSetting,
	val value: Int
)