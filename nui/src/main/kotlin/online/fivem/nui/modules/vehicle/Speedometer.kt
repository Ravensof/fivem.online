package online.fivem.nui.modules.vehicle

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.common.Utils
import online.fivem.common.events.net.SpeedometerDisableEvent
import online.fivem.common.events.net.SpeedometerEnableEvent
import online.fivem.common.events.net.SpeedometerUpdateEvent
import online.fivem.nui.extensions.nuiResourcesLink
import online.fivem.nui.extensions.toHTMLImageElement
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class Speedometer(override val coroutineContext: CoroutineContext) : AbstractModule(), CoroutineScope {

	private val speedometerArrow: HTMLImageElement by lazy {
		jQuery("<img src=\"$RESOURCES_DIR/arrow-speedometer.svg\"/>").toHTMLImageElement()
	}

	private val tachometerArrow: HTMLImageElement by lazy {
		jQuery("<img src=\"$RESOURCES_DIR/arrow-tachometer.svg\"/>").toHTMLImageElement()
	}

	private val speedometerBlock by lazy {
		jQuery(
			"""<div class="speedometer" style="display: none">
			<link rel="stylesheet" href="$RESOURCES_DIR/style.css">
			</div>""".trimIndent()
		)
	}

	private val speedometerInterpolatorChannel = Channel<SpeedometerData>(1)

	private var drawInterpolatorJob: Job? = null

	private val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
	private val context2D: CanvasRenderingContext2D = canvas.getContext("2d") as CanvasRenderingContext2D

	init {
		context2D.canvas.width = 440
		context2D.canvas.height = 212
	}

	override fun onInit() {
		ClientEvent.on<SpeedometerUpdateEvent> {
			if (speedometerInterpolatorChannel.isFull) return@on

			launch {
				speedometerInterpolatorChannel.send(
					SpeedometerData(
						speed = it.dashboardSpeed,
						rpm = it.currentRpm
					)
				)
			}
		}

		ClientEvent.on<SpeedometerEnableEvent> {
			runSpeedometer()
			speedometerBlock.fadeIn()
		}

		ClientEvent.on<SpeedometerDisableEvent> {
			speedometerBlock.fadeOut()
			drawInterpolatorJob?.cancel()
		}
	}

	override fun onStart(): Job? {
		speedometerBlock.append(canvas)
		jQuery("#content").append(speedometerBlock)

		return super.onStart()
	}

	override fun onStop(): Job? {
		drawInterpolatorJob?.cancel()
		speedometerInterpolatorChannel.close()

		return super.onStop()
	}

	private fun runSpeedometer() {
		drawInterpolatorJob?.cancel()
		drawInterpolatorJob = launch {

			var lastSpeed = 0.0
			var lastRpm = 0.0
			var lastUpdate = Date.now()

			var stepRPM: Double
			var stepSpeed: Double

			var del: Long = 0
			var drawingTime: Double

			for (data in speedometerInterpolatorChannel) {
				stepRPM = (data.rpm * 180 - lastRpm) / INTERPOLATION_STEPS
				stepSpeed = (data.speed * Utils.MPS_TO_MILES_PER_HOUR * 180 / 150 - lastSpeed) / INTERPOLATION_STEPS

				for (i in 1..INTERPOLATION_STEPS) {
					drawingTime = Date.now()
					context2D.clearRect(0.0, 0.0, context2D.canvas.width.toDouble(), context2D.canvas.height.toDouble())
					drawRotatedImage(
						tachometerArrow,
						105.0,
						102.0,
						lastRpm,
						tachometerArrow.width.toDouble() / 2,
						17.02
					)
					drawRotatedImage(
						speedometerArrow,
						291.0,
						182.0,
						lastSpeed,
						148.0,
						speedometerArrow.height.toDouble() / 2
					)

					lastRpm += stepRPM
					lastSpeed += stepSpeed

					if (i != INTERPOLATION_STEPS && !speedometerInterpolatorChannel.isFull) {
						delay(del - (Date.now() - drawingTime).toLong())
					}
				}

				del = (Date.now() - lastUpdate).toLong() / INTERPOLATION_STEPS
				lastUpdate = Date.now()
			}
		}
	}

	private fun drawRotatedImage(
		image: HTMLImageElement,
		x: Double,
		y: Double,
		angle: Double,
		rotatePointX: Double,
		rotatePointY: Double
	) {

		context2D.save()
		context2D.translate(x, y)
		context2D.rotate(angle * TO_RADIANS)
		context2D.drawImage(image, -rotatePointX, -rotatePointY)
		context2D.restore()
	}

	private class SpeedometerData(
		val speed: Double,
		val rpm: Double
	)

	companion object {

		private val RESOURCES_DIR = Html.nuiResourcesLink("speedometer/v1")

		private const val TO_RADIANS = kotlin.math.PI / 180
		private const val INTERPOLATION_STEPS = 10
	}
}