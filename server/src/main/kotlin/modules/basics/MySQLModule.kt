package online.fivem.server.modules.basics

import online.fivem.common.common.AbstractModule
import online.fivem.server.common.MySQL

class MySQLModule : AbstractModule() {

	val mySQL = MySQL()

	override fun init() {
		mySQL.connect()
	}
}