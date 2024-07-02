package com.github.leodan11.alertdialog.io.models

import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.io.content.AlertDialog

data class InputCodeExtra(
    var inputType: AlertDialog.Input = AlertDialog.Input.NONE,
    var textHide: String,
    var textHelperRes: Int? = null,
    var textHelper: String? = null,
    var textDefaultValue: String? = null,
    var textErrorRes: Int = R.string.text_value_this_field_is_required,
    var textError: String? = null,
)