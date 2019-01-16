package online.fivem.client.common

class Stack<Type> {

	private var index = -1
	private val _stack = mutableMapOf<Int, Type>()

	val stack = _stack.map { it.value }

	fun add(data: Type): Int {
		val index = ++this.index

		_stack[index] = data
		return index
	}

	fun remove(index: Int) {
		_stack.remove(index)
	}

	fun clear() {
		_stack.clear()
	}
}