package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.io.content.AlertDialog

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class TitleAlertDialog(val title: String, val textAlignment: AlertDialog.TextAlignment)