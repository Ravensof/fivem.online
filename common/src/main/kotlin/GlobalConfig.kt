package online.fivem.common

import online.fivem.common.entities.InternetRadioStation
import online.fivem.common.gtav.RadioStation

object GlobalConfig {
	const val RESOURCES_HTTP_HOME = "http://fivem.online/nui/resources/main/"
	const val MODULE_NAME = "fivem-online"
	const val SERVER_NAME_IN_MENU = "FIVEM.ONLINE"
	const val MAX_PLAYERS = 32
	const val APP_VERSION = 190107

	val internetRadioStations = mapOf(
		//		RadioStations.RADIO_01_CLASS_ROCK.name to RadioStation(),  // Los Santos Rock Radio
		RadioStation.RADIO_02_POP.name to InternetRadioStation(
			"https://revolutionradio.ru:8443/live.ogg",
			0.7
		),         // Non-Stop-Pop FM
		RadioStation.RADIO_03_HIPHOP_NEW.name to InternetRadioStation("http://stream.radioreklama.bg/nrj.ogg")  // Radio Los Santos
//		RadioStations.RADIO_04_PUNK.name to RadioStation(),        // Channel X
//		RadioStations.RADIO_05_TALK_01.name to RadioStation(),     // West Coast Talk Radio
//		RadioStations.RADIO_06_COUNTRY.name to RadioStation(),     // Rebel Radio
//		RadioStations.RADIO_07_DANCE_01.name to RadioStation(),    // Soulwax FM
//		RadioStations.RADIO_08_MEXICAN.name to RadioStation(),     // East Los FM
//		RadioStations.RADIO_09_HIPHOP_OLD.name to RadioStation(),  // West Coast Classics
//		RadioStation.RADIO_11_TALK_02.name to InternetRadioStation("http://fivem.online:8000/", 2.3)      // Blaine County Radio
//		RadioStations.RADIO_12_REGGAE.name to RadioStation(),      // Blue Ark
//		RadioStations.RADIO_13_JAZZ.name to RadioStation(),        // Worldwide FM
//		RadioStations.RADIO_14_DANCE_02.name to RadioStation(),    // FlyLo FM
//		RadioStations.RADIO_15_MOTOWN.name to RadioStation(),      // The Lowdown 91.1
//		RadioStations.RADIO_16_SILVERLAKE.name to RadioStation(),  // Radio Mirror Park
//		RadioStations.RADIO_17_FUNK.name to RadioStation(),        // Space 103.2
//		RadioStations.RADIO_18_90S_ROCK.name to RadioStation(),    // Vinewood Boulevard Radio
//		RadioStations.RADIO_20_THELAB.name to RadioStation(),      // The Lab
	)

	object BlackOut {
		const val blackOutTime: Long = 2_000
		const val blackOutFromDamage: Boolean = true
		const val blackoutDamageRequired: Int = 25
	}

	const val SHOW_CONSOLE_ERROR = true
	const val SHOW_CONSOLE_INFO = true
	const val SHOW_CONSOLE_WARN = true
	const val SHOW_CONSOLE_DEBUG = true
	const val SHOW_CONSOLE_LOG = true

	const val CONSOLE_PREFIX = "$MODULE_NAME/"

	const val NUI_EVENT_NAME = "nui"
	const val NET_EVENT_NAME = "net"
	const val NET_EVENT_ESTABLISHING_NAME = "establishing"
}