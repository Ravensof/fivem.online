package web.modules.speedometer

import external.Gauge
import js.externals.jquery.jQuery
import org.w3c.dom.HTMLElement
import universal.extensions.onNull
import universal.modules.AbstractModule
import universal.modules.speedometer.events.SpeedoMeterDisableEvent
import universal.modules.speedometer.events.SpeedoMeterEnableEvent
import universal.modules.speedometer.events.SpeedoMeterUpdateEvent
import web.common.Event
import kotlin.browser.document

class SpeedometerModule private constructor() : AbstractModule() {

	val body = jQuery("body")
//	val block = jQuery("<iframe hidden='hidden' id=\"speedometer\" src='" + RESOURCES_URL + "Speedometer-0.1/Speedometer.html' width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\" style=\"position: absolute; top: 0; left: 0;\"></iframe>")

	private var speedometerBlock: js.externals.jquery.JQuery<HTMLElement>? = null
//	private var speedometerCanvas: js.externals.jquery.JQuery<HTMLElement>?=null

	var debug: js.externals.jquery.JQuery<HTMLElement>? = null

	private var gauge: Gauge? = null

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

			debug?.html(text)

//			gauge.set(it.currentRpm*150)
			gauge?.set(it.dashboardSpeed * 5)
		}

		Event.onNui<SpeedoMeterEnableEvent> {
			speedometerBlock?.show()
		}

		Event.onNui<SpeedoMeterDisableEvent> {
			debug?.html("")
			speedometerBlock?.hide()
		}

		onDomContentLoaded()
	}

	private fun onDomContentLoaded() {
		debug = jQuery("#debug")
		speedometerBlock = jQuery("#$SPEEDOMETER_BLOCK")

		val target = document.getElementById(SPEEDOMETER_CANVAS) // your canvas element

		val opts = object {
			val angle = 0.01 // The span of the gauge arc
			val lineWidth = 0.31 // The line thickness
			val radiusScale = 1 // Relative radius
			val pointer = object {
				val length = 0.57 // // Relative to gauge radius
				val strokeWidth = 0.084 // The thickness
				val color = "#FF0000" // Fill color
			}
			val limitMax = false     // If false, max value increases automatically if value > maxValue
			val limitMin = false     // If true, the min value of the gauge will be fixed
			val colorStart = "#6FADCF"   // Colors
			val colorStop = "#1bb84f"    // just experiment with them
			val strokeColor = "#ffffff"  // to see which ones work best for you
			val generateGradient = true
			val highDpiSupport = true     // High resolution support
			// renderTicks is Optional

			val renderTicks = object {
				val divisions = 5
				val divWidth = 2.8
				val divLength = 0.68
				val divColor = "#000000"
				val subDivisions = 4
				val subLength = 0.48
				val subWidth = 1.2
				val subColor = "#000000"
			}
		}

		target?.let {
			gauge = Gauge(target).apply {
				setOptions(opts) // create sexy gauge!
				maxValue = 250 // set max gauge value
				setMinValue(0)  // Prefer setter over gauge.minValue = 0
				animationSpeed = 20 // set animation speed (32 is default value)
			}
		}
	}

	companion object {

		private const val SPEEDOMETER_BLOCK = "speedometerBlock"
		private const val SPEEDOMETER_CANVAS = "speedometerCanvas"

		private var instance: SpeedometerModule? = null

		fun getInstance(): SpeedometerModule {
			instance.onNull {
				instance = SpeedometerModule()
			}

			return instance!!
		}
	}
}