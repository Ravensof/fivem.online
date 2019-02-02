package online.fivem.common.events

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import online.fivem.common.entities.InternetRadioStation

@Serializable
class InternetRadioChangedEvent(
	@ContextualSerialization val internetRadioStation: InternetRadioStation
)