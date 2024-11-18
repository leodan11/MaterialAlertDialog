package com.github.leodan11.alertdialog.chroma

import androidx.annotation.ColorInt

interface ColorSelectListener {
    fun onColorSelected(@ColorInt color: Int)
}