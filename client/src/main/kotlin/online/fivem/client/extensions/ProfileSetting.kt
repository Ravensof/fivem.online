package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.ProfileSetting

val ProfileSetting.value: Int
	get() = Client.getProfileSetting(id)
