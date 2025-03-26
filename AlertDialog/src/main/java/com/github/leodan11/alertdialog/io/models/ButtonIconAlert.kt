package com.github.leodan11.alertdialog.io.models

import androidx.annotation.DrawableRes
import com.github.leodan11.alertdialog.io.content.Alert

data class ButtonIconAlert(
    @DrawableRes val icon: Int,
    val gravity: Alert.IconGravity = Alert.IconGravity.TEXT_START
)
