// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// MUTE_LL_FIR: StackOverflowError, https://issuetracker.google.com/TODO
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Box<T>(val x: T?)

@Parcelize
class P<X : Box<X>>(val x: @DataClass X) : Parcelable
