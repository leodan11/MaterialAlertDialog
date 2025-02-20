package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogSignInBase
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogSignIn private constructor(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    title: TitleAlertDialog?,
    mBoxCornerRadius: BoxCornerRadiusTextField?,
    mCountDownTimer: ButtonCountDownTimer?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogSignInBase(
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
        AlertDialogSignInBase.Builder<MaterialAlertDialogSignIn>(context = context) {

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