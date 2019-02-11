import external.danko.Base64
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.dumps
import kotlinx.serialization.json.Json
import kotlinx.serialization.loads
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.browser.window

fun main() {
	someTests()
}

fun someTests() {
	//serialization test

	val test = CommonTest(175)
	test.test = 16

	val serializer = CommonTest.serializer()

	val serializedString = Json.indented.stringify(serializer, test)

	println(serializedString)

	println(Json.parse(serializer, serializedString)::class)

	val protoBuf = ProtoBuf.dumps(serializer, test)

	println("protoBuf = $protoBuf -> ${ProtoBuf.loads(serializer, protoBuf)}")

	val cbor = Cbor.dumps(serializer, test)

	println("cbor = $cbor -> ${Cbor.loads(serializer, cbor)}")

	//test base 64
	println(Base64.encode("test") == window.atob(Base64.encode("test")))
}