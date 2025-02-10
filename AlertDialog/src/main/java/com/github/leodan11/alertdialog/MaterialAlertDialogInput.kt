package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogInputBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconInputDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogInput private constructor(
    mContext: Context,
    mIcon: IconAlertDialog?,
    mTintColor: IconTintAlertDialog?,
    mTitle: TitleAlertDialog?,
    mMessage: MessageAlertDialog<*>?,
    maskedFormatter: String?,
    mCountDownTimer: ButtonCountDownTimer?,
    mCounterMaxLength: Int?,
    mStartIcon: IconInputDialog?,
    mEndIcon: IconInputDialog?,
    inputTextHide: String,
    inputTextHelper: String?,
    inputTextError: String?,
    inputTextDefault: String?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogInputBase(
    mContext = mContext,
    icon = mIcon,
    iconTintColor = mTintColor,
    title = mTitle,
    message = mMessage,
    maskedFormatter = maskedFormatter,
    counterMax = mCounterMaxLength,
    countDownTimer = mCountDownTimer,
    startIcon = mStartIcon,
    endIcon = mEndIcon,
    inputTextHide = inputTextHide,
    inputTextHelper = inputTextHelper,
    inputTextError = inputTextError,
    inputTextDefault = inputTextDefault,
    isCancelable = mCancelable,
    positiveButton = mPositiveButton,
    negativeButton = mNegativeButton
) {

    // Init Dialog
    init {
        val builder = MaterialAlertDialogBuilder(mContext)
        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        val dialogView: View = createView(inflater)
        builder.setView(dialogView)
        // Set Cancelable property
        builder.setCancelable(mCancelable)
        // Create and show dialog
        mDialog = builder.create()
    }

    /**
     * Creates a builder for a progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogInputBase.Builder<MaterialAlertDialogInput>(context = context) {

        override fun create(): MaterialAlertDialogInput {
            return MaterialAlertDialogInput(
                mContext = context,
                mIcon = icon,
                mTintColor = iconTintColor,
                mTitle = title,
                mMessage = message,
                maskedFormatter = maskedFormatter,
                mCountDownTimer = countDownTimer,
                mCounterMaxLength = counterMaxLength,
                mStartIcon = startIcon,
                mEndIcon = endIcon,
                inputTextHide = inputTextHide,
                inputTextHelper = inputTextHelper,
                inputTextError = inputTextError,
                inputTextDefault = inputTextDefault,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNegativeButton = negativeButton
            )
        }

    }

}