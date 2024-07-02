package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogProgressBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogProgress(
    mContext: Context,
    icon: IconAlertDialog?,
    tintColor: IconTintAlertDialog?,
    iconVectorDrawable: IconAlertDialog,
    mAnimatedVectorDrawable: Boolean,
    mAnimatedVectorDrawableLoop: Boolean,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean,
    mNegativeButton: ButtonAlertDialog?,
) : AlertDialogProgressBase(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    iconVectorDrawable = iconVectorDrawable,
    mAnimatedVectorDrawable = mAnimatedVectorDrawable,
    mAnimatedVectorDrawableLoop = mAnimatedVectorDrawableLoop,
    title = title,
    message = message,
    mCancelable = mCancelable,
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
     * Creates a builder for a progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogProgressBase.Builder<MaterialAlertDialogProgress>(context = context) {

        override fun create(): MaterialAlertDialogProgress {
            return MaterialAlertDialogProgress(
                mContext = context,
                icon = icon,
                tintColor = tintColor,
                iconVectorDrawable = iconVectorDrawable,
                mAnimatedVectorDrawable = isAnimatedVectorDrawable,
                mAnimatedVectorDrawableLoop = isAnimatedVectorDrawableLoop,
                title = title,
                message = message,
                mCancelable = isCancelable,
                mNegativeButton = negativeButton
            )
        }

    }

}