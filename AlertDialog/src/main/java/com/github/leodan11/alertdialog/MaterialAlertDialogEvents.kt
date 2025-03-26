package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.EventsComponentBase
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.DetailsAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.SpanLength
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogEvents private constructor(
    mContext: Context,
    icon: IconAlert,
    type: Alert.State,
    backgroundColorSpanInt: Int?,
    backgroundColorSpanResource: Int?,
    mCountDownTimer: ButtonCountDownTimer?,
    messageSpanLengthMax: Int,
    title: TitleAlert?,
    message: MessageAlert<*>?,
    details: DetailsAlert<*>?,
    detailsSpanLengthMax: SpanLength,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : EventsComponentBase(
    mContext = mContext,
    icon = icon,
    type = type,
    backgroundColorSpanInt = backgroundColorSpanInt,
    backgroundColorSpanResource = backgroundColorSpanResource,
    countDownTimer = mCountDownTimer,
    messageSpanLengthMax = messageSpanLengthMax,
    title = title,
    message = message,
    details = details,
    detailsSpanLengthMax = detailsSpanLengthMax,
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
        EventsComponentBase.Builder<MaterialAlertDialogEvents>(context = context) {

        override fun create(): MaterialAlertDialogEvents {
            return MaterialAlertDialogEvents(
                mContext = context,
                icon = icon,
                type = type,
                backgroundColorSpanInt = backgroundColorSpanInt,
                backgroundColorSpanResource = backgroundColorSpan,
                mCountDownTimer = countDownTimer,
                messageSpanLengthMax = messageSpanLengthMax,
                title = title,
                message = message,
                details = details,
                detailsSpanLengthMax = detailsSpanLengthMax,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}