package server

import shared.Console
import shared.encodeURIComponent
import shared.struct.CustomHttpResponse
import shared.struct.HttpRequestType


object MySQL {

	private const val SERVER = "http://localhost/fivemapi.php"

	fun <T> query(sql: String, onError: (Exception) -> Unit = { Console.error(it.message) }, onSuccess: (Array<T>) -> Unit) {
		Server.performHttpRequest(SERVER, data = "request=" + encodeURIComponent(sql), type = HttpRequestType.POST) { code, jsonResponse, headers ->
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
		query<Any>(sql, onError, { onSuccess() })
	}
}

/*

<?php
if(!empty($_POST)){
	$mysql=new MySQLi();
	$mysql->connect('127.0.0.1', 'root', '', 'db');

	$result=$mysql->query($_POST['request']);

	$return=[
		'code'=>0,
		'response'=>[],
	];

	if($result){

		while ($data=$result->fetch_assoc()){
			$return['response'][]=$data;
		}

	}else{
		$return['code']=1;
		$return['response']=[$mysql->error];
	}

	echo json_encode($return);
}

 */
