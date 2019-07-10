@file:Suppress("DEPRECATION")

package online.fivem.client.extensions

import online.fivem.Natives
import online.fivem.common.gtav.NativeControls

fun NativeControls.Keys.disableControlAction(disable: Boolean = true) =
	Natives.disableControlAction(Natives.defaultControlGroup.index, index, disable)

fun NativeControls.Keys.isDisabledControlJustPressed() =
	Natives.isDisabledControlJustPressed(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isDisabledControlJustReleased() =
	Natives.isDisabledControlJustReleased(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isDisabledControlPressed() =
	Natives.isDisabledControlPressed(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isControlEnabled() =
	Natives.isControlEnabled(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isControlJustPressed() =
	Natives.isControlJustPressed(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isControlJustReleased() =
	Natives.isControlJustReleased(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isControlPressed() =
	Natives.isControlPressed(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.isControlReleased() =
	Natives.isControlReleased(Natives.defaultControlGroup.index, index)

fun NativeControls.Keys.getDisabledControlNormal() =
	Natives.getDisabledControlNormal(Natives.defaultControlGroup.index, index)

