package online.fivem.common.common

import online.fivem.common.other.SerializerInterface
import kotlin.reflect.KClass

object Serializer : SerializerInterface {

	override fun getSerializerHash(kClass: KClass<*>): Int = -1

	override fun <T : Any> serialize(obj: T): String {
		return JSON.stringify(prepare(obj))
	}

	override fun deserialize(serializerId: Int, string: String): Any? {
		return Serializer.deserialize(string)
	}

	inline fun <reified R> deserialize(jsonString: String): R {
		return unpack(JSON.parse(jsonString))
	}

	inline fun <reified R> unpack(obj: Any): R {
		val unpacked = uncheckedUnpack(obj)

		if (unpacked !is R) {
			throw DeserializationException("can't deserialize object ${unpacked::class.simpleName} as ${R::class.simpleName}")
		}

		return unpacked
	}

	/**
	 * не видит классы, лежащие не в common
	 */
	fun uncheckedUnpack(obj: Any): Any {

		return js(
			"""
function unserialize1(object) {
	if (object == null || typeof object != "object") return object

	if (typeof object.__className == "undefined" || typeof object.data == "undefined") return object

	if (object.__className == "") return unserialize1(object.data)

	var className = object.__className

	var recoveredObject = Object.create(eval(className + ".prototype"))

	for (var key in object.data) {
		recoveredObject[key] = unserialize1(object.data[key]);
	}

	if (Array.isArray(object.data)) {
		recoveredObject = Object.values(recoveredObject)
	}

	return recoveredObject
}
				unserialize1(obj)
			"""
		) as Any
	}

	fun <T> prepare(obj: T): T {

		return js(
			"""
function Serialize1(object) {

	if (object == null || typeof object != "object") return object

	var constructorName = ""

	try {
		if (typeof eval(object.constructor.name) !== 'undefined') {
			constructorName = object.constructor.name
		} else if (typeof eval("Kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName) !== 'undefined') {
			constructorName = "Kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName

		} /*else if (typeof eval("Kotlin.kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName) !== 'undefined') {
			constructorName = "Kotlin.kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName

		}*/
	} catch (e) {
		console.log(object)
		throw "Can't serialize type " + object.constructor.name
	}

	if (constructorName === "") {
		console.log(object)
		throw "Object constructor not found " + object.constructor.name
	}

	var newObject = {
		"__className": constructorName,
		"data": {}
	}

	for (var i in object) {
		if (typeof object[i] == "function") continue

		newObject.data[i] = Serialize1(object[i])
	}

	if (Array.isArray(object)) {
		newObject.data = Object.values(newObject.data)
	}

	return newObject
}

		return Serialize1(obj)
			"""
		) as T
	}

	class DeserializationException(message: String) : Throwable(message)
}

class TestObject(
	val obj: String = "test string",
	val list: Array<String> = arrayOf("123", "456"),
	val objects: Array<SubObject> = arrayOf(SubObject(), SubObject())
)

class SubObject(
	val string: String = "another test string",
	val int: Int = 123
)