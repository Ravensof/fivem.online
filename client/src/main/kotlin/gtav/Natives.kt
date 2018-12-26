package online.fivem.client.gtav

object Natives {
	fun setTick(callback: () -> Unit) = online.fivem.client.gtav.setTick(callback)

	fun on(eventName: String, callback: Any) = online.fivem.client.gtav.on(eventName, callback)

	fun onNet(eventName: String, callback: Any) = online.fivem.client.gtav.onNet(eventName, callback)

	fun emitNet(eventName: String, data: Any) {
		Exports.emitNet(eventName, data)
	}
}

//external interface IntPtrInitialized
//external interface FloatPtrInitialized
//external interface IntPtr
//external interface FloatPtr
//external interface VectorPtr
//external interface ReturnResultAnyway
//external interface ResultAsInteger
//external interface ResultAsFloat
//external interface ResultAsString
//external interface ResultAsVector
//
//typealias InputArgument = String
//
////string |
////number |
////IntPtrInitialized |
////FloatPtrInitialized |
////IntPtr |
////FloatPtr |
////VectorPtr |
////ReturnResultAnyway |
////ResultAsInteger |
////ResultAsFloat |
////ResultAsString |
////ResultAsVector;
//
//external interface CitizenInterface {
//	fun trace(vararg args: Array<String>)
//	fun setTickFunction(callback: () -> Unit)
//	fun setEventFunction(callback: () -> Unit)
//
//	fun setCallRefFunction(callback: () -> Unit)
//	fun setDeleteRefFunction(callback: () -> Unit)
//	fun setDuplicateRefFunction(callback: () -> Unit)
//	fun canonicalizeRef(ref: Float/*number*/): String
//	fun invokeFunctionReference(ref: String, args: Array<Int>): Array<Int>
//
//	fun getTickCount(): Float//number
//	fun <T> invokeNative(hash: String, vararg args: InputArgument): T
//	fun startProfiling(name: String?)
//	fun stopProfiling(name: String?)
//
//	fun pointerValueIntInitialized(): IntPtrInitialized
//	fun pointerValueFloatInitialized(): FloatPtrInitialized
//	fun pointerValueInt(): IntPtr
//	fun pointerValueFloat(): FloatPtr
//	fun pointerValueVector(): VectorPtr
//	fun returnResultAnyway(): ReturnResultAnyway
//	fun resultAsInteger(): ResultAsInteger
//	fun resultAsFloat(): ResultAsFloat
//	fun resultAsString(): ResultAsString
//	fun resultAsVector(): ResultAsVector
//
//	fun makeRefFunction(refFunction: () -> Unit): String
//}
//
//external val Citizen: CitizenInterface

private external fun on(eventName: String, callback: Any)

private external fun onNet(eventName: String, callback: Any)

private external fun setTick(callback: () -> Unit)