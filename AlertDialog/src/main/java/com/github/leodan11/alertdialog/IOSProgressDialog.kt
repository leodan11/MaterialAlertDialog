package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogProgressIOSBase
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class IOSProgressDialog(
    mContext: Context,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean
) : AlertDialogProgressIOSBase(
    mContext = mContext,
    message = message,
    mCancelable = mCancelable
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
        mDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    /**
     * Creates a builder for a circular progress alert dialog
     * that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogProgressIOSBase.Builder<IOSProgressDialog>(context = context) {

        override fun create(): IOSProgressDialog {
            return IOSProgressDialog(
                context,
                message,
                isCancelable
            )
        }

    }

}