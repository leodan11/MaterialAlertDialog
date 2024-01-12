package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogInputBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconInputDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.InputAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogInput(
    mContext: Context,
    mIcon: IconAlertDialog?,
    mTintColor: IconTintAlertDialog?,
    mTitle: TitleAlertDialog?,
    mMessage: MessageAlertDialog<*>?,
    mCounterMaxLength: Int?,
    mInputBase: InputAlertDialog,
    mStartIcon: IconInputDialog?,
    mEndIcon: IconInputDialog?,
    mCancelable: Boolean,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogInputBase(
    mContext = mContext,
    icon = mIcon,
    iconTintColor = mTintColor,
    title = mTitle,
    message = mMessage,
    counterMax = mCounterMaxLength,
    inputBase = mInputBase,
    startIcon = mStartIcon,
    endIcon = mEndIcon,
    isCancelable = mCancelable,
    positiveButton = mPositiveButton,
    negativeButton = mNegativeButton
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
     * Creates a builder for a progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogInputBase.Builder<MaterialAlertDialogInput>(context = context) {

        override fun create(): MaterialAlertDialogInput {
            return MaterialAlertDialogInput(
                context,
                icon,
                iconTintColor,
                title,
                message,
                counterMaxLength,
                inputBase,
                startIcon,
                endIcon,
                isCancelable,
                positiveButton,
                negativeButton
            )
        }

    }

}