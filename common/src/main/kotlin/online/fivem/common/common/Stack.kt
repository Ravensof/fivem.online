package online.fivem.common.common

class Stack<Type> : Iterable<Type> {

	private var index: Handle = UNDEFINED_INDEX
	private val _stack = mutableMapOf<Handle, Type>()
	private val indexes = mutableListOf<Handle>()

	fun add(data: Type): Handle {
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

	fun isEmpty() = _stack.isEmpty()

	fun isNotEmpty() = _stack.isNotEmpty()

	fun remove(index: Handle) {
		indexes.remove(index)
		_stack.remove(index)
	}

	fun clear() {
		_stack.clear()
	}

	companion object {
		const val UNDEFINED_INDEX = Handle.MIN_VALUE
	}
}