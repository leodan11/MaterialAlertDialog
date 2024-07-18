package com.github.leodan11.alertdialog.dist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputCodeBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.helpers.Functions.onValidateTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.core.colorOnSurface
import com.github.leodan11.k_extensions.core.colorPrimary

abstract class AlertDialogVerificationCodeBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mInputsContentValue: List<InputCodeExtra>,
    protected open var mNeedReason: Boolean,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @SuppressLint("WrongConstant")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        val binding: MAlertDialogInputCodeBinding =
            MAlertDialogInputCodeBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        val mIconView = binding.imageViewIconCodeAlertDialog
        val mTitleView = binding.textViewTitleDialogCodeAlert
        val mTitleCodeView = binding.textViewTitleCodeDialogCodeAlert
        val mMessageView = binding.textViewMessageDialogCodeAlert
        val mHeaderLayout = binding.layoutContentHeaderCodeAlertDialog
        val mOtpTextView = binding.otpTextView
        val mContentLayoutInputs = binding.containerInputs
        val mEditTextReasonLayout = binding.textInputLayoutCodeReason
        val mEditTextReasonInfo = binding.textInputEditTextCodeReason
        val mEditTextDecimalNumberLayout = binding.textInputLayoutCodeDecimalNumber
        val mEditTextDecimalNumberInfo = binding.textInputEditTextCodeDecimalNumber
        val mEditTextPercentageLayout = binding.textInputLayoutCodePercentage
        val mEditTextPercentageInfo = binding.textInputEditTextCodePercentage
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialogCode
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialogCode

        // Set Icon
        mIconView.isVisible = icon != null
        icon?.let { mIconView.setImageResource(it.mDrawableResId) }
        // Set Title
        if (title != null) {
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        } else mHeaderLayout.visibility = View.GONE

        // Set Content
        mEditTextReasonLayout.isVisible = mNeedReason
        if (mInputsContentValue.isNotEmpty()) {
            for (item in mInputsContentValue) {
                when (item.inputType) {
                    AlertDialog.Input.PERCENTAGE -> {
                        mEditTextPercentageLayout.hint = item.textHide
                        item.textHelper?.let { mEditTextPercentageLayout.helperText = it }
                        item.textHelperRes?.let {
                            mEditTextPercentageLayout.helperText = mContext.getString(it)
                        }
                        mContentLayoutInputs.visibility = View.VISIBLE
                        mEditTextPercentageLayout.visibility = View.VISIBLE
                        mEditTextPercentageInfo.requestFocus()
                    }

                    AlertDialog.Input.DECIMAL_NUMBER -> {
                        mEditTextDecimalNumberLayout.hint = item.textHide
                        item.textHelper?.let { mEditTextDecimalNumberLayout.helperText = it }
                        item.textHelperRes?.let {
                            mEditTextDecimalNumberLayout.helperText = mContext.getString(it)
                        }
                        mContentLayoutInputs.visibility = View.VISIBLE
                        mEditTextDecimalNumberLayout.visibility = View.VISIBLE
                        mEditTextDecimalNumberInfo.requestFocus()
                    }

                    else -> {
                        if (!mNeedReason) mContentLayoutInputs.visibility = View.GONE
                        mEditTextPercentageLayout.visibility = View.GONE
                        mEditTextDecimalNumberLayout.visibility = View.GONE
                    }
                }
            }
        } else {
            if (mNeedReason) mEditTextReasonInfo.requestFocus()
            else mContentLayoutInputs.visibility = View.GONE
        }
        // Set Message
        if (message != null) {
            mMessageView.visibility = View.VISIBLE
            mMessageView.text = message?.getText()
            mMessageView.textAlignment = message?.textAlignment!!.alignment
        } else mMessageView.visibility = View.GONE
        // Set Positive Button
        if (mPositiveButton != null) {
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
            mPositiveButtonView.setOnClickListener {
                if (mPositiveButton?.onClickVerificationCodeListener != null) {
                    if (!mOtpTextView.otp.isNullOrEmpty() && mOtpTextView.otp!!.length == 6) {
                        mOtpTextView.showSuccess()
                        val code = mOtpTextView.otp ?: ""
                        if (mNeedReason) {
                            if (onValidateTextField(
                                    mEditTextReasonLayout,
                                    mEditTextReasonInfo,
                                    mContext.getString(R.string.text_value_reason_error)
                                )
                            ) {
                                val reason = mEditTextReasonInfo.text.toString()
                                if (mInputsContentValue.isNotEmpty()) {
                                    var decimal: Double? = null
                                    var percentage: Double? = null
                                    var isFinish = false
                                    for (item in mInputsContentValue) {
                                        when (item.inputType) {
                                            AlertDialog.Input.PERCENTAGE -> {
                                                if (onValidateTextField(
                                                        mEditTextPercentageLayout,
                                                        mEditTextPercentageInfo,
                                                        if (item.textError != null) item.textError!! else mContext.getString(
                                                            item.textErrorRes
                                                        )
                                                    )
                                                ) {
                                                    percentage =
                                                        mEditTextPercentageInfo.text.toString()
                                                            .toDouble()
                                                    isFinish = true
                                                }
                                            }

                                            AlertDialog.Input.DECIMAL_NUMBER -> {
                                                if (onValidateTextField(
                                                        mEditTextDecimalNumberLayout,
                                                        mEditTextDecimalNumberInfo,
                                                        if (item.textError != null) item.textError!! else mContext.getString(
                                                            item.textErrorRes
                                                        )
                                                    )
                                                ) {
                                                    decimal =
                                                        mEditTextDecimalNumberInfo.text.toString()
                                                            .toDouble()
                                                    isFinish = true
                                                }
                                            }

                                            else -> {
                                                Toast.makeText(
                                                    mContext,
                                                    mContext.getString(R.string.text_value_unknown_input_type),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                isFinish = false
                                            }
                                        }
                                    }
                                    if (isFinish) mPositiveButton?.onClickVerificationCodeListener?.onClick(
                                        this,
                                        code,
                                        reason,
                                        decimal,
                                        percentage
                                    )
                                } else {
                                    mPositiveButton?.onClickVerificationCodeListener?.onClick(
                                        this,
                                        code,
                                        reason,
                                        null,
                                        null
                                    )
                                }
                            }
                        } else {
                            if (mInputsContentValue.isNotEmpty()) {
                                var decimal: Double? = null
                                var percentage: Double? = null
                                var isFinish = false
                                for (item in mInputsContentValue) {
                                    when (item.inputType) {
                                        AlertDialog.Input.PERCENTAGE -> {
                                            if (onValidateTextField(
                                                    mEditTextPercentageLayout,
                                                    mEditTextPercentageInfo,
                                                    if (item.textError != null) item.textError!! else mContext.getString(
                                                        item.textErrorRes
                                                    )
                                                )
                                            ) {
                                                percentage = mEditTextPercentageInfo.text.toString()
                                                    .toDouble()
                                                isFinish = true
                                            }
                                        }

                                        AlertDialog.Input.DECIMAL_NUMBER -> {
                                            if (onValidateTextField(
                                                    mEditTextDecimalNumberLayout,
                                                    mEditTextDecimalNumberInfo,
                                                    if (item.textError != null) item.textError!! else mContext.getString(
                                                        item.textErrorRes
                                                    )
                                                )
                                            ) {
                                                decimal = mEditTextDecimalNumberInfo.text.toString()
                                                    .toDouble()
                                                isFinish = true
                                            }
                                        }

                                        else -> {
                                            Toast.makeText(
                                                mContext,
                                                mContext.getString(R.string.text_value_unknown_input_type),
                                                Toast.LENGTH_LONG
                                            ).show()
                                            isFinish = false
                                        }
                                    }
                                }
                                if (isFinish) mPositiveButton?.onClickVerificationCodeListener?.onClick(
                                    this,
                                    code,
                                    null,
                                    decimal,
                                    percentage
                                )
                            } else {
                                mPositiveButton?.onClickVerificationCodeListener?.onClick(
                                    this,
                                    code,
                                    null,
                                    null,
                                    null
                                )
                            }
                        }
                    } else {
                        mOtpTextView.showError()
                        Toast.makeText(
                            mContext,
                            mContext.getString(R.string.text_value_code_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else if (mPositiveButton?.onClickListener != null) mPositiveButton?.onClickListener?.onClick(
                    this,
                    AlertDialog.UI.BUTTON_POSITIVE
                )
            }
        } else mPositiveButtonView.visibility = View.GONE
        // Set Negative Button
        if (mNegativeButton != null) {
            mNegativeButtonView.visibility = View.VISIBLE
            mNegativeButtonView.text = mNegativeButton?.title
            if (mNegativeButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNegativeButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mNegativeButton?.icon!!)
            mNegativeButtonView.setOnClickListener {
                mNegativeButton?.onClickListener?.onClick(
                    this,
                    AlertDialog.UI.BUTTON_NEGATIVE
                )
            }
        } else mNegativeButtonView.visibility = View.GONE
        // Apply Styles
        try {
            // Set Icon Color
            if (tintColor != null) {
                if (tintColor?.iconColorRes != null) mIconView.setColorFilter(
                    ContextCompat.getColor(
                        mContext.applicationContext,
                        tintColor?.iconColorRes!!
                    )
                )
                else if (tintColor?.iconColorInt != null) mIconView.setColorFilter(tintColor?.iconColorInt!!)
            }
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Title Code Text Color
            mTitleCodeView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set InputLayout Decimal Number Color
            mEditTextDecimalNumberLayout.boxStrokeColor = mContext.colorPrimary()
            mEditTextDecimalNumberLayout.hintTextColor =
                ColorStateList.valueOf(mContext.colorPrimary())
            // Set InputLayout Percentage Color
            mEditTextPercentageLayout.boxStrokeColor = mContext.colorPrimary()
            mEditTextPercentageLayout.hintTextColor =
                ColorStateList.valueOf(mContext.colorPrimary())
            // Set InputLayout Reason Color
            mEditTextReasonLayout.boxStrokeColor = mContext.colorPrimary()
            mEditTextReasonLayout.hintTextColor =
                ColorStateList.valueOf(mContext.colorPrimary())
            // Set Background Tint
            val mBackgroundTint: ColorStateList =
                ColorStateList.valueOf(mContext.colorPrimary())
            // Set Positive Button Icon Tint
            val mPositiveButtonTint: ColorStateList = mBackgroundTint
            mPositiveButtonView.setTextColor(mPositiveButtonTint)
            mPositiveButtonView.iconTint = mPositiveButtonTint
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList = mBackgroundTint
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            // Set Button Ripple Color
            mPositiveButtonView.rippleColor = mBackgroundTint.withAlpha(75)
            mNegativeButtonView.rippleColor = mBackgroundTint.withAlpha(75)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    /**
     * Cancel this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is canceled.
     *
     */
    override fun cancel() {
        if (mDialog != null) mDialog?.cancel()
        else throwNullDialog()
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is dismissed.
     *
     */
    override fun dismiss() {
        if (mDialog != null) mDialog?.dismiss()
        else throwNullDialog()
    }

    /**
     * Start the dialog and display it on screen.
     * The window is placed in the application layer and opaque.
     * Note that you should not override this method to do initialization when the dialog is shown.
     *
     */
    fun show() {
        if (mDialog != null) mDialog?.show()
        else throwNullDialog()
    }

    /**
     * Set the interface for callback events when the dialog is canceled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: MaterialDialogInterface.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: MaterialDialogInterface.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: MaterialDialogInterface.OnShowListener) {
        this.mOnShowListener = onShowListener
        mDialog?.setOnShowListener { showCallback() }
    }

    private fun cancelCallback() {
        mOnCancelListener?.onCancel(this)
    }

    private fun dismissCallback() {
        mOnDismissListener?.onDismiss(this)
    }

    private fun showCallback() {
        mOnShowListener?.onShow(this)
    }

    private fun throwNullDialog() {
        throw NullPointerException("Called method on null Dialog. Create dialog using `Builder` before calling on Dialog")
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogVerificationCodeBase>(protected open val context: Context) {

        protected open var icon: IconAlertDialog? = null
        protected open var tintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var isNeedReason: Boolean = true
        protected open var isCancelable: Boolean = false
        protected open var mInputsContentValue: List<InputCodeExtra> = arrayListOf()
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }

        /**
         * Set icon tint of [ColorInt].
         *
         * @param tintColor the color int. E.g. [Color.BLUE]
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@ColorInt tintColor: Int): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorInt = tintColor)
            return this
        }

        /**
         * Set icon tint color, Return a color-int from red, green, blue components.
         * These component values should be [0..255],
         * so if they are out of range, the returned color is undefined.
         *
         * @param red to extract the red component
         * @param green to extract the green component
         * @param blue to extract the blue component
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(
            @IntRange(from = 0, to = 255) red: Int,
            @IntRange(from = 0, to = 255) green: Int,
            @IntRange(from = 0, to = 255) blue: Int,
        ): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorInt = Color.rgb(red, green, blue))
            return this
        }

        /**
         * Set icon tint of [ColorRes].
         *
         * @param tintColor the color resource.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColorRes(@ColorRes tintColor: Int): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorRes = tintColor)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String? = null, alignment: AlertDialog.TextAlignment): Builder<D> {
            val valueText =
                if (title.isNullOrEmpty()) context.getString(R.string.text_value_information)
                else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(
            @StringRes title: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.title =
                TitleAlertDialog(title = context.getString(title), textAlignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(
            @StringRes message: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.message =
                MessageAlertDialog.text(text = context.getString(message), alignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
            return this
        }


        /**
         * Set inputs to be displayed. Use class [InputCodeExtra].
         *
         * @param listInputs the list of input.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setInputs(@Size(max = 2) listInputs: List<InputCodeExtra> = arrayListOf()): Builder<D> {
            this.mInputsContentValue = listInputs
            return this
        }

        /**
         * Sets whether the dialog box should specify a reason for the action.
         *
         * @param isNeedReason is [Boolean] value. Default is true.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeedReason(isNeedReason: Boolean): Builder<D> {
            this.isNeedReason = isNeedReason
            return this
        }

        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is false.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickVerificationCodeListener    The [MaterialDialogInterface.OnClickVerificationCodeListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickVerificationCodeListener
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickVerificationCodeListener    The [MaterialDialogInterface.OnClickVerificationCodeListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickVerificationCodeListener
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickVerificationCodeListener    The [MaterialDialogInterface.OnClickVerificationCodeListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            @DrawableRes icon: Int,
            onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_accept)
                else buttonText
            positiveButton = ButtonAlertDialog(
                title = valueText,
                icon = icon,
                onClickVerificationCodeListener = onClickVerificationCodeListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickVerificationCodeListener    The [MaterialDialogInterface.OnClickVerificationCodeListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener,
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickVerificationCodeListener = onClickVerificationCodeListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNegativeButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNegativeButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_cancel)
                else buttonText
            negativeButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            @StringRes buttonText: Int,
            icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Creates an [MaterialAlertDialogVerificationCode] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}