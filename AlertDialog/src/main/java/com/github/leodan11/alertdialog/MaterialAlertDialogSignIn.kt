package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.SignInComponentBase
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogSignIn private constructor(
    mContext: Context,
    icon: IconAlert?,
    tintColor: IconTintAlert?,
    title: TitleAlert?,
    mBoxCornerRadius: BoxCornerRadiusTextField?,
    mCountDownTimer: ButtonCountDownTimer?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : SignInComponentBase(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    boxCornerRadius = mBoxCornerRadius,
    countDownTimer = mCountDownTimer,
    mCancelable = mCancelable,
    mPositiveButton = mPositiveButton,
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
     * Creates a builder for a login alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        SignInComponentBase.Builder<MaterialAlertDialogSignIn>(context = context) {

        override fun create(): MaterialAlertDialogSignIn {
            return MaterialAlertDialogSignIn(
                mContext = context,
                icon = icon,
                tintColor = tintColor,
                title = title,
                mBoxCornerRadius = boxCornerRadius,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mPositiveButton = positiveButton,
                mNegativeButton = negativeButton
            )
        }

    }

}