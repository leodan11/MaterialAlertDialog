package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.IOSComponentBase
import com.github.leodan11.alertdialog.io.content.IOSDialog.Orientation
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class IOSAlertDialog private constructor(
    mContext: Context,
    mOrientationButton: Orientation,
    mTitle: TitleAlert?,
    mMessage: MessageAlert<*>?,
    mCountDownTimer: ButtonCountDownTimer?,
    mCancelable: Boolean,
    mGravity: Int?,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : IOSComponentBase(
    mContext = mContext,
    orientationButton = mOrientationButton,
    title = mTitle,
    message = mMessage,
    countDownTimer = mCountDownTimer,
    mCancelable = mCancelable,
    mPositiveButton = mPositiveButton,
    mNeutralButton = mNeutralButton,
    mNegativeButton = mNegativeButton,
) {

    // Init Dialog
    init {
        val builder = MaterialAlertDialogBuilder(mContext)
        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        val dialogView: View = onCustomCreateView(inflater)
        builder.setView(dialogView)
        // Set Cancelable property
        builder.setCancelable(mCancelable)
        // Create and show dialog
        mDialog = builder.create()
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        IOSComponentBase.Builder<IOSAlertDialog>(context = context) {

        override fun create(): IOSAlertDialog {
            return IOSAlertDialog(
                mContext = context,
                mOrientationButton = orientationButton,
                mTitle = title,
                mMessage = message,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mGravity = gravity,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }

    }

}