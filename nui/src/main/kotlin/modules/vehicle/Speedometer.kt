package online.fivem.nui.modules.vehicle

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.AbstractModule
import online.fivem.common.common.Html
import online.fivem.common.events.SpeedometerDisableEvent
import online.fivem.common.events.SpeedometerEnableEvent
import online.fivem.common.events.SpeedometerUpdateEvent
import online.fivem.nui.extensions.nuiLink
import online.fivem.nui.extensions.toHTMLCanvasElement
import online.fivem.nui.extensions.toHTMLImageElement
import online.fivem.nui.modules.clientEventEchanger.ClientEvent
import org.w3c.dom.HTMLImageElement
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class Speedometer : AbstractModule(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Job()

	private val speedometerArrow: HTMLImageElement by lazy {
		jQuery("<img src=\"$RESOURCES_DIR/arrow-speedometer.svg\"/>").toHTMLImageElement()
	}

	private val tachometerArrow: HTMLImageElement by lazy {
		jQuery("<img src=\"$RESOURCES_DIR/arrow-tachometer.svg\"/>").toHTMLImageElement()
	}

	private val speedometerCanvas by lazy {
		jQuery("<canvas id=\"$SPEEDOMETER_CANVAS\" width=\"440\" height=\"212\"></canvas>")
	}

	private val speedometerBlock by lazy {
		jQuery(
			"""
		<div id="$SPEEDOMETER_BLOCK" class="speedometer" style="display: none">
			<link rel="stylesheet" href="$RESOURCES_DIR/style.css">
		</div>
	""".trimIndent()
		).apply {
			append(speedometerCanvas)
		}
	}

	private var context: dynamic = speedometerCanvas.toHTMLCanvasElement().getContext("2d")

	private val speedometerInterpolatorChannel = Channel<SpeedometerData>(1)

	private val drawInterpolatorJob by lazy {
		launch {

			var lastSpeed = 0.0
			var lastRpm = 0.0
			var lastUpdate = Date.now()

			var stepRPM: Double
			var stepSpeed: Double

			var del: Long = 0
			var drawingTime: Double

			for (data in speedometerInterpolatorChannel) {
				stepRPM = (data.rpm - lastRpm) / INTERPOLATION_STEPS
				stepSpeed = (data.speed - lastSpeed) / INTERPOLATION_STEPS

				for (i in 1..INTERPOLATION_STEPS) {
					drawingTime = Date.now()
					context.clearRect(0, 0, speedometerCanvas.width(), speedometerCanvas.height())
					drawRotatedImage(tachometerArrow, 105, 102, lastRpm, tachometerArrow.width.toDouble() / 2, 17.02)
					drawRotatedImage(
						speedometerArrow,
						291,
						182,
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

	override fun init() {
		ClientEvent.on<SpeedometerUpdateEvent> {
			if (speedometerInterpolatorChannel.isFull) return@on

			launch {
				speedometerInterpolatorChannel.send(
					SpeedometerData(
						speed = it.dashboardSpeed * 2.236936 * 180 / 150,
						rpm = it.currentRpm * 180
					)
				)
			}
		}

		ClientEvent.on<SpeedometerEnableEvent> {
			speedometerBlock.show()
		}

		ClientEvent.on<SpeedometerDisableEvent> {
			speedometerBlock.hide()
		}
	}

	override fun start(): Job? {
		jQuery("#content").append(speedometerBlock)
		drawInterpolatorJob.start()

		return super.start()
	}

	override fun stop(): Job? {
		drawInterpolatorJob.cancel()
		speedometerInterpolatorChannel.close()

		return super.stop()
	}

	private fun drawRotatedImage(
		image: HTMLImageElement,
		x: Int,
		y: Int,
		angle: Double,
		rotatePointX: Double,
		rotatePointY: Double
	) {

		context.save()
		context.translate(x, y)
		context.rotate(angle * TO_RADIANS)
		context.drawImage(image, -rotatePointX, -rotatePointY)
		context.restore()
	}

	private class SpeedometerData(
		val speed: Double,
		val rpm: Double
	)

	companion object {

		private val RESOURCES_DIR = Html.nuiLink("speedometer/v1")

		private const val SPEEDOMETER_BLOCK = "speedometer_block"
		private const val SPEEDOMETER_CANVAS = "speedometer_canvas"

		private const val TO_RADIANS = kotlin.math.PI / 180
		private const val INTERPOLATION_STEPS = 10
	}
}