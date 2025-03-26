package com.github.leodan11.alertdialog.io.models

import android.text.Spanned
import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.Alert

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class MessageAlert<T : CharSequence>(val textAlignment: Alert.TextAlignment) {

    companion object {
        @JvmStatic
        fun spanned(text: Spanned, alignment: Alert.TextAlignment) = SpannedMessage(text, alignment)

        @JvmStatic
        fun text(text: String, alignment: Alert.TextAlignment) = TextMessage(text, alignment)
    }

    abstract fun getText(): T

    class SpannedMessage(private val text: Spanned, textAlignment: Alert.TextAlignment) :
        MessageAlert<Spanned>(textAlignment) {

        override fun getText(): Spanned {
            return text
        }

    }

    class TextMessage(private val text: String, textAlignment: Alert.TextAlignment) :
        MessageAlert<String>(textAlignment) {

        override fun getText(): String {
            return text
        }

    }

}