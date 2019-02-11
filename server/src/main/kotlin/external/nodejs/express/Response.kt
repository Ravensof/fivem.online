package external.nodejs.express

// https://expressjs.com/ru/4x/api.html#res
external interface Response {

	fun download()

	fun end()//	Завершение процесса ответа.
	fun end(data: String)
	fun end(data: String, encoding: String)

	/**
	 * Returns the HTTP response header specified by field. The match is case-insensitive.
	 *
	 * res.get('Content-Type');
	 * // => "text/plain"
	 */
	fun get(field: String): String?

	/**
	 * Sends a JSON response. This method sends a response (with the correct content-type) that is the parameter converted to a JSON string using JSON.stringify().
	 *
	 * The parameter can be any JSON type, including object, array, string, Boolean, number, or null, and you can also use it to convert other values to JSON.
	 *
	 * res.json(null);
	 * res.json({ user: 'tobi' });
	 * res.status(500).json({ error: 'message' });
	 */
	fun json(body: Any?)

	fun jsonp(body: Any?)
	//	fun redirect()//	Перенаправление ответа.
//	fun render()//	Вывод шаблона представления.
	fun send(response: String)

	fun sendFile(path: String)
	fun sendFile(path: String, options: Options)

	/**
	 * Sets the response HTTP status code to statusCode and send its string representation as the response body.
	 */
	fun sendStatus(code: Int)

	/**
	 * Sets the Content-Type HTTP header to the MIME type as determined by mime.lookup() for the specified type. If type
	 * contains the “/” character, then it sets the Content-Type to type.
	 *
	 * res.type('.html');              // => 'text/html'
	 * res.type('html');               // => 'text/html'
	 * res.type('json');               // => 'application/json'
	 * res.type('application/json');   // => 'application/json'
	 * res.type('png');                // => image/png:
	 */
	fun type(type: String)
}