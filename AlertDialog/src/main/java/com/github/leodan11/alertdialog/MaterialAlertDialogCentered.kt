package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogCenteredBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.RawAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogCentered(
    mContext: Context,
    icon: IconAlertDialog?,
    image: IconAlertDialog?,
    jsonAnimation: RawAlertDialog?,
    backgroundColorInt: Int?,
    backgroundColorResource: Int?,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogCenteredBase(
    mContext = mContext,
    icon = icon,
    bitmap = image,
    jsonAnimation = jsonAnimation,
    backgroundColorInt = backgroundColorInt,
    backgroundColorResource = backgroundColorResource,
    title = title,
    message = message,
    mCancelable = mCancelable,
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
        AlertDialogCenteredBase.Builder<MaterialAlertDialogCentered>(context = context) {

        override fun create(): MaterialAlertDialogCentered {
            return MaterialAlertDialogCentered(
                mContext = context,
                icon = icon,
                image = bitmap,
                jsonAnimation = jsonAnimation,
                backgroundColorInt = backgroundColorInt,
                backgroundColorResource = backgroundColorResource,
                title = title,
                message = message,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}