package online.fivem.client.entities

import online.fivem.Natives

object Date {
	var day: Int
		get() = Natives.getClockDayOfMonth()
		set(value) = Natives.setClockDate(value, month, year)

	var month: Int
		get() = Natives.getClockMonth()
		set(value) = Natives.setClockDate(day, value, year)

	var year: Int
		get() = Natives.getClockYear()
		set(value) = Natives.setClockDate(day, month, value)

	var hour: Int
		get() = Natives.getClockHours()
		set(value) = setTime(value, minute, second)

	var minute: Int
		get() = Natives.getClockMinutes()
		set(value) = setTime(hour, value, second)
	var second: Int
		get() = Natives.getClockSeconds()
		set(value) = setTime(hour, minute, value)

	fun setDate(day: Int, month: Int, year: Int) {
		Natives.setClockDate(day, month, year)
	}

	fun setTime(hour: Int, minute: Int, second: Int) {
		Natives.networkOverrideClockTime(hour, minute, second)
	}
}