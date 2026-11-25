// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
@file:OptIn(kotlinx.parcelize.Experimental::class)

import android.os.Parcelable
import kotlinx.parcelize.DataClass
import kotlinx.parcelize.Parcelize

data class Color(val r: Int)

@Parcelize
class P(val colors: <!PARCELABLE_TYPE_CONTAINS_NOT_SUPPORTED!>@DataClass List<Color><!>) : Parcelable
