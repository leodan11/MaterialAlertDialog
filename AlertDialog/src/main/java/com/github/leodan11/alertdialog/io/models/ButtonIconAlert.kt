package com.github.leodan11.alertdialog.io.models

import androidx.annotation.DrawableRes
import com.github.leodan11.alertdialog.io.content.AlertDialog

data class ButtonIconAlert(
    @DrawableRes val icon: Int,
    val gravity: AlertDialog.IconGravity = AlertDialog.IconGravity.TEXT_START
)
