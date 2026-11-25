// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: CODEGEN
// WITH_STDLIB

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
class P(val c: DataColor) : Parcelable
