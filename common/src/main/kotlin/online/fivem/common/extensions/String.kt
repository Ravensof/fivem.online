package online.fivem.common.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@UseExperimental(ExperimentalContracts::class)
fun String?.isEmpty(): Boolean {
	contract {
		returns(false) implies (this@isEmpty != null)
	}

	return this == null || trim().isBlank()
}

@UseExperimental(ExperimentalContracts::class)
fun String?.isNotEmpty(): Boolean {
	contract {
		returns(true) implies (this@isNotEmpty != null)
	}

	return !isEmpty()
}