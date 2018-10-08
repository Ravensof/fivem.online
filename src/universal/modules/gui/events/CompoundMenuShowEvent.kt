package universal.modules.gui.events

import universal.events.IEvent
import universal.modules.gui.MenuItem

class CompoundMenuShowEvent(
		val menu: MenuItem
) : IEvent()