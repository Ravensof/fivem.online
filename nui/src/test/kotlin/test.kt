import kotlinx.serialization.cbor.CBOR
import kotlinx.serialization.dumps
import kotlinx.serialization.json.JSON
import kotlinx.serialization.loads
import kotlinx.serialization.protobuf.ProtoBuf
import online.fivem.common.external.Base64
import kotlin.browser.window

fun main() {
	someTests()
}

fun someTests() {
	//serialization test

	val test = CommonTest(175)
	test.test = 16

	val serializer = CommonTest.serializer()

	val serializedString = JSON.indented.stringify(serializer, test)

	println(serializedString)

	println(JSON.parse(serializer, serializedString)::class)

	val protoBuf = ProtoBuf.dumps(serializer, test)

	println("protoBuf = $protoBuf -> ${ProtoBuf.loads(serializer, protoBuf)}")

	val cbor = CBOR.dumps(serializer, test)

	println("cbor = $cbor -> ${CBOR.loads(serializer, cbor)}")

	//test base 64
	println(Base64.encode("test") == window.atob(Base64.encode("test")))
}