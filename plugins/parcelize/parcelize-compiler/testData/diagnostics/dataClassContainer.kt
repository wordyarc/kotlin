// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: CODEGEN
// WITH_STDLIB
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Color(val r: Int)

@Parcelize
class P(val colors: @DataClass List<Color>) : Parcelable
