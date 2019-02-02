package online.fivem.client.gtav

object Natives {
	fun setTick(callback: () -> Unit) = online.fivem.client.gtav.setTick(callback)

	fun on(eventName: String, callback: Any) = online.fivem.client.gtav.on(eventName, callback)

	fun onNet(eventName: String, callback: Any) = online.fivem.client.gtav.onNet(eventName, callback)

	fun emitNet(eventName: String, data: Any) {
		Exports.emitNet(eventName, data)
	}

	fun <T> invokeNative(hex: String, vararg args: InputArgument): T {
		return Citizen.invokeNative(hex, *args)
	}

	// the callback will be called next game tick
	fun mainThread(callback: () -> Unit) {
		setImmediate(callback)
	}
}

private external interface IntPtrInitialized
private external interface FloatPtrInitialized
private external interface IntPtr
private external interface FloatPtr
private external interface VectorPtr
private external interface ReturnResultAnyway
private external interface ResultAsInteger
private external interface ResultAsFloat
private external interface ResultAsString
private external interface ResultAsVector

private typealias InputArgument = Any

//string |
//number |
//IntPtrInitialized |
//FloatPtrInitialized |
//IntPtr |
//FloatPtr |
//VectorPtr |
//ReturnResultAnyway |
//ResultAsInteger |
//ResultAsFloat |
//ResultAsString |
//ResultAsVector;

private external interface CitizenInterface {
	fun trace(vararg args: Array<String>)
	fun setTickFunction(callback: () -> Unit)
	fun setEventFunction(callback: () -> Unit)

	fun setCallRefFunction(callback: () -> Unit)
	fun setDeleteRefFunction(callback: () -> Unit)
	fun setDuplicateRefFunction(callback: () -> Unit)
	fun canonicalizeRef(ref: Float/*number*/): String
	fun invokeFunctionReference(ref: String, args: Array<Int>): Array<Int>

	fun getTickCount(): Float//number
	fun <T> invokeNative(hash: String, vararg args: InputArgument): T
	fun startProfiling(name: String?)
	fun stopProfiling(name: String?)

	fun pointerValueIntInitialized(): IntPtrInitialized
	fun pointerValueFloatInitialized(): FloatPtrInitialized
	fun pointerValueInt(): IntPtr
	fun pointerValueFloat(): FloatPtr
	fun pointerValueVector(): VectorPtr
	fun returnResultAnyway(): ReturnResultAnyway
	fun resultAsInteger(): ResultAsInteger
	fun resultAsFloat(): ResultAsFloat
	fun resultAsString(): ResultAsString
	fun resultAsVector(): ResultAsVector

	fun makeRefFunction(refFunction: () -> Unit): String
}

private external val Citizen: CitizenInterface

private external fun on(eventName: String, callback: Any)

private external fun onNet(eventName: String, callback: Any)

private external fun setTick(callback: () -> Unit)

private external fun setImmediate(callback: () -> Unit)