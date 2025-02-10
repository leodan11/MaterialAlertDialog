package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogVerificationCodeBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogVerificationCode private constructor(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mNeedReason: Boolean,
    mCountDownTimer: ButtonCountDownTimer?,
    mCancelable: Boolean,
    mInputsContentValue: List<InputCodeExtra>,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogVerificationCodeBase(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    message = message,
    mNeedReason = mNeedReason,
    countDownTimer = mCountDownTimer,
    mCancelable = mCancelable,
    mInputsContentValue = mInputsContentValue,
    mPositiveButton = mPositiveButton,
    mNegativeButton = mNegativeButton
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
     * Creates a builder for a code alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogVerificationCodeBase.Builder<MaterialAlertDialogVerificationCode>(context = context) {

        override fun create(): MaterialAlertDialogVerificationCode {
            return MaterialAlertDialogVerificationCode(
                mContext = context,
                icon = icon,
                tintColor = tintColor,
                title = title,
                message = message,
                mNeedReason = isNeedReason,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mInputsContentValue = mInputsContentValue,
                mPositiveButton = positiveButton,
                mNegativeButton = negativeButton
            )
        }

    }

}