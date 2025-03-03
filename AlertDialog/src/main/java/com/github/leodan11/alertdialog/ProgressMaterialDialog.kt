package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.ProgressDialogBase
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProgressMaterialDialog private constructor(
    mContext: Context,
    icon: IconAlert?,
    tintColor: IconTintAlert?,
    progressType: AlertDialog.Progress,
    title: TitleAlert?,
    message: MessageAlert<*>?,
    detailsLinearProgress: MessageAlert<*>?,
    mCancelable: Boolean,
    mIndeterminate: Boolean,
    mMax: Int,
    mNegativeButton: ButtonAlertDialog?,
) : ProgressDialogBase(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    progressType = progressType,
    title = title,
    message = message,
    detailsLinearProgress = detailsLinearProgress,
    mMax = mMax,
    mCancelable = mCancelable,
    mIndeterminate = mIndeterminate,
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
     * Creates a builder for a circular progress alert dialog
     * that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     *
     * @param context the parent context
     *
     */
    class Builder(context: Context) :
        ProgressDialogBase.Builder<ProgressMaterialDialog>(context) {

        override fun create(): ProgressMaterialDialog {
            return ProgressMaterialDialog(
                mContext = context,
                icon = icon,
                tintColor = iconTint,
                progressType = progressType,
                title = title,
                message = message,
                detailsLinearProgress = detailsLinearProgress,
                mCancelable = isCancelable,
                mIndeterminate = isIndeterminate,
                mMax = max,
                mNegativeButton = negativeButton
            )
        }

    }

}