package com.github.leodan11.alertdialog.io.models

import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class ButtonAlertDialog(
    val title: String,
    @DrawableRes
    val icon: Int,
    val onClickListener: MaterialDialogInterface.OnClickListener? = null,
    val onClickInputListener: MaterialDialogInterface.OnClickInputListener? = null,
    val onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener? = null,
    val onClickSignInListener: MaterialDialogInterface.OnClickSignInListener? = null
)