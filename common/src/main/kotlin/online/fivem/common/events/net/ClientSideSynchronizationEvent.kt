package online.fivem.common.events.net

import online.fivem.common.events.net.sync.RolePlaySystemSaveEvent
import online.fivem.common.other.Serializable

@kotlinx.serialization.Serializable
class ClientSideSynchronizationEvent(

	var rolePlaySystem: RolePlaySystemSaveEvent? = null

) : Serializable()