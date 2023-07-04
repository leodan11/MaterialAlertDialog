package com.github.leodan11.alertdialog

import android.content.Context
import com.github.leodan11.alertdialog.dist.base.BaseDialogFragment
import com.github.leodan11.alertdialog.dist.models.IconAlertDialog
import com.github.leodan11.alertdialog.dist.models.MessageAlertDialog

class MaterialProgressSmallDialog(
    mContext: Context,
    icon: IconAlertDialog,
    mAnimatedVectorDrawable: Boolean,
    message: MessageAlertDialog<*>?,
    mCancelable: Boolean
) : BaseDialogFragment(
    mContext = mContext,
    icon = icon,
    mAnimatedVectorDrawable = mAnimatedVectorDrawable,
    message = message,
    mCancelable = mCancelable
) {
    /**
     * Creates a builder for an progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        BaseDialogFragment.Builder<MaterialProgressSmallDialog>(context = context) {

        override fun create(): MaterialProgressSmallDialog {
            return MaterialProgressSmallDialog(
                context,
                icon,
                isAnimatedVectorDrawable,
                message,
                isCancelable
            )
        }

    }

    companion object {
        const val TAG: String = "MaterialProgressSmallAlertDialog::class"
    }

}