package external.nodejs.mysql

external interface MySQL {

	fun createConnection(params: Params): Connection

	fun createConnection(params: String): Connection

//	fun createPool(...): Pool

	fun raw(string: String): String

	fun format(sql: String, inserts: Array<Any>): String
}

class Params(
	val host: String = "localhost",
	val port: Int = 3306,
	val user: String = "root",
	val password: String = "",
	val database: String,

	val charset: String = "UTF8_GENERAL_CI",
	val connectTimeout: Int = 0,
	val connectionLimit: Int = 10,
	val trace: Boolean = true
)