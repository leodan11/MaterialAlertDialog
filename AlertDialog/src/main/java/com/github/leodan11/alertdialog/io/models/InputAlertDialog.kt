package com.github.leodan11.alertdialog.io.models

import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog

data class InputAlertDialog(
    var inputType: MaterialAlertDialog.InputType = MaterialAlertDialog.InputType.NONE,
    var textHide: String,
    var textHelperRes: Int? = null,
    var textHelper: String? = null,
    var textDefaultValue: String? = null,
    var textErrorRes: Int = R.string.text_value_this_field_is_required,
    var textError: String? = null,
)