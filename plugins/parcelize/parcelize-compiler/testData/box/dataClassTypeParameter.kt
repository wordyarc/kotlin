// https://issuetracker.google.com/TODO
// WITH_STDLIB
// DIAGNOSTICS: -FINAL_UPPER_BOUND
@file:OptIn(kotlinx.parcelize.Experimental::class)
@file:JvmName("TestKt")
package test

import android.os.Parcelable
import kotlinx.parcelize.*

data class Color(val r: Int)
data class Box<T>(val x: T)
data class BoundedBox<T : Box<String>>(val box: T)

@Parcelize
data class Test<C : Color, N : Color?, B : Box<String>>(
    val color: @DataClass C,
    val nullableColor: @DataClass C?,
    val nullableBound: @DataClass N,
    val boundedBox: @DataClass BoundedBox<B>,
) : Parcelable

fun box() = parcelTest { parcel ->
    val first = Test(Color(1), Color(2), Color(3), BoundedBox(Box("x")))
    val second = Test(Color(4), null, null, BoundedBox(Box("y")))
    first.writeToParcel(parcel, 0)
    second.writeToParcel(parcel, 0)

    val bytes = parcel.marshall()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val first2 = parcelableCreator<Test<*, *, *>>().createFromParcel(parcel)
    val second2 = parcelableCreator<Test<*, *, *>>().createFromParcel(parcel)
    check(first == first2) { "Expected $first, got $first2" }
    check(second == second2) { "Expected $second, got $second2" }
    check(parcel.dataAvail() == 0) { "Parcel has ${parcel.dataAvail()} unread bytes" }
}
