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

	val serial = CommonTest.serializer()

	val serialized = JSON.indented.stringify(serial, test)

	println(serialized)

	println(JSON.parse(serial, serialized)::class)

	val protoBuf = ProtoBuf.dumps(serial, test)

	println("protoBuf = $protoBuf -> ${ProtoBuf.loads(serial, protoBuf)}")

	val cbor = CBOR.dumps(serial, test)

	println("cbor = $cbor -> ${CBOR.loads(serial, cbor)}")

	//test base 64
	println(Base64.encode("test") == window.atob(Base64.encode("test")))
}