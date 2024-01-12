package com.github.leodan11.alertdialog.io.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.io.content.AlertDialogInput.InputType

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class InputAlertDialog(
    var inputType: InputType = InputType.NONE,
    var textHide: String,
    var textHelperRes: Int? = null,
    var textHelper: String? = null,
    var textErrorRes: Int = R.string.text_value_this_field_is_required,
    var textError: String? = null
)