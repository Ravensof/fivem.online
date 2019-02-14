package online.fivem.client.modules.role_play_system

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import online.fivem.client.gtav.Client
import online.fivem.common.common.AbstractModule
import kotlin.coroutines.CoroutineContext

class RolePlaySystemModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = SupervisorJob()

	override fun onInit() {
		Client.setPlayerHealthRechargeMultiplier(Client.getPlayerId(), 0f)
	}
}