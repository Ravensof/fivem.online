<?php
if (!empty($_POST)) {
	$return = [
		'code' => 0,
		'response' => [],
	];

	$mysql = new MySQLi();
	$mysql->connect('127.0.0.1', 'fivem', 'qwerta', 'fivem');

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
