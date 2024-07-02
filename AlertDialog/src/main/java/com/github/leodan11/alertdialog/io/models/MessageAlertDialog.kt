package com.github.leodan11.alertdialog.io.models

import android.text.Spanned
import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.AlertDialog

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class MessageAlertDialog<T: CharSequence>(val textAlignment: AlertDialog.TextAlignment){

    companion object{
        @JvmStatic
        fun spanned(text: Spanned, alignment: AlertDialog.TextAlignment) = SpannedMessage(text, alignment)

        @JvmStatic
        fun text(text: String, alignment: AlertDialog.TextAlignment) = TextMessage(text, alignment)
    }

    abstract fun getText(): T

    class SpannedMessage(private val text: Spanned, textAlignment: AlertDialog.TextAlignment) : MessageAlertDialog<Spanned>(textAlignment) {

        override fun getText(): Spanned {
            return text
        }

    }

    class TextMessage(private val text: String, textAlignment: AlertDialog.TextAlignment): MessageAlertDialog<String>(textAlignment) {

        override fun getText(): String {
            return text
        }

    }

}