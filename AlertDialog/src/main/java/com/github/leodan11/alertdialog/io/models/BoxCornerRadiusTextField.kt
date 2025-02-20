package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class BoxCornerRadiusTextField(
    var topStart: Float = 0f,
    var topEnd: Float = 0f,
    var bottomStart: Float = 0f,
    var bottomEnd: Float = 0f
)
