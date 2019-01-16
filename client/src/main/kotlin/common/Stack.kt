package online.fivem.client.common

class Stack<Type> : Iterable<Type> {

	private var index = -1
	private val _stack = mutableMapOf<Int, Type>()

	fun add(data: Type): Int {
		val index = ++this.index

		_stack[index] = data

		return index
	}

	override fun iterator(): Iterator<Type> {
		return _stack.map { it.value }.iterator()
	}

	fun remove(index: Int) {
		_stack.remove(index)
	}

	fun clear() {
		_stack.clear()
	}
}