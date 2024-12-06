package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AboutDialogBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.DetailsAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AboutMaterialDialog private constructor(
    mContext: Context,
    icon: IconAlertDialog,
    iconStore: IconAlertDialog,
    backgroundIconTintColor: IconTintAlertDialog?,
    detailsScrollHeightSpan: Int,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    span: DetailsAlertDialog<*>?,
    details: DetailsAlertDialog<*>?,
    mCancelable: Boolean,
    mIconStore: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AboutDialogBase(
    mContext = mContext,
    icon = icon,
    iconStore = iconStore,
    backgroundIconTintColor = backgroundIconTintColor,
    detailsScrollHeightSpan = detailsScrollHeightSpan,
    title = title,
    message = message,
    span = span,
    details = details,
    mCancelable = mCancelable,
    mIconStore = mIconStore,
    mPositiveButton = mPositiveButton,
    mNeutralButton = mNeutralButton,
    mNegativeButton = mNegativeButton
) {

    init {
        val builder = MaterialAlertDialogBuilder(mContext)
        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        val dialogView: View = onCreateViewDialogContent(inflater)
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
        AboutDialogBase.Builder<AboutMaterialDialog>(context = context) {

        override fun create(): AboutMaterialDialog {
            return AboutMaterialDialog(
                mContext = context,
                icon = icon,
                iconStore = iconStore,
                backgroundIconTintColor = backgroundIconTintColor,
                detailsScrollHeightSpan = detailsScrollHeightSpan,
                title = title,
                message = message,
                span = span,
                details = details,
                mCancelable = isCancelable,
                mIconStore = isIconStore,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}