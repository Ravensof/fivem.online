package client.modules.radio.data

import shared.modules.radio.InternetRadioStation
import shared.r.RadioStation

val RadioStationList = mapOf(
//		RadioStations.RADIO_01_CLASS_ROCK.name to RadioStation(),  // Los Santos Rock Radio
		RadioStation.RADIO_02_POP.name to InternetRadioStation("https://revolutionradio.ru:8443/live.ogg"),         // Non-Stop-Pop FM
		RadioStation.RADIO_03_HIPHOP_NEW.name to InternetRadioStation("http://stream.radioreklama.bg/nrj.ogg"),  // Radio Los Santos
//		RadioStations.RADIO_04_PUNK.name to RadioStation(),        // Channel X
//		RadioStations.RADIO_05_TALK_01.name to RadioStation(),     // West Coast Talk Radio
//		RadioStations.RADIO_06_COUNTRY.name to RadioStation(),     // Rebel Radio
//		RadioStations.RADIO_07_DANCE_01.name to RadioStation(),    // Soulwax FM
//		RadioStations.RADIO_08_MEXICAN.name to RadioStation(),     // East Los FM
//		RadioStations.RADIO_09_HIPHOP_OLD.name to RadioStation(),  // West Coast Classics
		RadioStation.RADIO_11_TALK_02.name to InternetRadioStation("http://filev.online:8000/")      // Blaine County Radio
//		RadioStations.RADIO_12_REGGAE.name to RadioStation(),      // Blue Ark
//		RadioStations.RADIO_13_JAZZ.name to RadioStation(),        // Worldwide FM
//		RadioStations.RADIO_14_DANCE_02.name to RadioStation(),    // FlyLo FM
//		RadioStations.RADIO_15_MOTOWN.name to RadioStation(),      // The Lowdown 91.1
//		RadioStations.RADIO_16_SILVERLAKE.name to RadioStation(),  // Radio Mirror Park
//		RadioStations.RADIO_17_FUNK.name to RadioStation(),        // Space 103.2
//		RadioStations.RADIO_18_90S_ROCK.name to RadioStation(),    // Vinewood Boulevard Radio
//		RadioStations.RADIO_20_THELAB.name to RadioStation(),      // The Lab
)