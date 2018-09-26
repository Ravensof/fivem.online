package web.modules.speedometer

import js.externals.jquery.jQuery
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import universal.extensions.onNull
import universal.modules.AbstractModule
import universal.modules.speedometer.events.SpeedoMeterDisableEvent
import universal.modules.speedometer.events.SpeedoMeterEnableEvent
import universal.modules.speedometer.events.SpeedoMeterUpdateEvent
import web.common.Event
import kotlin.browser.document

class SpeedometerModule private constructor() : AbstractModule() {

	private var speedometerCanvas = document.getElementById(SPEEDOMETER_CANVAS) as HTMLCanvasElement?
	private var context: dynamic = speedometerCanvas?.getContext("2d")

	private var speedometerBlock = jQuery("#$SPEEDOMETER_BLOCK")

	private var speedometerArrow = document.getElementById(SPEEDOMETER_ARROW) as HTMLImageElement?
	private var tachometerArrow = document.getElementById(TACHOMETER_ARROW) as HTMLImageElement?

	private val debug = jQuery("#debug")

	init {

		Event.onNui<SpeedoMeterUpdateEvent> {
			val text =
					"currentRpm  ${it.currentRpm} <br />" +
							"dashboardSpeed ${it.dashboardSpeed} <br />" + //*2.236936 mph
							"turboPressure ${it.turboPressure} <br />" +
							"engineHealth ${it.engineHealth} <br />" +
							"engineOn ${it.engineOn} <br />" +
							"engineRunning ${it.engineRunning} <br />" +
							"engineTemperature ${it.engineTemperature} <br />" +
							"currentGear ${it.currentGear} <br />" +
							"fuelLevel ${it.fuelLevel} <br />" +
							"handbrake ${it.handbrake} <br />" +
							"oilLevel ${it.oilLevel} <br />" +
							"petrolTankHealth ${it.petrolTankHealth} <br />"

			debug.html(text)

			drawSpeedo(it.dashboardSpeed * 2.236936 * 180 / 150, it.currentRpm * 180)
		}

		Event.onNui<SpeedoMeterEnableEvent> {
			debug.show()
			speedometerBlock.show()
		}

		Event.onNui<SpeedoMeterDisableEvent> {
			debug.hide()
			speedometerBlock.hide()
		}
	}

	fun drawRotatedImage(image: HTMLImageElement, x: Int, y: Int, angle: Double, rotatePointX: Double, rotatePointY: Double) {

		context.save()

		context.translate(x, y)

		context.rotate(angle * TO_RADIANS)

		context.drawImage(image, -rotatePointX, -rotatePointY)

		context.restore()
	}

	fun drawSpeedo(speed: Double, rpm: Double, turbo: Double? = null) {

		context.clearRect(0, 0, speedometerCanvas?.width, speedometerCanvas?.height)

		tachometerArrow?.let {
			drawRotatedImage(it, 105, 102, rpm, it.width.toDouble() / 2, 17.02)
		}
		speedometerArrow?.let {
			drawRotatedImage(it, 291, 182, speed, 148.0, it.height.toDouble() / 2)
		}
	}

	companion object {

		private const val SPEEDOMETER_BLOCK = "speedometer_block"
		private const val SPEEDOMETER_CANVAS = "speedometer_canvas"
		private const val SPEEDOMETER_ARROW = "speedometer_arrow"
		private const val TACHOMETER_ARROW = "tachometer_arrow"

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