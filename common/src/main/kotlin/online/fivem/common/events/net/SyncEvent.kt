package online.fivem.common.events.net

import online.fivem.common.other.KotlinXSerializationPacket
import online.fivem.common.other.Serializable

class SyncEvent(
	val data: List<KotlinXSerializationPacket>
) : Serializable()