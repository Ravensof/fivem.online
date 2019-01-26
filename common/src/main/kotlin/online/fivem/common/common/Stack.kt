package online.fivem.common.common

class Stack<Type> : Iterable<Type> {

	private var index = UNDEFINED_INDEX
	private val _stack = mutableMapOf<Int, Type>()
	private val indexes = mutableListOf<Int>()

	fun add(data: Type): Int {
		val index = ++this.index

		indexes += index
		_stack[index] = data

		return index
	}

	override fun iterator(): Iterator<Type> {
		return _stack.map { it.value }.iterator()
	}

	fun lastOrNull(): Type? {
		indexes.lastOrNull()?.let {
			return _stack[it]
		}

		return null
	}

	fun remove(index: Int) {
		indexes.remove(index)
		_stack.remove(index)
	}

	fun clear() {
		_stack.clear()
	}

	companion object {
		const val UNDEFINED_INDEX = Int.MIN_VALUE
	}
}