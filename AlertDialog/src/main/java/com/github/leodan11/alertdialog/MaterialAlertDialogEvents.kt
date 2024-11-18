package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogEventsBase
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.DetailsAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogEvents private constructor(
    mContext: Context,
    icon: IconAlertDialog,
    type: AlertDialog.State,
    backgroundColorSpanInt: Int?,
    backgroundColorSpanResource: Int?,
    messageSpanLengthMax: Int,
    detailsScrollHeightSpan: Int,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    details: DetailsAlertDialog<*>?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogEventsBase(
    mContext = mContext,
    icon = icon,
    type = type,
    backgroundColorSpanInt = backgroundColorSpanInt,
    backgroundColorSpanResource = backgroundColorSpanResource,
    messageSpanLengthMax = messageSpanLengthMax,
    detailsScrollHeightSpan = detailsScrollHeightSpan,
    title = title,
    message = message,
    details = details,
    mCancelable = mCancelable,
    mPositiveButton = mPositiveButton,
    mNeutralButton = mNeutralButton,
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
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogEventsBase.Builder<MaterialAlertDialogEvents>(context = context) {

        override fun create(): MaterialAlertDialogEvents {
            return MaterialAlertDialogEvents(
                mContext = context,
                icon = icon,
                type = type,
                backgroundColorSpanInt = backgroundColorSpanInt,
                backgroundColorSpanResource = backgroundColorSpan,
                messageSpanLengthMax = messageSpanLengthMax,
                detailsScrollHeightSpan = detailsScrollHeightSpan,
                title = title,
                message = message,
                details = details,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}