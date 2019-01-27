package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import online.fivem.common.common.AbstractModule
import online.fivem.server.common.MySQL
import kotlin.coroutines.CoroutineContext

class MySQLModule(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	val mySQL = MySQL(coroutineContext)

	override fun onInit() {
		mySQL.connect()
	}
}