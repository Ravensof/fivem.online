package online.fivem.common.events

import online.fivem.common.gtav.RadioStation

class PlayerVehicleRadioEnabledEvent(val radioStation: RadioStation) : PlayerVehicleRadioToggledEvent(true)