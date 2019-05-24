package external.nodejs.mysql

external interface Pool {

	fun getConnection(action: (Connection.Error?, Connection?) -> Unit)

}