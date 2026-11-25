// https://issuetracker.google.com/TODO
// WITH_STDLIB
@file:OptIn(kotlinx.parcelize.Experimental::class)
@file:JvmName("TestKt")
package test

import android.os.Parcelable
import kotlinx.parcelize.*
import java.io.Serializable

data class Color(val r: Int)

// Data classes in a `@DataClass` container are serialized property by property, even if they are `Serializable`,
// so the transient property is not lost.
data class Inner(val x: String, @Transient val transient: String?) : Serializable

@Parcelize
class Test(
    val list: @DataClass List<Color>,
    val nullableList: @DataClass List<Color?>?,
    val nested: @DataClass List<List<Color>>,
    val map: @DataClass Map<String, Color>,
    val array: @DataClass Array<Color>,
    val serializable: @DataClass List<Inner>,
) : Parcelable

fun box() = parcelTest { parcel ->
    val test = Test(
        list = listOf(Color(1)),
        nullableList = listOf(Color(2), null),
        nested = listOf(listOf(Color(3))),
        map = mapOf("a" to Color(4)),
        array = arrayOf(Color(5)),
        serializable = listOf(Inner("x", "transient")),
    )
    test.writeToParcel(parcel, 0)

    val bytes = parcel.marshall()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val test2 = parcelableCreator<Test>().createFromParcel(parcel)
    check(test2.list == test.list) { "Expected ${test.list}, got ${test2.list}" }
    check(test2.nullableList == test.nullableList) { "Expected ${test.nullableList}, got ${test2.nullableList}" }
    check(test2.nested == test.nested) { "Expected ${test.nested}, got ${test2.nested}" }
    check(test2.map == test.map) { "Expected ${test.map}, got ${test2.map}" }
    check(test2.array.contentEquals(test.array)) { "Expected ${test.array.toList()}, got ${test2.array.toList()}" }
    check(test2.serializable == test.serializable) { "Expected ${test.serializable}, got ${test2.serializable}" }
    check(parcel.dataAvail() == 0) { "Parcel has ${parcel.dataAvail()} unread bytes" }
}
