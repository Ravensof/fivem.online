package external

import org.w3c.dom.Element

external class Gauge(element: Element) {
	var maxValue: Int
	var animationSpeed: Int

	fun setMinValue(value: Int)

	fun setOptions(options: Any)

	fun set(value: Double)
}