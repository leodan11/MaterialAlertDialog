package com.github.leodan11.alertdialog.io.models

import android.view.View.OnClickListener
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class IconInputDialog(
    @DrawableRes val icon: Int,
    val contentDescription: String? = null,
    @StringRes val contentDescriptionRes: Int? = null,
    val listener: OnClickListener? = null,
)