package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.MaterialAlert

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class ButtonAlertDialog(
    val title: String,
    val iconAlert: ButtonIconAlert,
    val onClickListener: MaterialAlert.OnClickListener? = null,
    val onClickInputListener: MaterialAlert.OnClickInputListener? = null,
    val onClickVerificationCodeListener: MaterialAlert.OnClickVerificationCodeListener? = null,
    val onClickSignInListener: MaterialAlert.OnClickSignInListener? = null
)