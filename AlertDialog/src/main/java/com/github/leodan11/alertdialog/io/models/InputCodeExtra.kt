package com.github.leodan11.alertdialog.io.models

import android.text.InputType
import com.github.leodan11.alertdialog.R

data class InputCodeExtra(
    var textHide: String,
    var inputType: Int = InputType.TYPE_CLASS_TEXT,
    var textHelperRes: Int? = null,
    var textHelper: String? = null,
    var textDefaultValue: String? = null,
    var textErrorRes: Int = R.string.label_text_this_field_is_required,
    var textError: String? = null,
    var enabled: Boolean = true
)