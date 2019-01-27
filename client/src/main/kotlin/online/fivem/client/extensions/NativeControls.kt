package online.fivem.client.extensions

import online.fivem.client.gtav.Client
import online.fivem.common.gtav.NativeControls

@Suppress("DEPRECATION")
fun NativeControls.Keys.disableControlAction(disable: Boolean = true) =
	Client.disableControlAction(Client.defaultControlGroup.index, index, disable)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isDisabledControlJustPressed() =
	Client.isDisabledControlJustPressed(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isDisabledControlJustReleased() =
	Client.isDisabledControlJustReleased(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isDisabledControlPressed() =
	Client.isDisabledControlPressed(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isControlEnabled() =
	Client.isControlEnabled(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isControlJustPressed() =
	Client.isControlJustPressed(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isControlJustReleased() =
	Client.isControlJustReleased(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isControlPressed() =
	Client.isControlPressed(Client.defaultControlGroup.index, index)

@Suppress("DEPRECATION")
fun NativeControls.Keys.isControlReleased() =
	Client.isControlReleased(Client.defaultControlGroup.index, index)

