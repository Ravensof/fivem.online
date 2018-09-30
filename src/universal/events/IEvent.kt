package universal.events

abstract class IEvent {

	var __className = this::class.simpleName

	fun serialize(): IEvent {
		return this
	}

	companion object {

		/**
		 * не видит классы, лежащие не в universal
		 */
		fun <A> unserialize(obj: Any): A {

			return js("""
	function unserialize1(object) {
		var args = [];
		var params = [];
		for (var i in object) {
			if (i !== '__className' && typeof object[i] !== 'function') {
				params[params.length] = 'args[' + args.length + ']';
				if (typeof object[i] === 'object' && typeof object[i].__className !== 'undefined') {
					args[args.length] = unserialize1(object[i]);
				} else {
					args[args.length] = '\'' + object[i] + '\'';
				}
			}
		}
		return 'new ' + object.__className + '(' + args.join(',') + ')';
	}

	eval(unserialize1(obj));
			 """)

		}
	}
}

