package com.github.leodan11.alertdialog.io.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

/**
 * Hide input keyboard view
 *
 */
fun Activity.hideInputKeyboardView() {
    val view: View? = this.currentFocus
    view?.clearFocus()
    if (view != null){
        val input = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * Get Background Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultBackgroundTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
    return typedValue.data
}


/**
 * Get OnBackground Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnBackgroundTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnBackground, typedValue, true)
    return typedValue.data
}


/**
 * Get color custom resource theme
 *
 * @param idAttrRes
 * @return [Int] - Color value
 */
fun Context.getColorDefaultCustomResourceTheme(@AttrRes idAttrRes: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(idAttrRes, typedValue, true)
    return typedValue.data
}


/**
 * Get Error Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultErrorTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorError, typedValue, true)
    return typedValue.data
}


/**
 * Get OnError Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnErrorTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnError, typedValue, true)
    return typedValue.data
}


/**
 * Get ErrorContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultErrorContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorErrorContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get OnErrorContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnErrorContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnErrorContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get Outline Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOutlineTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOutline, typedValue, true)
    return typedValue.data
}


/**
 * Get OutlineVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOutlineVariantTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOutlineVariant, typedValue, true)
    return typedValue.data
}


/**
 * Get Primary Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultPrimaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
    return typedValue.data
}


/**
 * Get OnPrimary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnPrimaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
    return typedValue.data
}


/**
 * Get PrimaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultPrimaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get OnPrimaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnPrimaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get Secondary Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultSecondaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSecondary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnSecondaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondary, typedValue, true)
    return typedValue.data
}


/**
 * Get SecondaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultSecondaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSecondaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnSecondaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondaryContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get Surface Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultSurfaceTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSurface Color Default Theme Material Design
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnSurfaceTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
    return typedValue.data
}


/**
 * Get SurfaceVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultSurfaceVariantTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, typedValue, true)
    return typedValue.data
}


/**
 * Get OnSurfaceVariant Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnSurfaceVariantTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, typedValue, true)
    return typedValue.data
}


/**
 * Get Tertiary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultTertiaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiary, typedValue, true)
    return typedValue.data
}


/**
 * Get OnTertiary Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnTertiaryTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary, typedValue, true)
    return typedValue.data
}


/**
 * Get TertiaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultTertiaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiaryContainer, typedValue, true)
    return typedValue.data
}


/**
 * Get OnTertiaryContainer Color Only Theme Material Design 3
 *
 * @return [Int] - Color value
 */
fun Context.getColorDefaultOnTertiaryContainerTheme(): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiaryContainer, typedValue, true)
    return typedValue.data
}

/**
 * On animated vector drawable
 *
 */
fun ImageView.onAnimatedVectorDrawable(){
    val d : Drawable = this.drawable
    if (d is AnimatedVectorDrawableCompat) {
        val avd : AnimatedVectorDrawableCompat = d
        avd.start()
    } else if (d is AnimatedVectorDrawable) {
        val avd : AnimatedVectorDrawable = d
        avd.start()
    }
}