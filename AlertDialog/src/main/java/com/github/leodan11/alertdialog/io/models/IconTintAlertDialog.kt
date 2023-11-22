package com.github.leodan11.alertdialog.io.models

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class IconTintAlertDialog(
    @ColorRes
    var iconColorRes: Int? = null,
    @ColorInt
    var iconColorInt: Int ? = null
)