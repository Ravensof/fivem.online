package universal.common

abstract class Serializable {
	companion object {

		fun serialize(data: Any): String {
			return JSON.stringify(prepare(data))
		}

		fun <T> unserialize(jsonString: String): T {
			return unpack(JSON.parse(jsonString)) as T
		}

		/**
		 * не видит классы, лежащие не в universal
		 */
		fun unpack(obj: Any): Any {

			return js("""
function unserialize1(object) {
	if (object != null && typeof object === 'object') {
		if (typeof object.__className !== 'undefined') {
			function recoveredObject() {
				for (var key in object) {
					this[key] = object[key];
				}

			}

			var className=object.__className

			if(typeof eval("Kotlin.kotlin."+className)!=='undefined'){
				className="Kotlin.kotlin."+className
			}else if(typeof eval(className) !=='undefined'){
				className=className
			}else {
				throw "class "+className+"not found"
			}

			recoveredObject.prototype.__proto__ = eval(className + '.prototype');
			recoveredObject.${'$'}metadata${'$'} = recoveredObject.${'$'}metadata${'$'} = eval(className + '.${'$'}metadata${'$'}');

			object = new recoveredObject();
		}
		for (var key in object) {
			object[key] = unserialize1(object[key]);
		}
	}
	return object;
}
				unserialize1(obj)
			""") as Any
		}

		fun <T> prepare(obj: T): T {

			return js("""
						function Serialize1(obj) {
			if (obj != null && typeof obj === 'object') {
				for (var i in obj) {
					obj[i] = Serialize1(obj[i])
				}
				obj['__className'] = obj.constructor.name;
			}

			return obj
		}

		return Serialize1(obj)
			""") as T
		}
	}
}