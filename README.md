
# **FIVEM.ONLINE RU**

небольшой фрейморк, написанный на Kotlin, для создания собственных игровых сценариев к FiveM.

вам понадобятся ненмого знаний по работе с gradle, kotlin, node.js и умение поднять свой fivem, ftp и mysql сервер.

## **Установка**

для работы нужно установить node.js, а так же ftp, mysql и fivem сервера

### **node.js**

необходимо установить следующие модули в папку мода:
`express`
`mysql`
`stream`
`easyrtc`

лежать они должны в
 
`%server_dir%/resources/fivem-online/node_modules/`

### **FTP**

для удобства, после сборки проекта, все "скомпилированные" файлы будут отправляться на ftp сервер, поэтому в файле
`\buildSrc\src\main\kotlin\Config.kt`
необходимо указать данные ftp сервера

	host = "your.ftp.server.com"
	user = "your_ftp_user"
	password = "your_ftp_user"
	root = "your_server_path/resources/fivem-online/"

### **MySQL**

mysql уже должен быть установлен.

данные для подключения, указываются здесь
 
`\server\src\main\kotlin\online\fivem\server\ServerConfig.kt`

	val MYSQL_PARAMS = Params(
		host = "127.0.0.1",
		database = "fivem",
		user = "root",
		password = ""
	)
	
### **прочие настройки**

в файле

`\common\src\main\kotlin\online\fivem\common\GlobalConfig.kt`

стоит обратить внимание на 

	const val MODULE_NAME = "fivem-online" // название папки fivem модуля в папке your_server_path/resources/
	const val SERVER_IP = "server1.fivem.online" // ip адрес fivem сервера
	const val SERVER_PORT = 30120 //порт сервера, должен совпадать с значением в server.cfg
	const val MAX_PLAYERS = 32 // должно совпадать со значением в server.cfg
	
в `server.cfg` добавить 

`start fivem-online`

также нужно открыть порты 

	const val SERVER_PORT = 30120
	const val HTTP_PORT = SERVER_PORT + 1 // нужно для отображения html интерфейса
	const val WEBRTC_PORT = HTTP_PORT + 1 // пока не используется
	
т.е. `30120`, `30121`, `30122`


всё, теперь после сборки проекта, всё должно загружаться на сервер в 
нужную папку, остаётся только запустить fivem сервер.

## **Написание модуля**

весь проект поделен на несколько gradle модулей

	common/
           | client/           
           | server/            
            nui/
            loadingScreen/
        

                
-

    client/
        modules/
        
    server/
        modules/
        
    nui/
        modules/
        
    loadingScreen/
        modules/



---
# **FIVEM.ONLINE EN**

_in progress.._


ravensof@fivem.online