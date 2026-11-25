// https://issuetracker.google.com/TODO
// WITH_STDLIB

// MODULE: lib
// FILE: lib.kt
@file:OptIn(kotlinx.parcelize.Experimental::class)
package test

import android.os.Parcelable
import kotlinx.parcelize.*

data class Color(val r: Int)

@Parcelize
open class Base(val color: @DataClass Color) : Parcelable

// MODULE: main(lib)
// FILE: main.kt
@file:JvmName("TestKt")
package test

import android.os.Parcelable
import kotlinx.parcelize.*

@Parcelize
class Derived(val x: Int, color: Color) : Base(color)

fun box() = parcelTest { parcel ->
    val test = Derived(1, Color(2))
    test.writeToParcel(parcel, 0)

    val bytes = parcel.marshall()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val test2 = parcelableCreator<Derived>().createFromParcel(parcel)
    check(test2.x == test.x) { "Expected ${test.x}, got ${test2.x}" }
    check(test2.color == test.color) { "Expected ${test.color}, got ${test2.color}" }
    check(parcel.dataAvail() == 0) { "Parcel has ${parcel.dataAvail()} unread bytes" }
}
