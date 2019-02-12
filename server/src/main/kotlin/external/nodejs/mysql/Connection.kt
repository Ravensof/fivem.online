package external.nodejs.mysql

import external.nodejs.stream.Transform

private typealias Error = String

external interface Connection {

	val threadId: Int

	fun connect()
	fun end()
	fun end(callback: (String?) -> Unit)

	fun escape(raw: String): String
	fun escapeId(identifier: String): String

	fun query(query: String): Query
	fun query(query: String, params: Any): Query

	fun query(query: String, callback: (Error?, Results, Array<Connection.Field>) -> Unit)
	fun query(query: String, params: Any, callback: (Error?, Results, Array<Connection.Field>) -> Unit)

	fun pause()
	fun resume()

	interface Results {
		val insertId: Int?
		val affectedRows: Int
		val changedRows: Int
	}

	interface Query {

		fun stream(): Stream

		/**
		 * var query = connection.query('SELECT * FROM posts');
		 * query
		 *  .on('error', function(err) {
		 *      // Handle error, an 'end' event will be emitted after this as well
		 *  })
		 *  .on('fields', function(fields) {
		 *      // the field packets for the rows to follow
		 *  })
		 *  .on('result', function(row) {
		 *      // Pausing the connnection is useful if your processing involves I/O
		 *      connection.pause();
		 *
		 *      processRow(row, function() {
		 *      connection.resume();
		 *  });
		 * })
		 *  .on('end', function() {
		 *      // all rows have been received
		 *  });
		 */
		fun on(event: String, callback: (Any) -> Unit)

		interface Stream {
			fun pipe(transform: Transform): Stream

			fun on(event: String, callback: () -> Unit)
		}
	}

	interface Field {
		val catalog: String
		val db: String
		val table: String
		val orgTable: String
		val name: String
		val orgName: String
		val charsetNr: Int
		val length: Int
		val type: Int
		val flags: Int
		val decimals: Int
		val zeroFill: Boolean
		val protocol41: Boolean
	}

	interface Result
}