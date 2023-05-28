package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.leodan11.alertdialog.dist.base.AlertDefaultDialog
import com.github.leodan11.alertdialog.dist.models.*

class MaterialAlertDialog(
    mContext: Context,
    icon: IconAlertDialog,
    type: TypeAlertDialog,
    backgroundColorSpan: Int,
    detailsScrollHeightSpan: Int,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    details: DetailsAlertDialog<*>?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?
): AlertDefaultDialog(
    mContext = mContext,
    icon = icon,
    type = type,
    backgroundColorSpan = backgroundColorSpan,
    detailsScrollHeightSpan = detailsScrollHeightSpan,
    title = title,
    message = message,
    details = details,
    mCancelable = mCancelable,
    mPositiveButton = mPositiveButton,
    mNeutralButton = mNeutralButton,
    mNegativeButton = mNegativeButton
){

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
        mDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context): AlertDefaultDialog.Builder<MaterialAlertDialog>(context = context) {

        override fun create(): MaterialAlertDialog {
            return MaterialAlertDialog(
                context,
                icon,
                type,
                backgroundColorSpan,
                detailsScrollHeightSpan,
                title,
                message,
                details,
                isCancelable,
                positiveButton,
                neutralButton,
                negativeButton
            )
        }
    }

}