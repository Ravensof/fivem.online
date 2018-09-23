package external

external class Howl(options: dynamic) {

	fun volume(volume: Double)
	fun volume(volume: Double, id: Int)

	fun mute(mute: Boolean)
	fun mute(mute: Boolean, id: Int)

	fun play(): Int
	fun play(id: String): Int

	fun stop()
	fun stop(id: Int)
}