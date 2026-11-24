// https://issuetracker.google.com/TODO
// WITH_STDLIB
// DIAGNOSTICS: -FINAL_UPPER_BOUND
@file:OptIn(kotlinx.parcelize.Experimental::class)
@file:JvmName("TestKt")
package test

import android.os.Parcelable
import kotlinx.parcelize.*

data class Color(val r: Int)
data class Chain<A : B, B : Color>(val a: A, val b: B)
data class Nested<T : List<U>, U : Color>(val items: T)
data class SelfBounded<T : Comparable<T>>(val x: T)

@Parcelize
data class Test(
    val chain: @DataClass Chain<*, *>,
    val nested: @DataClass Nested<*, *>,
    val selfBounded: @RawValue @DataClass SelfBounded<*>,
) : Parcelable

fun box() = parcelTest { parcel ->
    val test = Test(Chain(Color(1), Color(2)), Nested(listOf(Color(3), Color(4))), SelfBounded("x"))
    test.writeToParcel(parcel, 0)

    val bytes = parcel.marshall()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val test2 = parcelableCreator<Test>().createFromParcel(parcel)
    check(test == test2) { "Expected $test, got $test2" }
    check(parcel.dataAvail() == 0) { "Parcel has ${parcel.dataAvail()} unread bytes" }
}
