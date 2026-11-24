// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// MUTE_LL_FIR: StackOverflowError, https://issuetracker.google.com/TODO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class P<X : List<X>>(val x: X) : Parcelable
