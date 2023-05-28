package com.github.leodan11.alertdialog.dist.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
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

    const val DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN : Int = 400

    /**
     * Get Background Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultBackgroundTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnBackground Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultOnBackgroundTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnBackground, typedValue, true)
        return typedValue.data
    }

    /**
     * Get theme color by resource id.
     *
     * @param context the parent context
     * @param idAttrRes the id attr resource, e.g. [android.R.attr.colorAccent]
     */
    fun getColorDefaultCustomResourceTheme(context: Context, @AttrRes idAttrRes: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(idAttrRes, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Error Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultErrorTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorError, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnError Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultOnErrorTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnError, typedValue, true)
        return typedValue.data
    }

    /**
     * Get ErrorContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultErrorContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorErrorContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnErrorContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnErrorContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnErrorContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Outline Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOutlineTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOutline, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OutlineVariant Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOutlineVariantTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOutlineVariant, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Primary Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultPrimaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnPrimary Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnPrimaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get PrimaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultPrimaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnPrimaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnPrimaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Secondary Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultSecondaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnSecondary Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnSecondaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get SecondaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultSecondaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnSecondaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnSecondaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondaryContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Surface Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultSurfaceTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnSurface Color Default Theme Material Design
     *
     * @param context the parent context
     */
    fun getColorDefaultOnSurfaceTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
        return typedValue.data
    }

    /**
     * Get SurfaceVariant Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultSurfaceVariantTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnSurfaceVariant Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnSurfaceVariantTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, typedValue, true)
        return typedValue.data
    }

    /**
     * Get Tertiary Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultTertiaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnTertiary Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnTertiaryTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary, typedValue, true)
        return typedValue.data
    }

    /**
     * Get TertiaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultTertiaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiaryContainer, typedValue, true)
        return typedValue.data
    }

    /**
     * Get OnTertiaryContainer Color Only Theme Material Design 3
     *
     * @param context the parent context
     */
    fun getColorDefaultOnTertiaryContainerTheme(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiaryContainer, typedValue, true)
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

    fun onTextViewTextSize(view: TextView, textString: String): Rect {
        val bounds = Rect()
        val paint = view.paint
        paint.getTextBounds(textString, 0, textString.length, bounds)
        return bounds
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