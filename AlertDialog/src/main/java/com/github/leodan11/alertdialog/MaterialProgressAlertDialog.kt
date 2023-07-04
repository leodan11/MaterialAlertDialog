package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.base.VectorAlertDialog
import com.github.leodan11.alertdialog.dist.models.IconAlertDialog
import com.github.leodan11.alertdialog.dist.models.MessageAlertDialog
import com.github.leodan11.alertdialog.dist.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialProgressAlertDialog(
    mContext: Context,
    icon: IconAlertDialog,
    mAnimatedVectorDrawable: Boolean,
    title: TitleAlertDialog?,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean
): VectorAlertDialog(
    mContext = mContext,
    icon = icon,
    mAnimatedVectorDrawable = mAnimatedVectorDrawable,
    title = title,
    message = message,
    mCancelable = mCancelable
){

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
    class Builder(context: Context): VectorAlertDialog.Builder<MaterialProgressAlertDialog>(context = context) {

        override fun create(): MaterialProgressAlertDialog {
            return MaterialProgressAlertDialog(
                context,
                icon,
                isAnimatedVectorDrawable,
                title,
                message,
                isCancelable
            )
        }

    }

}