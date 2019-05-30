package online.fivem.nui.modules.vehicle.view

import js.externals.jquery.jQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.fivem.common.common.Html
import online.fivem.common.common.Utils
import online.fivem.common.common.createSupervisorJob
import online.fivem.nui.common.View
import online.fivem.nui.extensions.getImage
import online.fivem.nui.extensions.nuiResourcesLink
import org.w3c.dom.HTMLImageElement
import kotlin.coroutines.CoroutineContext
import kotlin.js.Date

class SpeedometerSimpleView : View(), CoroutineScope {
	override val coroutineContext: CoroutineContext = createSupervisorJob()

	var data: SpeedometerData? = null
		set(value) {
			field = value ?: return

			launch {
				speedometerInterpolatorChannel.send(
					SpeedometerData(
						speed = value.speed,
						rpm = value.rpm
					)
				)
			}
		}

	private val speedometerArrow: HTMLImageElement = Html.getImage("$RESOURCES_DIR/arrow-speedometer.svg")

	private val tachometerArrow: HTMLImageElement = Html.getImage("$RESOURCES_DIR/arrow-tachometer.svg")

	private val speedometerBlock by lazy {
		jQuery(
			"""<div class="speedometer" style="display: none">
			<link rel="stylesheet" href="$RESOURCES_DIR/style.css">
			</div>""".trimIndent()
		)
	}

	private val speedometerInterpolatorChannel = Channel<SpeedometerData>(1)

	private var drawInterpolatorJob: Job? = null

	init {
		context2D.canvas.width = 440
		context2D.canvas.height = 212
	}

	override fun onShow() {
		runSpeedometer()
	}

	override fun onHide() {
		drawInterpolatorJob?.cancel()
	}

	override fun onRemove() {
		drawInterpolatorJob?.cancel()
		speedometerInterpolatorChannel.close()
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

					if (i != INTERPOLATION_STEPS) {
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

	class SpeedometerData(
		val speed: Double,
		val rpm: Double
	)

	companion object {

		private val RESOURCES_DIR = Html.nuiResourcesLink("modules/speedometer/v1")

		private const val TO_RADIANS = kotlin.math.PI / 180
		private const val INTERPOLATION_STEPS = 10
	}
}