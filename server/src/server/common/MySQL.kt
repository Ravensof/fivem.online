package server.common

import fivem.Config
import universal.common.Base64
import universal.common.Console
import universal.common.setTimeout
import universal.struct.CustomHttpResponse
import universal.struct.HttpRequestType


object MySQL {

	private const val SERVER = Config.MYSQL_API_URL

	fun filter(value: String): String {
		return "FROM_BASE64('${Base64.toBase64(value)}')"
	}

	fun <T> query(sql: String, onError: (Exception) -> Unit = { Console.error(it.message) }, onSuccess: (Array<T>) -> Unit) {

		Console.debug("MySQL: $sql")

		Server.performHttpRequest(
				SERVER,
				data = mapOf(
						"request" to sql
				),
				type = HttpRequestType.POST) { code, jsonResponse, headers ->

			if (code == 200 && jsonResponse.firstOrNull() == '{') {
				try {
					val response = JSON.parse<CustomHttpResponse<Array<T>>>(jsonResponse)

					if (response.code == 0) {
						onSuccess(response.response)
					} else {
						onError(Exception(response.response.first() as String))
					}
				} catch (exception: Exception) {
					onError(exception)
				}
			} else {
				onError(Exception(code.toString() + " " + jsonResponse))
			}
		}
	}

	fun execute(sql: String, onError: (Exception) -> Unit = { Console.error(it.message) }, onSuccess: () -> Unit = {}) {
		setTimeout {
			query<Any>(sql, onError, { onSuccess() })
		}
	}
}

/*
<?php
if (!empty($_POST)) {
	$return = [
		'code' => 0,
		'response' => [],
	];

	$mysql = new MySQLi();
	$mysql->connect('127.0.0.1', '', '', '');

	$result = $mysql->query($_POST['request']);


	if ($result) {

		if ($result->field_count > 0) {
			while ($data = $result->fetch_assoc()) {
				$return['response'][] = $data;
			}
		}

	} else {
		$return['code'] = 1;
		$return['response'] = [$mysql->error];
	}
	$mysql->close();

	echo json_encode($return);
}
 */
