package online.fivem.common.events

open class PlayerVehicleRadioToggledEvent(val isEnabled: Boolean)

class PlayerVehicleRadioEnabledEvent : PlayerVehicleRadioToggledEvent(true)

class PlayerVehicleRadioDisabledEvent : PlayerVehicleRadioToggledEvent(false)