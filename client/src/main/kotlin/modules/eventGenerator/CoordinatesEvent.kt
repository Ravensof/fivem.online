package online.fivem.client.modules.eventGenerator

import online.fivem.common.common.UEvent
import online.fivem.common.entities.Coordinates
import online.fivem.common.extensions.compareTo
import online.fivem.common.extensions.distance

object CoordinatesEvent {
	init {
		UEvent.on<Coordinates> { handle(it) }
	}

	private val onJoinHandlers = mutableMapOf<Point, () -> Unit>()
	private val onExitHandlers = mutableMapOf<Point, () -> Unit>()

	private val inPoints = mutableListOf<Point>()

	fun on(coordinates: Coordinates, radius: Float, onExit: (() -> Unit)? = null, onJoin: () -> Unit): Point {
		val point = Point(coordinates) {
			return@Point coordinates.distance(it) <= radius
		}

		on(point, onExit, onJoin)

		return point
	}

	fun on(point: Point, onExit: (() -> Unit)? = null, onJoin: () -> Unit) {
		onJoinHandlers[point] = onJoin
		onExit?.let {
			onExitHandlers[point] = it
		}
	}

	fun unSubscribe(point: Point) {
		onJoinHandlers.remove(point)
		onExitHandlers.remove(point)
	}

	fun handle(coordinates: Coordinates) {
		onJoinHandlers.forEach {
			if (it.key.function(coordinates)) {
				if (!inPoints.contains(it.key)) {
					inPoints.add(it.key)
					it.value()
				}
			} else {
				if (inPoints.contains(it.key)) {
					inPoints.remove(it.key)
					onExitHandlers[it.key]?.invoke()
				}
			}
		}
	}

	class Point(
		val coordinates: Coordinates,
		val function: (Coordinates) -> Boolean
	)
}