package com.github.leodan11.alertdialog.dist.helpers

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object AlertDialog {

    const val DIALOG_STYLE_CUSTOM: Int = 1111
    const val DIALOG_STYLE_ERROR: Int = 1234
    const val DIALOG_STYLE_HELP: Int = 3421
    const val DIALOG_STYLE_INFORMATION: Int = 1241
    const val DIALOG_STYLE_SUCCESS: Int = 2135
    const val DIALOG_STYLE_WARNING: Int = 4552

    const val NOT_ICON: Int = 321

    const val BUTTON_POSITIVE: Int = 111
    const val BUTTON_NEUTRAL: Int = 222
    const val BUTTON_NEGATIVE: Int = 333

    const val INPUT_TYPE_PERCENTAGE: Int = 9433
    const val INPUT_TYPE_DECIMAL_NUMBER: Int = 5234

    fun onAnimatedVectorDrawable(view: ImageView){
        val d : Drawable = view.drawable
        if (d is AnimatedVectorDrawableCompat) {
            val avd : AnimatedVectorDrawableCompat = d
            avd.start()
        } else if (d is AnimatedVectorDrawable) {
            val avd : AnimatedVectorDrawable = d
            avd.start()
        }
    }

    fun onCallbackRequestFocus(editTextOrigen: EditText, editTextDestiny: EditText, lengthCounter: Int = 1){
        editTextOrigen.addTextChangedListener{
            if (it?.length == lengthCounter) editTextDestiny.requestFocus()
        }
    }

    fun onValidateTextField(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, error: String): Boolean{
        if (TextUtils.isEmpty(textInputEditText.text.toString().trim())){
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = error
            return false
        }else textInputLayout.isErrorEnabled = false
        return true
    }

}