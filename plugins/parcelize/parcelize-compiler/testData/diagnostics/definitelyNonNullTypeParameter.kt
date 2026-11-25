// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class P<T : Parcelable?>(val x: <!PARCELABLE_TYPE_NOT_SUPPORTED!>T & Any<!>) : Parcelable
