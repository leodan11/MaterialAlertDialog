package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.CenteredComponentBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.RawAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogCentered private constructor(
    mContext: Context,
    icon: IconAlert?,
    image: IconAlert?,
    jsonAnimation: RawAlertDialog?,
    backgroundColorTint: IconTintAlert?,
    mCountDownTimer: ButtonCountDownTimer?,
    title: TitleAlert?,
    message: MessageAlert<*>?,
    mCancelable: Boolean,
    mGravity: Int?,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : CenteredComponentBase(
    mContext = mContext,
    icon = icon,
    bitmap = image,
    jsonAnimation = jsonAnimation,
    iconTint = backgroundColorTint,
    countDownTimer = mCountDownTimer,
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
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        CenteredComponentBase.Builder<MaterialAlertDialogCentered>(context = context) {

        override fun create(): MaterialAlertDialogCentered {
            return MaterialAlertDialogCentered(
                mContext = context,
                icon = icon,
                image = bitmap,
                jsonAnimation = jsonAnimation,
                backgroundColorTint = iconTint,
                title = title,
                message = message,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mGravity = gravity,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}