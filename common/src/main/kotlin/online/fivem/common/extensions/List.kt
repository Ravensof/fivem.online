package online.fivem.common.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@UseExperimental(ExperimentalContracts::class)
fun List<*>?.isNotEmpty(): Boolean {
	contract {
		returns(true) implies (this@isNotEmpty != null)
	}

	return this != null && !isEmpty()
}

inline fun <A, B> List<Pair<A, B>>.forEach(action: (A, B) -> Unit) {
	for ((a, b) in this) {
		action(a, b)
	}
}