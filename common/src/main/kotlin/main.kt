import online.fivem.common.SerializersList
import online.fivem.common.kSerializer

private fun main() {
	SerializersList.forEach {
		kSerializer.add(it.key, it.value)
	}
}