package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AlertDialogProgressSmallBase
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogProgressSmall(
    mContext: Context,
    icon: IconAlertDialog,
    mAnimatedVectorDrawable: Boolean,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean,
) : AlertDialogProgressSmallBase(
    mContext = mContext,
    icon = icon,
    mAnimatedVectorDrawable = mAnimatedVectorDrawable,
    message = message,
    mCancelable = mCancelable
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
     * Creates a builder for an progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AlertDialogProgressSmallBase.Builder<MaterialAlertDialogProgressSmall>(context = context) {

        override fun create(): MaterialAlertDialogProgressSmall {
            return MaterialAlertDialogProgressSmall(
                context,
                icon,
                isAnimatedVectorDrawable,
                message,
                isCancelable
            )
        }

    }

}