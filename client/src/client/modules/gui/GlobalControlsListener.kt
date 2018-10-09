package client.modules.gui

import client.common.ControlListener
import client.common.ControlManager
import universal.r.Controls

class GlobalControlsListener {

	private val controlsListener = ControlListener()

	init {

		controlsListener.onKeyShortPressed(Controls.Keys.INPUT_INTERACTION_MENU) {
			InteractionMenu()
					.show()
		}

		ControlManager.attach(controlsListener)
	}
}