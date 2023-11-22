package com.github.leodan11.alertdialog.io.models

import android.text.Spanned
import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class MessageAlertDialog<T: CharSequence>(val textAlignment: MaterialAlertDialog.TextAlignment){

    companion object{
        @JvmStatic
        fun spanned(text: Spanned, alignment: MaterialAlertDialog.TextAlignment) = SpannedMessage(text, alignment)

        @JvmStatic
        fun text(text: String, alignment: MaterialAlertDialog.TextAlignment) = TextMessage(text, alignment)
    }

    abstract fun getText(): T

    class SpannedMessage(private val text: Spanned, textAlignment: MaterialAlertDialog.TextAlignment) : MessageAlertDialog<Spanned>(textAlignment) {

        override fun getText(): Spanned {
            return text
        }

    }

    class TextMessage(private val text: String, textAlignment: MaterialAlertDialog.TextAlignment): MessageAlertDialog<String>(textAlignment) {

        override fun getText(): String {
            return text
        }

    }

}