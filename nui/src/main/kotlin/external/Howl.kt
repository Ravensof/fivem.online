package online.fivem.nui.external

//https://github.com/goldfire/howler.js/blob/master/README.md
external class Howl(options: HowlOptions) {

	fun volume(volume: Double)
	fun volume(volume: Double, id: Int)

	fun mute(mute: Boolean)
	fun mute(mute: Boolean, id: Int)

	fun play(): Int
	fun play(id: String): Int

	fun pause()
	fun pause(id: String)

	fun stop()
	fun stop(id: Int)

	fun playing(id: Int): Boolean

	//load, loaderror, playerror, play, end, pause, stop, mute, volume, rate, seek, fade, unlock
	fun on(event: String, function: () -> Unit)

	fun on(event: String, function: () -> Unit, id: Int)

	fun once(event: String, function: () -> Unit)
	fun once(event: String, function: () -> Unit, id: Int)

	fun load()
	fun unload()
}

class HowlOptions(
	val src: Array<String>,
	val html5: Boolean = true,
	val autoplay: Boolean = false,
	val loop: Boolean = false,
	val preload: Boolean = true,
	val mute: Boolean = false,
	val rate: Double = 1.0,
	val pool: Int = 5,
	val xhrWithCredentials: Boolean = true,
	val format: List<String> = listOf("opus", "ogg", "mp3"),
	val volume: Double = 1.0,

	val onload: () -> Unit = {},
	val onloaderror: () -> Unit = {},
	val onplay: () -> Unit = {},
	val onend: () -> Unit = {},
	val onpause: () -> Unit = {},
	val onstop: () -> Unit = {},
	val onmute: () -> Unit = {},
	val onvolume: () -> Unit = {}
)