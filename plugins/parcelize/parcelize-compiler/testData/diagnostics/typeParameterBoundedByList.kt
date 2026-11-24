// https://issuetracker.google.com/TODO
// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class P<T : List<String>>(val items: T) : Parcelable
