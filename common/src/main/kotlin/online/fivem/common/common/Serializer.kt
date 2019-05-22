package online.fivem.common.common

import online.fivem.common.other.SerializerInterface
import kotlin.reflect.KClass

internal class Serializer : SerializerInterface {

	override fun getSerializerHash(kClass: KClass<*>): Int = -1

	override fun <T : Any> serialize(obj: T): String {
		return JSON.stringify(prepare(obj))
	}

	override fun deserialize(string: String, serializerId: Int): Any {
		return deserialize(string)
	}

	inline fun <reified R> deserialize(jsonString: String): R {
		return unpack(JSON.parse(jsonString))
	}

	inline fun <reified R> unpack(obj: Any): R {
		val unpacked = uncheckedUnpack(obj)

		if (unpacked !is R) {
			throw SerializerInterface.DeserializationException("can't deserialize object ${unpacked::class.simpleName} as ${R::class.simpleName}")
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
function checkConstructor(string) {
	try {
		if (typeof eval(string) !== "undefined") return true
	} catch (e) {

	}
	return false
}

function Serialize1(object) {

	if (object == null || typeof object != "object") return object

	var constructorName = ""

	if (checkConstructor(object.constructor.name)) {
		constructorName = object.constructor.name

	} else if (checkConstructor("Kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName)) {
		constructorName = "Kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName

	} else if (checkConstructor("Kotlin.kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName)) {
		constructorName = "Kotlin.kotlin." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName
	} else if (checkConstructor("Kotlin.kotlin.collections." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName)) {
		constructorName = "Kotlin.kotlin.collections." + object.__proto__.constructor.${'$'}metadata${'$'}.simpleName
	}

	if (constructorName === "") {
		console.log(object)
		throw "Can't serialize type " + object.constructor.name
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
}