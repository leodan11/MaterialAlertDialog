package com.github.leodan11.alertdialog.dist.models

import androidx.annotation.RestrictTo
import com.github.leodan11.alertdialog.R

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class InputAlertDialog(
    var inputType: Int,
    var textHide: String,
    var textHelperRes: Int? = null,
    var textHelper: String? = null,
    var textErrorRes: Int = R.string.text_value_this_field_is_required,
    var textError: String? = null
)