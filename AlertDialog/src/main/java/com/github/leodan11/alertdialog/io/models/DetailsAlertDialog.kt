package com.github.leodan11.alertdialog.io.models

import android.text.Spanned
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class DetailsAlertDialog<T: CharSequence>{

    companion object{
        @JvmStatic
        fun spanned(text: Spanned) = SpannedDetails(text)

        @JvmStatic
        fun text(text: String) = TextDetails(text)
    }

    abstract fun getText(): T

    class SpannedDetails(private val text: Spanned) : DetailsAlertDialog<Spanned>() {

        override fun getText(): Spanned {
            return text
        }

    }

    class TextDetails(private val text: String): DetailsAlertDialog<String>() {

        override fun getText(): String {
            return text
        }

    }

}
