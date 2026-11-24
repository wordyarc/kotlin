// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// RENDER_DIAGNOSTICS_FULL_TEXT
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Box<T>(val x: T?)

@Parcelize
class P<X : Box<X>>(val x: <!PARCELABLE_RECURSIVE_DATA_CLASS_NOT_SUPPORTED!>@DataClass X<!>) : Parcelable

data class A<T : Box<T>>(val x: T?)
data class ListBox<T>(val items: List<T>)

@Parcelize
class Q<Y : ListBox<Y>, Z : List<@DataClass Box<Z>>>(
    val a: <!PARCELABLE_RECURSIVE_DATA_CLASS_NOT_SUPPORTED!>@DataClass A<*><!>,
    val y: <!PARCELABLE_RECURSIVE_DATA_CLASS_NOT_SUPPORTED!>@DataClass Y<!>,
    val z: <!PARCELABLE_RECURSIVE_DATA_CLASS_NOT_SUPPORTED!>Z<!>,
) : Parcelable

// Not recursive
@Parcelize
class R<W : Box<Int>>(
    val w: @DataClass W,
    val boxedW: @DataClass Box<W>,
) : Parcelable
