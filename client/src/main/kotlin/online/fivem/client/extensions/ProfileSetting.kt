package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.ProfileSetting

val ProfileSetting.value: Int
	get() = Natives.getProfileSetting(id)
