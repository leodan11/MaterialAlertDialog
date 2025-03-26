package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.LottieComponentBase
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LottieAlertDialog private constructor(
    mContext: Context,
    mAnimationAsset: String?,
    mAnimationRaw: Int?,
    mAnimationRepeatCount: Int?,
    mAnimationSpeed: Float?,
    mAnimationUrl: String?,
    mCancelable: Boolean,
    mGravity: Int?,
    mLayoutParams: Int?,
    mTimeout: Long?,
) : LottieComponentBase(
    mContext = mContext,
    mAnimationAsset = mAnimationAsset,
    mAnimationRaw = mAnimationRaw,
    mAnimationRepeatCount = mAnimationRepeatCount,
    mAnimationSpeed = mAnimationSpeed,
    mAnimationUrl = mAnimationUrl,
    mCancelable = mCancelable,
    mLayoutHeight = mLayoutParams,
    mTimeout = mTimeout
) {

    // Init Dialog
    init {
        val builder = MaterialAlertDialogBuilder(mContext)
        val inflater: LayoutInflater = LayoutInflater.from(mContext)
        val dialogView: View = onCreateViewDialogContent(inflater)
        builder.setView(dialogView)
        // Set Cancelable property
        builder.setCancelable(mCancelable)
        // Create and show dialog
        mDialog = builder.create()
        // Set Background Color
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        LottieComponentBase.Builder<LottieAlertDialog>(context = context) {

        override fun create(): LottieAlertDialog {
            return LottieAlertDialog(
                mContext = context,
                mAnimationAsset = lottieAnimationAsset,
                mAnimationRaw = lottieAnimationRaw,
                mAnimationRepeatCount = lottieAnimationRepeatCount,
                mAnimationSpeed = lottieAnimationSpeed,
                mAnimationUrl = lottieAnimationUrl,
                mCancelable = isCancelable,
                mGravity = gravity,
                mLayoutParams = lottieLayoutHeight,
                mTimeout = onTimeout,
            )
        }
    }

}