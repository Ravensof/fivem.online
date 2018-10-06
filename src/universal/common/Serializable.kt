package universal.common

abstract class Serializable {
	var __className = this::class.simpleName

	fun serialize(): Serializable {
		return this
	}

	companion object {
		/**
		 * не видит классы, лежащие не в universal
		 */
		fun <A> unserialize(obj: Any): A {

			return js("""
	  function unserialize1(object) {
		  if (object != null && typeof object === 'object') {
			  if (typeof object.__className !== 'undefined') {
				  function recoveredObject() {
					  for (var key in object) {
						  this[key] = object[key];
					  }

				  }
				  recoveredObject.prototype.__proto__ = eval(object.__className + '.prototype');

				  recoveredObject.${"$"}metadata${"$"} = recoveredObject.${"$"}metadata${"$"} = eval(object.__className + '.${"$"}metadata${'$'}')

				  object = new recoveredObject();
			  }
			  for (var key in object) {
				  object[key] = unserialize1(object[key]);
			  }
		  }
		  return object;
	  }

	unserialize1(obj)
			""") as A

		}
	}
}