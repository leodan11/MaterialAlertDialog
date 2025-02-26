package com.github.leodan11.alertdialog.dist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
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
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputCodeBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.context.validateTextField
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale
import java.util.concurrent.TimeUnit

abstract class AlertDialogVerificationCodeBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var boxCornerRadius: BoxCornerRadiusTextField?,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var mInputsContentValue: List<InputCodeExtra>,
    protected open var mNeedReason: Boolean,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MAlertDialogInputCodeBinding
    private var mCountDownTimer: CountDownTimer? = null
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
        binding = MAlertDialogInputCodeBinding.inflate(layoutInflater, container, false)
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
                            if (mContext.validateTextField(
                                    mEditTextReasonLayout,
                                    mEditTextReasonInfo,
                                    message = mContext.getString(R.string.label_text_reason_error)
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
                                                if (mContext.validateTextField(
                                                        mEditTextPercentageLayout,
                                                        mEditTextPercentageInfo,
                                                        message = if (item.textError != null) item.textError!! else mContext.getString(
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
                                                if (mContext.validateTextField(
                                                        mEditTextDecimalNumberLayout,
                                                        mEditTextDecimalNumberInfo,
                                                        message = if (item.textError != null) item.textError!! else mContext.getString(
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
                                                    mContext.getString(R.string.label_text_unknown_input_type),
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
                                            if (mContext.validateTextField(
                                                    mEditTextPercentageLayout,
                                                    mEditTextPercentageInfo,
                                                    message = if (item.textError != null) item.textError!! else mContext.getString(
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
                                            if (mContext.validateTextField(
                                                    mEditTextDecimalNumberLayout,
                                                    mEditTextDecimalNumberInfo,
                                                    message = if (item.textError != null) item.textError!! else mContext.getString(
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
                                                mContext.getString(R.string.label_text_unknown_input_type),
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
                            mContext.getString(R.string.label_text_code_error),
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
            // Set InputsLayout Box Corner Radius
            setBoxCustomCornerRadius(
                mEditTextDecimalNumberLayout,
                mEditTextPercentageLayout,
                mEditTextReasonLayout
            )
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
            // Set CountDownTimer to button
            countDownTimer?.let { timer ->
                val button = getButton(timer.button)
                val buttonText = button.text
                mCountDownTimer = object : CountDownTimer(timer.millis, timer.countInterval) {
                    override fun onTick(millisUntilFinished: Long) {
                        button.apply {
                            isEnabled = false
                            alpha = 0.5f
                            text = String.format(
                                Locale.getDefault(),
                                "%s (%d)",
                                buttonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                            )
                        }
                    }

                    override fun onFinish() {
                        if (isShowing) {
                            button.apply {
                                isEnabled = true
                                alpha = 1f
                                text = buttonText
                            }
                        }
                    }
                }
            }
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
        if (mDialog != null) {
            mCountDownTimer?.cancel()
            mDialog?.cancel()
        } else throwNullDialog()
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is dismissed.
     *
     */
    override fun dismiss() {
        if (mDialog != null) {
            mCountDownTimer?.cancel()
            mDialog?.dismiss()
        } else throwNullDialog()
    }

    /**
     * Start the dialog and display it on screen.
     * The window is placed in the application layer and opaque.
     * Note that you should not override this method to do initialization when the dialog is shown.
     *
     */
    open fun show() {
        if (mDialog != null) {
            mDialog?.show()
            mCountDownTimer?.start()
        } else throwNullDialog()
    }


    /**
     * Get the button with the specified type.
     *
     * @param which The type of button.
     *
     * @return [MaterialButton]
     *
     * @throws IllegalArgumentException
     *
     */
    @Throws(IllegalArgumentException::class)
    open fun getButton(which: AlertDialog.UI): MaterialButton {
        return when (which) {
            AlertDialog.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogCode
            AlertDialog.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogCode
            else -> throw IllegalArgumentException("Button type not supported")
        }
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

    private fun setBoxCustomCornerRadius(
        decimalNumberLayout: TextInputLayout,
        percentageLayout: TextInputLayout,
        reasonLayout: TextInputLayout
    ) {
        boxCornerRadius?.let {
            decimalNumberLayout.setBoxCornerRadii(
                it.topStart,
                it.topEnd,
                it.bottomStart,
                it.bottomEnd
            )
            percentageLayout.setBoxCornerRadii(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
            reasonLayout.setBoxCornerRadii(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
        }
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
        protected open var boxCornerRadius: BoxCornerRadiusTextField? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var isNeedReason: Boolean = true
        protected open var isCancelable: Boolean = false
        protected open var mInputsContentValue: List<InputCodeExtra> = arrayListOf()
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }

        /**
         * Set icon tint of [ColorInt].
         *
         * @param tintColor the color int. E.g. [Color.BLUE]
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconTintColorRes(@ColorRes tintColor: Int): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorRes = tintColor)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String? = null, alignment: AlertDialog.TextAlignment): Builder<D> {
            val valueText =
                if (title.isNullOrEmpty()) context.getString(R.string.label_text_information)
                else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogVerificationCode]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set input first to be displayed.
         *
         * @param inputStream Use class [InputCodeExtra].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputFirstExtra(inputStream: InputCodeExtra): Builder<D> {
            val inputs = this.mInputsContentValue.toMutableList()
            inputs.add(inputStream)
            this.mInputsContentValue = inputs.toList()
            return this
        }

        /**
         * Set input second to be displayed.
         *
         * @param inputStream Use class [InputCodeExtra].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputSecondExtra(inputStream: InputCodeExtra): Builder<D> {
            val inputs = this.mInputsContentValue.toMutableList()
            inputs.add(inputStream)
            this.mInputsContentValue = inputs.toList()
            return this
        }

        /**
         *  Set the corner radius of the [TextInputLayout]
         *
         * @param radius The radius to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setBoxCornerRadius(radius: Float): Builder<D> {
            this.boxCornerRadius = BoxCornerRadiusTextField(radius, radius, radius, radius)
            return this
        }

        /**
         *  Set the corner radius of the [TextInputLayout]
         *
         * @param topStart The top start radius to use.
         * @param topEnd The top end radius to use.
         * @param bottomStart The bottom start radius to use.
         * @param bottomEnd The bottom end radius to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setBoxCornerRadius(
            topStart: Float,
            topEnd: Float,
            bottomStart: Float,
            bottomEnd: Float
        ): Builder<D> {
            this.boxCornerRadius =
                BoxCornerRadiusTextField(topStart, topEnd, bottomStart, bottomEnd)
            return this
        }

        /**
         * Sets whether the dialog box should specify a reason for the action.
         *
         * @param isNeedReason is [Boolean] value. Default is true.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeedReason(isNeedReason: Boolean): Builder<D> {
            this.isNeedReason = isNeedReason
            return this
        }

        /**
         * Set count down timer. Default `1000`
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE` or `AlertDialog.UI.BUTTON_NEGATIVE`
         * @param millis [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(button: AlertDialog.UI, millis: Long): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis)
            return this
        }

        /**
         * Set count down timer.
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE` or `AlertDialog.UI.BUTTON_NEGATIVE`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: AlertDialog.UI,
            millis: Long,
            countInterval: Long
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval)
            return this
        }

        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is false.
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            @DrawableRes icon: Int,
            onClickVerificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_accept)
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_cancel)
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}