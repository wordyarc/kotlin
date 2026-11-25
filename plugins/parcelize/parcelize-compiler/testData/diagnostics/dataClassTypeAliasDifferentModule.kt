// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// MUTE_LL_FIR: The Analysis API test analyzes `lib` from sources, where `@DataClass` is not lost, https://issuetracker.google.com/TODO

// MODULE: lib
// FILE: lib.kt
@file:OptIn(kotlinx.parcelize.Experimental::class)

import kotlinx.parcelize.DataClass

data class Color(val r: Int)

typealias DataColor = @DataClass Color

// MODULE: main(lib)
// FILE: main.kt
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class P(val c: <!PARCELABLE_TYPE_NOT_SUPPORTED!>DataColor<!>) : Parcelable
