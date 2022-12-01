package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.leodan11.alertdialog.dist.base.CircularProgressAlertDialog
import com.github.leodan11.alertdialog.dist.models.*

class MaterialCircularProgressAlertDialog(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean,
    mIndeterminate: Boolean,
    mNegativeButton: ButtonAlertDialog?
): CircularProgressAlertDialog(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    message = message,
    mCancelable = mCancelable,
    mIndeterminate = mIndeterminate,
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
     * Creates a builder for an circular progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context): CircularProgressAlertDialog.Builder<MaterialCircularProgressAlertDialog>(context = context) {

        override fun create(): MaterialCircularProgressAlertDialog {
            return MaterialCircularProgressAlertDialog(
                context,
                icon,
                tintColor,
                title,
                message,
                isCancelable,
                isIndeterminate,
                negativeButton
            )
        }

    }

}