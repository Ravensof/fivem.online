@file:Suppress(
	"INTERFACE_WITH_SUPERCLASS",
	"OVERRIDING_FINAL_MEMBER",
	"RETURN_TYPE_MISMATCH_ON_OVERRIDE",
	"CONFLICTING_OVERLOADS",
	"EXTERNAL_DELEGATION",
	"NESTED_CLASS_IN_EXTERNAL_INTERFACE"
)

package online.fivem


import org.khronos.webgl.Uint8Array

internal external interface IntPtrInitialized
internal external interface FloatPtrInitialized
internal external interface IntPtr
internal external interface FloatPtr
internal external interface VectorPtr
internal external interface ReturnResultAnyway
internal external interface ResultAsInteger
internal external interface ResultAsFloat
internal external interface ResultAsString
internal external interface ResultAsVector
internal external interface CitizenInterface {
	fun trace(vararg args: String)
	fun setTickFunction(callback: Function<*>)
	fun setEventFunction(callback: Function<*>)
	fun setCallRefFunction(callback: Function<*>)
	fun setDeleteRefFunction(callback: Function<*>)
	fun setDuplicateRefFunction(callback: Function<*>)
	fun canonicalizeRef(ref: Number): String
	fun invokeFunctionReference(ref: String, args: Uint8Array): Uint8Array
	fun getTickCount(): Number
	fun <T> invokeNative(
		hash: String,
		vararg args: dynamic /* String | Number | IntPtrInitialized | FloatPtrInitialized | IntPtr | FloatPtr | VectorPtr | ReturnResultAnyway | ResultAsInteger | ResultAsFloat | ResultAsString | ResultAsVector */
	): T

	fun startProfiling(name: String? = definedExternally /* null */)
	fun stopProfiling(name: String? = definedExternally /* null */): Any
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
	fun makeRefFunction(refFunction: Function<*>): String
}

internal external var Citizen: CitizenInterface = definedExternally
internal external fun addRawEventListener(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun addEventListener(
	eventName: String,
	callback: Function<*>,
	netSafe: Boolean? = definedExternally /* null */
): Unit = definedExternally

internal external fun on(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun AddEventHandler(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun addNetEventListener(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun onNet(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun emit(eventName: String, vararg args: Any): Unit = definedExternally
internal external fun TriggerEvent(eventName: String, vararg args: Any): Unit = definedExternally
internal external fun emitNet(eventName: String, vararg args: Any): Unit = definedExternally
internal external fun TriggerServerEvent(eventName: String, vararg args: Any): Unit = definedExternally
internal external fun emitNet(eventName: String, target: String, vararg args: Any): Unit = definedExternally
internal external fun emitNet(eventName: String, target: Number, vararg args: Any): Unit = definedExternally
internal external fun TriggerClientEvent(eventName: String, target: String, vararg args: Any): Unit = definedExternally
internal external fun TriggerClientEvent(eventName: String, target: Number, vararg args: Any): Unit = definedExternally
internal external fun removeEventListener(eventName: String, callback: Function<*>): Unit = definedExternally
internal external fun setTick(callback: Function<*>): Unit = definedExternally
internal external var exports: Any = definedExternally
