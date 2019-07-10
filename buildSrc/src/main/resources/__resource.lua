resource_manifest_version '05cfa83c-a124-4cfa-a768-c24a5811d8f9'

resource_type 'data' {
	name = 'test'
}

files {
	'nui/resources/main/external_js/jquery-3.3.1.min.js',
	'nui/resources/main/external_js/jquery.scrollTo.min.js',
	'nui/resources/main/external_js/howler_2.0.15.core.min.js',
	'nui/resources/main/external_js/includeHTML.js',
	'nui/lib/kotlinx-html-js.js',
	'nui/lib/nui.js',

	'nui/resources/main/common.js',
	'nui/resources/main/index.html',

	-- splashScreen

	'loadingScreen/resources/main/sound/intro.mp3',
	'loadingScreen/resources/main/images/_preloader.gif',
	'loadingScreen/resources/main/images/char025.png',
	'loadingScreen/resources/main/images/files.txt',
	'loadingScreen/resources/main/images/logo.png',
	'loadingScreen/resources/main/images/mute.png',
	'loadingScreen/resources/main/images/_1.jpg',
	'loadingScreen/resources/main/images/_12.jpg',
	'loadingScreen/resources/main/images/_14.png',
	'loadingScreen/resources/main/images/_1_1.png',
	'loadingScreen/resources/main/images/_2.jpg',
	'loadingScreen/resources/main/images/_3.jpg',
	'loadingScreen/resources/main/images/_3_1.png',
	'loadingScreen/resources/main/images/_4.jpg',
	'loadingScreen/resources/main/images/_4_1.png',
	'loadingScreen/resources/main/images/_7.png',
	'loadingScreen/resources/main/images/_9.jpg',
	'loadingScreen/resources/main/js/index.js',

	'loadingScreen/resources/main/loading_screen.html',
	'loadingScreen/resources/main/index.html',
}

loadscreen "loadingScreen/resources/main/index.html"
ui_page "nui/resources/main/index.html"

client_scripts {

	'client/resources/main/common.js',
	-- start common
	'common/resources/main/js/common.js',
	'common/resources/main/external_js/base64Danko.min.js',
	'common/resources/main/external_js/sha512.min.js',
	'common/lib/kotlin.js',
	'common/lib/kotlinx-coroutines-core.js',
	'common/lib/kotlinx-serialization-kotlinx-serialization-runtime.js',
	'common/lib/common.js',
	-- end common
	'client/resources/main/client.lua',

	'client_natives/lib/client_natives.js',
	'client/lib/client.js',
}

server_scripts {

	'server/resources/main/common.js',
	-- start common
	'common/resources/main/js/common.js',
	'common/resources/main/external_js/base64Danko.min.js',
	'common/resources/main/external_js/sha512.min.js',
	'common/lib/kotlin.js',
	'common/lib/kotlinx-coroutines-core.js',
	'common/lib/kotlinx-serialization-kotlinx-serialization-runtime.js',
	'common/lib/common.js',
	-- end common

	'server/resources/main/server.lua',
	'server_natives/lib/server_natives.js',
	'server/lib/server.js',
}