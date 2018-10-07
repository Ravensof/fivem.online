package web.modules.speedometer

import RESOURCES_URL
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLImageElement
import universal.common.clearInterval
import universal.common.setInterval
import universal.extensions.onNull
import universal.modules.AbstractModule
import universal.modules.speedometer.events.SpeedoMeterDisableEvent
import universal.modules.speedometer.events.SpeedoMeterEnableEvent
import universal.modules.speedometer.events.SpeedoMeterUpdateEvent
import web.common.Event
import web.extensions.toHTMLCanvasElement
import web.extensions.toHTMLImageElement
import kotlin.js.Date

class SpeedometerModule private constructor() : AbstractModule() {


	private val speedometerArrow: HTMLImageElement? by lazy {
		jQuery("<img src=\"${RESOURCES_URL}Speedometer-0.1/arrow-speedometer.svg\"/>").toHTMLImageElement()
	}

	private val tachometerArrow: HTMLImageElement? by lazy {
		jQuery("<img src=\"${RESOURCES_URL}Speedometer-0.1/arrow-tachometer.svg\"/>").toHTMLImageElement()
	}

	private val speedometerCanvas by lazy {
		jQuery("<canvas id=\"$SPEEDOMETER_CANVAS\" width=\"440\" height=\"212\"></canvas>")
	}

	private val speedometerBlock by lazy {
		jQuery("""
		<div id="$SPEEDOMETER_BLOCK" class="speedometer" style="display: none">
			<link rel="stylesheet" href="${RESOURCES_URL}Speedometer-0.1/style.css">
		</div>
	""".trimIndent()).apply {
			append(speedometerCanvas)
		}
	}

	private val body = jQuery("#content").apply {
		append(speedometerBlock)
	}

	private var context: dynamic = speedometerCanvas.toHTMLCanvasElement()?.getContext("2d")


	init {

		Event.onNui<SpeedoMeterUpdateEvent> {

			//			val text =
//					"currentRpm  ${it.currentRpm} <br />" +
//							"dashboardSpeed ${it.dashboardSpeed} <br />" + //*2.236936 mph
//							"turboPressure ${it.turboPressure} <br />" +
//							"engineHealth ${it.engineHealth} <br />" +
//							"engineOn ${it.engineOn} <br />" +
//							"engineRunning ${it.engineRunning} <br />" +
//							"engineTemperature ${it.engineTemperature} <br />" +
//							"currentGear ${it.currentGear} <br />" +
//							"fuelLevel ${it.fuelLevel} <br />" +
//							"handbrake ${it.handbrake} <br />" +
//							"oilLevel ${it.oilLevel} <br />" +
//							"petrolTankHealth ${it.petrolTankHealth} <br />"
//
//			Console.logWeb(text)

			drawSpeedo(it.dashboardSpeed * 2.236936 * 180 / 150, it.currentRpm * 180)
		}

		Event.onNui<SpeedoMeterEnableEvent> {
			speedometerBlock.show()
		}

		Event.onNui<SpeedoMeterDisableEvent> {
			speedometerBlock.hide()
		}
	}

	private fun drawRotatedImage(image: HTMLImageElement, x: Int, y: Int, angle: Double, rotatePointX: Double, rotatePointY: Double) {

		context.save()

		context.translate(x, y)

		context.rotate(angle * TO_RADIANS)

		context.drawImage(image, -rotatePointX, -rotatePointY)

		context.restore()
	}


	private var lastSpeed: Double = 0.0
	private var lastRpm: Double = 0.0
	private var lastUpdate = Date.now()
	private var intervalUpdateId: dynamic = 0f

	private fun drawSpeedo(speed: Double, rpm: Double, turbo: Double? = null) {
		val n = 10

		val stepRPM = (rpm - this.lastRpm) / n
		val stepSpeed = (speed - this.lastSpeed) / n

		var i = 1

		clearInterval(intervalUpdateId)

		intervalUpdateId = setInterval((Date.now() - lastUpdate).toInt() / n) {

			context.clearRect(0, 0, speedometerCanvas.width(), speedometerCanvas.height())

			tachometerArrow?.let {
				drawRotatedImage(it, 105, 102, this.lastRpm, it.width.toDouble() / 2, 17.02)
			}
			speedometerArrow?.let {
				drawRotatedImage(it, 291, 182, this.lastSpeed, 148.0, it.height.toDouble() / 2)
			}

			this.lastRpm += stepRPM
			this.lastSpeed += stepSpeed

			if (i >= n) {
				clearInterval(intervalUpdateId)

				this.lastRpm = rpm
				this.lastSpeed = speed
			}
			i++
		}

		lastUpdate = Date.now()
	}

	companion object {

		private const val SPEEDOMETER_BLOCK = "speedometer_block"
		private const val SPEEDOMETER_CANVAS = "speedometer_canvas"

		private const val TO_RADIANS = kotlin.math.PI / 180

		private var instance: SpeedometerModule? = null

		fun getInstance(): SpeedometerModule {
			instance.onNull {
				instance = SpeedometerModule()
			}

			return instance!!
		}
	}
}