package online.fivem.client.entities

import online.fivem.client.gtav.Client

object Date {
	var day: Int
		get() = Client.getClockDayOfMonth()
		set(value) = Client.setClockDate(value, month, year)

	var month: Int
		get() = Client.getClockMonth()
		set(value) = Client.setClockDate(day, value, year)

	var year: Int
		get() = Client.getClockYear()
		set(value) = Client.setClockDate(day, month, value)

	var hour: Int
		get() = Client.getClockHours()
		set(value) = setTime(value, minute, second)

	var minute: Int
		get() = Client.getClockMinutes()
		set(value) = setTime(hour, value, second)
	var second: Int
		get() = Client.getClockSeconds()
		set(value) = setTime(hour, minute, value)

	fun setDate(day: Int, month: Int, year: Int) {
		Client.setClockDate(day, month, year)
	}

	fun setTime(hour: Int, minute: Int, second: Int) {
		Client.networkOverrideClockTime(hour, minute, second)
	}
}