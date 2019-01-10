package online.fivem.server.modules.basics

import online.fivem.common.common.AbstractModule

class BasicsModule : AbstractModule() {
	override fun init() {
		moduleLoader.add(CommandsModule())
	}
}