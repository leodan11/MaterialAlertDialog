package com.github.leodan11.alertdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.leodan11.alertdialog.dist.VerificationCodeComponentBase
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogVerificationCode private constructor(
    mContext: Context,
    icon: IconAlert?,
    tintColor: IconTintAlert?,
    title: TitleAlert?,
    message: MessageAlert<*>?,
    messageXcode: MessageAlert<*>,
    mNeedReason: Boolean,
    mBoxCornerRadius: BoxCornerRadiusTextField?,
    mCountDownTimer: ButtonCountDownTimer?,
    mCancelable: Boolean,
    mGravity: Int?,
    mInputsContentValue: List<InputCodeExtra>,
    mShowInputCode: Boolean,
    mShowMessageCode: Boolean,
    mInputCodeLength: Int,
    mInputCodeMask: String,
    mPositiveButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : VerificationCodeComponentBase(
    mContext = mContext,
    icon = icon,
    tintColor = tintColor,
    title = title,
    message = message,
    messageXcode = messageXcode,
    mNeedReason = mNeedReason,
    boxCornerRadius = mBoxCornerRadius,
    countDownTimer = mCountDownTimer,
    mCancelable = mCancelable,
    mInputsContentValue = mInputsContentValue,
    mShowInputCode = mShowInputCode,
    mShowMessageCode = mShowMessageCode,
    mInputCodeLength = mInputCodeLength,
    mInputCodeMask = mInputCodeMask,
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
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
        }
    }

    /**
     * Creates a builder for a code alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context the parent context
     */
    class Builder(context: Context) :
        VerificationCodeComponentBase.Builder<MaterialAlertDialogVerificationCode>(context = context) {

        override fun create(): MaterialAlertDialogVerificationCode {
            return MaterialAlertDialogVerificationCode(
                mContext = context,
                icon = icon,
                tintColor = tintColor,
                title = title,
                message = message,
                messageXcode = messageXcode,
                mNeedReason = isNeedReason,
                mBoxCornerRadius = boxCornerRadius,
                mCountDownTimer = countDownTimer,
                mCancelable = isCancelable,
                mGravity = gravity,
                mInputsContentValue = mInputsContentValue,
                mShowInputCode = isShowInputCode,
                mShowMessageCode = isShowMessageCode,
                mInputCodeLength = inputCodeLength,
                mInputCodeMask = inputCodeMask,
                mPositiveButton = positiveButton,
                mNegativeButton = negativeButton
            )
        }

    }

}