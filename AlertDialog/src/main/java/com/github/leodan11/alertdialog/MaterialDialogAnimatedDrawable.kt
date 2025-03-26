package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.AnimatedDrawableComponentBase
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialDialogAnimatedDrawable private constructor(
    mContext: Context,
    icon: IconAlert,
    mAnimatedVectorDrawable: Boolean,
    mAnimatedVectorDrawableLoop: Boolean,
    message: MessageAlert<*>?,
    mTextSize: Float?,
    mCancelable: Boolean,
    mGravity: Int?,
    mTimeout: Long?,
) : AnimatedDrawableComponentBase(
    mContext = mContext,
    icon = icon,
    mAnimatedVectorDrawable = mAnimatedVectorDrawable,
    mAnimatedVectorDrawableLoop = mAnimatedVectorDrawableLoop,
    message = message,
    mCancelable = mCancelable,
    mTextSize = mTextSize,
    mTimeout = mTimeout
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
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    /**
     * Creates a builder for a progress alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        AnimatedDrawableComponentBase.Builder<MaterialDialogAnimatedDrawable>(context = context) {

        override fun create(): MaterialDialogAnimatedDrawable {
            return MaterialDialogAnimatedDrawable(
                mContext = context,
                icon = icon,
                mAnimatedVectorDrawable = isAnimatedVectorDrawable,
                mAnimatedVectorDrawableLoop = isAnimatedVectorDrawableLoop,
                message = message,
                mTextSize = messageTextSize,
                mCancelable = isCancelable,
                mGravity = gravity,
                mTimeout = onTimeout,
            )
        }

    }

}