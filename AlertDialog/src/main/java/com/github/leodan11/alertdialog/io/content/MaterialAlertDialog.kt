package com.github.leodan11.alertdialog.io.content

import android.view.View

class MaterialAlertDialog {

    enum class UI {
        BUTTON_POSITIVE, BUTTON_NEUTRAL, BUTTON_NEGATIVE
    }

    enum class TextAlignment(val alignment: Int) {

        START(View.TEXT_ALIGNMENT_TEXT_START),
        END(View.TEXT_ALIGNMENT_TEXT_END),
        CENTER(View.TEXT_ALIGNMENT_CENTER)

    }

    enum class Type { CIRCULAR, LINEAR }

}