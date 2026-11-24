// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: CODEGEN
// WITH_STDLIB
// DIAGNOSTICS: -FINAL_UPPER_BOUND
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Color(val r: Int)
data class Chain<A : B, B : Color>(val a: A)

@Parcelize
class P(val c: @DataClass Chain<*, *>) : Parcelable
