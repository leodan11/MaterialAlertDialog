package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.leodan11.alertdialog.dist.base.CodeAlertDialog
import com.github.leodan11.alertdialog.dist.models.*

class MaterialCodeAlertDialog(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean,
    mInputsContentValue: List<InputAlertDialog>,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?
): CodeAlertDialog(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    message = message,
    mCancelable = mCancelable,
    mInputsContentValue = mInputsContentValue,
    mPositiveButton = mPositiveButton,
    mNegativeButton = mNegativeButton
) {

    // Init Dialog
    init {
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        val dialogView: View = createView(inflater)
        builder.setView(dialogView)
        // Set Cancelable property
        builder.setCancelable(mCancelable)
        // Create and show dialog
        mDialog = builder.create()
    }

    /**
     * Creates a builder for an code alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context): CodeAlertDialog.Builder<MaterialCodeAlertDialog>(context = context) {

        override fun create(): MaterialCodeAlertDialog {
            return MaterialCodeAlertDialog(
                context,
                icon,
                tintColor,
                title,
                message,
                isCancelable,
                mInputsContentValue,
                positiveButton,
                negativeButton
            )
        }

    }

}