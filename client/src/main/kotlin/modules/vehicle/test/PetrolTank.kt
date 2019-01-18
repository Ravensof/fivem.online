package online.fivem.client.modules.vehicle.test

class PetrolTank(
	val capacity: Double = 50.0,
	var currentLevel: Double = 50.0

) {

	fun getFuel(amount: Double): Double {
		val result =

			if (amount > currentLevel) {
				currentLevel
			} else {
				amount
			}
		currentLevel -= result

		return result
	}

}