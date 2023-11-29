package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogIOSBase
import com.github.leodan11.alertdialog.io.content.IOSDialog.Orientation
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class IOSAlertDialog(
    mContext: Context,
    mOrientationButton: Orientation,
    mTitle: TitleAlertDialog?,
    mMessage: MessageAlertDialog<*>?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?
) : AlertDialogIOSBase(
    mContext = mContext,
    orientationButton = mOrientationButton,
    title = mTitle,
    message = mMessage,
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
        mDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) : AlertDialogIOSBase.Builder<IOSAlertDialog>(context = context) {

        override fun create(): IOSAlertDialog {
            return IOSAlertDialog(
                mContext = context,
                mOrientationButton = orientationButton,
                mTitle = title,
                mMessage = message,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }

    }

}