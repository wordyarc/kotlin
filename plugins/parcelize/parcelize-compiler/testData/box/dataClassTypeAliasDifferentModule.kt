// https://issuetracker.google.com/TODO
// WITH_STDLIB

// MODULE: lib
// FILE: lib.kt
@file:OptIn(kotlinx.parcelize.Experimental::class)
package test

import kotlinx.parcelize.DataClass

data class Color(val r: Int)

typealias DataColor = @DataClass Color

// MODULE: main(lib)
// FILE: main.kt
@file:JvmName("TestKt")
package test

import android.os.Parcelable
import kotlinx.parcelize.*

@Parcelize
class WithTypeAlias(val color: DataColor) : Parcelable

fun box() = parcelTest { parcel ->
    val test = WithTypeAlias(Color(1))
    test.writeToParcel(parcel, 0)

    val bytes = parcel.marshall()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val test2 = parcelableCreator<WithTypeAlias>().createFromParcel(parcel)
    check(test2.color == test.color) { "Expected ${test.color}, got ${test2.color}" }
    check(parcel.dataAvail() == 0) { "Parcel has ${parcel.dataAvail()} unread bytes" }
}
