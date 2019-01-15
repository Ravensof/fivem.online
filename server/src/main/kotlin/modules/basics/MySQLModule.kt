package online.fivem.server.modules.basics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.common.common.AbstractModule
import online.fivem.server.common.MySQL
import kotlin.coroutines.CoroutineContext

class MySQLModule : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	val mySQL = MySQL(coroutineContext)

	override fun init() {
		mySQL.connect()
	}
}