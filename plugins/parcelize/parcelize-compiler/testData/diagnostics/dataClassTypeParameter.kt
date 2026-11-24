// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// DIAGNOSTICS: -FINAL_UPPER_BOUND
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Color(val r: Int)

@Parcelize
class P<T : Color>(val c: @DataClass T) : Parcelable
