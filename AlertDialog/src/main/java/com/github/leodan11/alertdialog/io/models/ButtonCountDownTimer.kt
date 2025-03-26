package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class ButtonCountDownTimer(
    val button: DialogAlertInterface.UI,
    val millis: Long,
    val countInterval: Long = 1000L,
    val format: String = "%s (%d)"
)