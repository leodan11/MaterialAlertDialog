package com.github.leodan11.alertdialog.io.models

import com.github.leodan11.alertdialog.io.content.AlertDialog

data class ButtonCountDownTimer(
    val button: AlertDialog.UI,
    val millis: Long,
    val countInterval: Long = 1000L,
    val format: String = "%s (%d)"
)