package com.github.leodan11.alertdialog.io.helpers

import android.graphics.Rect
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object Functions {


    /**
     * Move focus from a current element to next
     *
     * @param editTextOrigen Current item
     * @param editTextDestiny Element that will receive the focus
     * @param lengthCounter Number of characters to pass focus
     */
    fun onCallbackRequestFocus(
        editTextOrigen: EditText,
        editTextDestiny: EditText,
        lengthCounter: Int = 1,
    ) {
        editTextOrigen.addTextChangedListener {
            if (it?.length == lengthCounter) editTextDestiny.requestFocus()
        }
    }


    /**
     * Validate Text field has data
     *
     * @param textInputLayout Parent element [TextInputLayout].
     * @param textInputEditText Text field [TextInputEditText].
     * @param error Error to be displayed on the element.
     * @return [Boolean] true or false.
     */
    fun onValidateTextField(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        error: String,
    ): Boolean {
        if (TextUtils.isEmpty(textInputEditText.text.toString().trim())) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = error
            return false
        } else textInputLayout.isErrorEnabled = false
        return true
    }


    /**
     * Get the size of the rectangle based on the text string
     *
     * @param view Text view where you will set the rectangle
     * @param textString Text string
     * @return [Rect] Holds four integer coordinates for a rectangle.
     */
    fun onTextViewTextSize(view: TextView, textString: String): Rect {
        val bounds = Rect()
        val paint = view.paint
        paint.getTextBounds(textString, 0, textString.length, bounds)
        return bounds
    }

}