package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class ButtonAlertDialog(
    val title: String,
    val iconAlert: ButtonIconAlert,
    val onClickListener: DialogAlertInterface.OnClickListener? = null,
    val onClickInputListener: DialogAlertInterface.OnClickInputListener? = null,
    val onClickVerificationCodeListener: DialogAlertInterface.OnClickVerificationCodeListener? = null,
    val onClickSignInListener: DialogAlertInterface.OnClickSignInListener? = null
)