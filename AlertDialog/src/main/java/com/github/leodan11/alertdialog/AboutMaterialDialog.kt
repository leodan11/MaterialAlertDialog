package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AboutDialogBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.DetailsAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AboutMaterialDialog private constructor(
    mContext: Context,
    icon: IconAlert,
    iconStore: IconAlert,
    backgroundIconTintColor: IconTintAlert?,
    mCountDownTimer: ButtonCountDownTimer?,
    title: TitleAlert?,
    message: MessageAlert<*>?,
    span: DetailsAlert<*>?,
    details: DetailsAlert<*>?,
    maxLength: Int,
    maxLengthDetails: Int,
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
    countDownTimer = mCountDownTimer,
    title = title,
    message = message,
    span = span,
    details = details,
    maxLength = maxLength,
    maxLengthDetails = maxLengthDetails,
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
                title = title,
                message = message,
                span = span,
                details = details,
                maxLength = maxLength,
                maxLengthDetails = maxLengthDetails,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mIconStore = isIconStore,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}