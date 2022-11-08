package com.github.leodan11.alertdialog.dist.models

import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.dist.base.source.AlertDialogInterface

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class ButtonAlertDialog(
    val title: String,
    @DrawableRes
    val icon: Int?,
    val onClickListener: AlertDialogInterface.OnClickListener? = null,
    val onChildClickListener: AlertDialogInterface.OnChildClickListenerInput? = null,
    val onClickInvokedCallback: AlertDialogInterface.OnClickInvokedCallback? = null
)