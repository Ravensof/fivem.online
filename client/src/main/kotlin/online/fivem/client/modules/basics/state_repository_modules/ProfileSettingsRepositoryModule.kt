package online.fivem.client.modules.basics.state_repository_modules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.value
import online.fivem.common.common.createSupervisorJob
import online.fivem.common.gtav.ProfileSetting
import online.fivem.extensions.Native

class ProfileSettingsRepositoryModule : AbstractClientModule() {

	override val coroutineContext = createSupervisorJob() + Dispatchers.Native

	private val channels = mutableMapOf<ProfileSetting, ConflatedBroadcastChannel<Int>>()

	val profileSettingsProvider = ProfileSettingChannelProvider(channels)

	fun check() = launch {
		channels.forEach {
			val profileSetting = it.key
			val channel = it.value

			val currentValue = profileSetting.value

			if (currentValue != channel.valueOrNull) {
				channel.send(currentValue)
			}
		}
	}


	class ProfileSettingChannelProvider(private val channels: MutableMap<ProfileSetting, ConflatedBroadcastChannel<Int>>) {
		operator fun get(profileSetting: ProfileSetting): ConflatedBroadcastChannel<Int> {
			return channels.getOrPut(profileSetting) {
				ConflatedBroadcastChannel()
			}
		}
	}
}


