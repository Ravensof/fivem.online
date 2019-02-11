package online.fivem.common

import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.gtav.NativeWeather
import online.fivem.common.gtav.RadioStation

@Suppress("MemberVisibilityCanBePrivate")
object GlobalConfig {
	const val MODULE_NAME = "fivem-online"
	const val SERVER_IP = "server1.fivem.online"

	const val SERVER_PORT = 30120
	const val HTTP_PORT = SERVER_PORT + 1
	const val WEBRTC_PORT = HTTP_PORT + 1

	const val SERVER_NAME_IN_MENU = "FIVEM.ONLINE"
	const val MAX_PLAYERS = 32
	const val APP_VERSION = 190107

	val defaultWeather: NativeWeather = NativeWeather.OVERCAST

	val internetRadioStations: Map<String, InternetRadioStation> = mapOf(
		RadioStation.RADIO_01_CLASS_ROCK.name to InternetRadioStation(
			url = "http://de1.internet-radio.com:8005/live",
			name = "Los Santos Rock Radio",
			defaultVolume = 0.8
		),  // Los Santos Rock Radio

		RadioStation.RADIO_02_POP.name to InternetRadioStation(
			url = "https://revolutionradio.ru:8443/live.ogg",
			name = "Revolution Radio",
			defaultVolume = 0.7
		),         // Non-Stop-Pop FM

		RadioStation.RADIO_03_HIPHOP_NEW.name to InternetRadioStation(
			url = "http://stream.radioreklama.bg/nrj.ogg",
			name = "Radio Energy",
			defaultVolume = 0.8
		),  // Radio Los Santos

		RadioStation.RADIO_04_PUNK.name to InternetRadioStation(
			url = "http://158.174.163.217:8000/;?type=http&nocache=79766",
			name = "Synth, EBM, Industrial, C64, and more!",
			defaultVolume = 0.7
		),        // Channel X
//		RadioStations.RADIO_05_TALK_01.name to InternetRadioStation("http://server1.fivem.online:8000/", 2.3),     // West Coast Talk Radio
//		RadioStation.RADIO_06_COUNTRY.name to InternetRadioStation(
//			url="http://us3.internet-radio.com:8297/",
//			name = "Classic Country, The Ranch"
//		),     // Rebel Radio
		RadioStation.RADIO_07_DANCE_01.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/ps_320",
			name = "Pirate Station",
			defaultVolume = 0.3
		),    // Soulwax FM

		RadioStation.RADIO_08_MEXICAN.name to InternetRadioStation(
			url = "http://162.247.76.193:7000/stream",
			name = "Cacoteo Radio",
			defaultVolume = 0.5
		),     // East Los FM

		RadioStation.RADIO_09_HIPHOP_OLD.name to InternetRadioStation(
			url = "http://s6.voscast.com:10898/stream/1/",
			name = "939.40 AT THAT RADIO",
			defaultVolume = 1.4
		),  // West Coast Classics

		RadioStation.RADIO_11_TALK_02.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/hbass_320",
			name = "Hard Bass",
			defaultVolume = 0.4
		),      // Blaine County Radio

//		RadioStation.RADIO_12_REGGAE.name to InternetRadioStation(
//			url = "http://us4.internet-radio.com:8266/",
//			name = "Smooth Jazz Florida"
//		),      // Blue Ark

		RadioStation.RADIO_13_JAZZ.name to InternetRadioStation(
			"http://air.radiorecord.ru:805/fbass_320",
			"Future Bass",
			0.4
		),        // Worldwide FM

		RadioStation.RADIO_14_DANCE_02.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/tm_320",
			name = "Trancemission",
			defaultVolume = 0.4
		),    // FlyLo FM

		RadioStation.RADIO_15_MOTOWN.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/yo_320",
			name = "Yo! FM",
			defaultVolume = 0.8
		),      // The Lowdown 91.1

		RadioStation.RADIO_16_SILVERLAKE.name to InternetRadioStation(
			url = "http://ice3.somafm.com/indiepop-128-mp3",
			name = "SomaFM: Indie Pop Rock",
			defaultVolume = 0.7
		),  // Radio Mirror Park

//		RadioStation.RADIO_17_FUNK.name to InternetRadioStation(
//			url = "http://uk2.internet-radio.com:31491/",
//			name = "Ambient Radio"
//		),        // Space 103.2

		RadioStation.RADIO_18_90S_ROCK.name to InternetRadioStation(
			url = "http://212.83.150.15:8111/stream",
			name = "DISCO FUNK",
			defaultVolume = 0.3
		),    // Vinewood Boulevard Radio

		RadioStation.RADIO_20_THELAB.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/rave_320",
			name = "Rave FM",
			defaultVolume = 0.4
		),      // The Lab

		RadioStation.RADIO_21_DLC_XM17.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/gold_320",
			name = "Gold",
			defaultVolume = 0.6
		),// Blonded Los Santos 97.8 FM

		RadioStation.RADIO_22_DLC_BATTLE_MIX1_RADIO.name to InternetRadioStation(
			url = "http://air.radiorecord.ru:805/techno_320",
			name = "Techno",
			defaultVolume = 0.4
		)//LS UR
	)

	object BlackOut {
		const val BLACKOUT_TIME_FROM_COMMAS: Long = 30 //seconds
		const val ACCELERATION_THRESHOLD: Long = 250 // m/s^2
		const val EXTRA_BLACKOUT_TIME: Long = 15//seconds
		const val WAKING_UP_TIME: Int = 10//seconds
	}

	const val SHOW_CONSOLE_ERROR = true
	const val SHOW_CONSOLE_INFO = true
	const val SHOW_CONSOLE_WARN = true
	const val SHOW_CONSOLE_DEBUG = true
	const val SHOW_CONSOLE_LOG = true

	//dont touch
	const val RESOURCES_HTTP_HOME = "http://$SERVER_IP:$HTTP_PORT/"

	const val CONSOLE_PREFIX = "$MODULE_NAME/"

	const val NUI_EVENT_NAME = "nui"
	const val NET_EVENT_NAME = "net"
	const val NET_EVENT_ESTABLISHING_NAME = "establishing"
}