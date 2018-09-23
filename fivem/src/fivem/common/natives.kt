package fivem.common

import MODULE_FOLDER_NAME


external interface IntPtrInitialized
external interface FloatPtrInitialized
external interface IntPtr
external interface FloatPtr
external interface VectorPtr
external interface ReturnResultAnyway
external interface ResultAsInteger
external interface ResultAsFloat
external interface ResultAsString
external interface ResultAsVector

typealias InputArgument = String

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

external interface CitizenInterface {
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

external val Citizen: CitizenInterface

private external fun addRawEventListener(eventName: String, callback: (Any) -> Unit)
private external fun addEventListener(eventName: String, callback: (Any) -> Unit, netSafe: Boolean?)

external fun on(eventName: String, callback: Any)

//@Deprecated("use on(Events, ...")
//external fun AddEventHandler(eventName: String, callback: (Any) -> Unit)
//
//@Deprecated("use onNet(Events, ...")
//external fun addNetEventListener(eventName: String, callback: (Any) -> Unit)

external fun onNet(eventName: String, callback: Any)

external fun emit(eventName: String, vararg args: Any)
//@Deprecated("use emit(Events, ...")
//external fun TriggerEvent(eventName: String, eventArgument: Any)

private external fun emitNet(eventName: String, vararg args: Any)//не работает
//@Deprecated("use emitNet(Events, ...")
//external fun TriggerServerEvent(eventName: String, eventArgument: Any)

external fun emitNet(eventName: String, target: String, vararg args: Any)
//@Deprecated("use emitNet(Events, ...")
//external fun TriggerClientEvent(eventName: String, target: String, eventArgument: Any)

private external fun removeEventListener(eventName: String, callback: (eventArgument: Any) -> Unit)

external fun setTick(callback: () -> Unit)

external val exports: dynamic

inline val Exports: dynamic
	get() {
		return exports[MODULE_FOLDER_NAME]
	}