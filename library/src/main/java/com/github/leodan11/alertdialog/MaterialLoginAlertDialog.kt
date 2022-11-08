package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.leodan11.alertdialog.dist.base.LoginAlertDialog
import com.github.leodan11.alertdialog.dist.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.dist.models.IconAlertDialog
import com.github.leodan11.alertdialog.dist.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.dist.models.TitleAlertDialog

class MaterialLoginAlertDialog(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    title: TitleAlertDialog?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?
): LoginAlertDialog(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    mCancelable = mCancelable,
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
     * Creates a builder for an login alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context): LoginAlertDialog.Builder<MaterialLoginAlertDialog>(context = context) {

        override fun create(): MaterialLoginAlertDialog {
            return MaterialLoginAlertDialog(
                context,
                icon,
                tintColor,
                title,
                isCancelable,
                positiveButton,
                negativeButton
            )
        }

    }

}