package com.github.leodan11.alertdialog.dist.helpers

import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
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


    fun getColorDefaultPrimaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }

    fun getColorDefaultBackgroundTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
        return typedValue.data
    }

    fun getColorDefaultOnSurfaceTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
        return typedValue.data
    }

    fun getColorDefaultSurfaceTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        return typedValue.data
    }

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

    fun hideInputKeyboardView(activity: Activity) {
        val view: View? = activity.currentFocus
        view?.clearFocus()
        if (view != null){
            val input = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
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