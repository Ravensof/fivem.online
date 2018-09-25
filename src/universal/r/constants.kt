package universal.r

import MODULE_FOLDER_NAME

const val MAX_PLAYERS = 32
const val MYSQL_API_URL = "http://localhost/fivemapi.php"//"http://fivem.online/resources/fivemapi.php"

val CONSOLE_PREFIX: String
	get() {
		return "[$MODULE_FOLDER_NAME]"
	}

const val CONSOLE_DIR = true
const val CONSOLE_ERROR = true
const val CONSOLE_INFO = true
const val CONSOLE_LOG = true
const val CONSOLE_WARN = true
const val CONSOLE_DEBUG = true
