package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputCodeBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_MASKED
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.helpers.isValidOtp
import com.github.leodan11.alertdialog.io.helpers.setColorList
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toImageView
import com.github.leodan11.alertdialog.io.helpers.toInputEditText
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.context.validateTextField
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class VerificationCodeComponentBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert?,
    protected open var tintColor: IconTintAlert?,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var boxCornerRadius: BoxCornerRadiusTextField?,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var mInputsContentValue: List<InputCodeExtra>,
    protected open var mShowInputCode: Boolean,
    protected open var mInputCodeLength: Int,
    protected open var mInputCodeMask: String,
    protected open var mNeedReason: Boolean,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MAlertDialogInputCodeBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: DialogAlertInterface.OnDismissListener? = null
    protected open var mOnCancelListener: DialogAlertInterface.OnCancelListener? = null
    protected open var mOnShowListener: DialogAlertInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = MAlertDialogInputCodeBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set header layout
                layoutContentHeaderCodeAlertDialog.isVisible = title != null || icon != null
                // Set icon
                icon.toImageView(imageViewIconCodeAlertDialog, tintColor)
                // Set title
                title.toTitleView(textViewTitleDialogCodeAlert)
                // Set Message
                message.toMessageView(textViewMessageDialogCodeAlert)
                // Set Input Code
                otpTextView.apply {
                    setPinCount(mInputCodeLength)
                    setPinMask(mInputCodeMask)
                    setTextVisible(mShowInputCode)
                }
                // Set Background Tint
                val mBackgroundTint: ColorStateList =
                    ColorStateList.valueOf(mContext.colorPrimary())
                // Set Inputs Layout
                containerInputs.isVisible = mNeedReason || mInputsContentValue.isNotEmpty()
                textInputLayoutCodeReason.setColorList(mContext.colorPrimary())
                setInputsInContent(binding)
                // Set InputsLayout Box Corner Radius
                setBoxCustomCornerRadius(
                    textInputLayoutCodeDecimalNumber,
                    textInputLayoutCodePercentage,
                    textInputLayoutCodeReason
                )
                // Set negative button
                buttonActionNegativeAlertDialogCode.apply {
                    mNegativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNegativeButton?.onClickListener?.onClick(
                            this@VerificationCodeComponentBase,
                            DialogAlertInterface.UI.BUTTON_NEGATIVE
                        )
                    }
                }
                // Set positive button
                buttonActionPositiveAlertDialogCode.apply {
                    mPositiveButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        if (otpTextView.isValidOtp(mContext, mInputCodeLength)) {
                            val code = otpTextView.text.orEmpty()
                            val reason = getReasonIfNeeded(
                                textInputLayoutCodeReason,
                                textInputEditTextCodeReason
                            )
                            val firstField = mInputsContentValue.getOrNull(0)?.let {
                                getInputFirst(
                                    textInputLayoutCodeDecimalNumber,
                                    textInputEditTextCodeDecimalNumber
                                )
                            }
                            val lastField = mInputsContentValue.getOrNull(1)?.let {
                                getInputLast(
                                    textInputLayoutCodePercentage,
                                    textInputEditTextCodePercentage
                                )
                            }
                            if (mNeedReason && reason.isNullOrEmpty()) {
                                return@setOnClickListener
                            }
                            mPositiveButton?.onClickVerificationCodeListener?.onClick(
                                this@VerificationCodeComponentBase,
                                code,
                                reason,
                                firstField,
                                lastField
                            )
                        }
                    }
                }
                // Set CountDownTimer to button
                countDownTimer?.let {
                    val button = getButton(it.button)
                    mCountDownTimer = it.toButtonView(button)
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
    open fun getButton(which: DialogAlertInterface.UI): MaterialButton {
        return when (which) {
            DialogAlertInterface.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogCode
            DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogCode
            else -> throw IllegalArgumentException("Button type not supported")
        }
    }


    /**
     * Set the interface for callback events when the dialog is canceled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: DialogAlertInterface.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: DialogAlertInterface.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: DialogAlertInterface.OnShowListener) {
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

    private fun setInputsInContent(binding: MAlertDialogInputCodeBinding) {
        with(binding) {
            if (mInputsContentValue.isNotEmpty()) {
                when (mInputsContentValue.size) {
                    1 -> {
                        mInputsContentValue.first().let {
                            it.toInputEditText(
                                mContext,
                                textInputLayoutCodeDecimalNumber,
                                textInputEditTextCodeDecimalNumber
                            )
                            textInputLayoutCodeDecimalNumber.setColorList(mContext.colorPrimary())
                        }
                    }

                    2 -> {
                        mInputsContentValue.first().let {
                            it.toInputEditText(
                                mContext,
                                textInputLayoutCodeDecimalNumber,
                                textInputEditTextCodeDecimalNumber
                            )
                            textInputLayoutCodeDecimalNumber.setColorList(mContext.colorPrimary())
                        }

                        mInputsContentValue.last().let {
                            it.toInputEditText(
                                mContext,
                                textInputLayoutCodePercentage,
                                textInputEditTextCodePercentage
                            )
                            textInputLayoutCodePercentage.setColorList(mContext.colorPrimary())
                        }

                    }

                    else -> {
                        textInputLayoutCodePercentage.isVisible = false
                        textInputLayoutCodeDecimalNumber.isVisible = false
                        throw IllegalArgumentException("Only two inputs are allowed")
                    }
                }
            }
        }
    }


    private fun getReasonIfNeeded(layout: TextInputLayout, editText: TextInputEditText): String? {
        return if (mNeedReason) {
            if (mContext.validateTextField(layout, editText, R.string.label_text_reason_error)) {
                editText.text.toString()
            } else null
        } else null
    }

    private fun getInputFirst(layout: TextInputLayout, editText: TextInputEditText): String? {
        return getInputValue(
            mInputsContentValue.first(),
            layout,
            editText
        )
    }

    private fun getInputLast(layout: TextInputLayout, editText: TextInputEditText): String? {
        return getInputValue(
            mInputsContentValue.last(),
            layout,
            editText
        )
    }

    private fun getInputValue(
        inputFilter: InputCodeExtra,
        layout: TextInputLayout,
        editText: TextInputEditText
    ): String? {
        val error = inputFilter.textError ?: mContext.getString(inputFilter.textErrorRes)
        return if (mContext.validateTextField(layout, editText, error)) {
            editText.text.toString()
        } else null
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : VerificationCodeComponentBase>(protected open val context: Context) :
        AlertBuilder() {

        protected open var icon: IconAlert? = null
        protected open var tintColor: IconTintAlert? = null
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var boxCornerRadius: BoxCornerRadiusTextField? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var isNeedReason: Boolean = true
        protected open var isCancelable: Boolean = false
        protected open var isShowInputCode: Boolean = true
        protected open var gravity: Int? = null
        protected open var inputCodeLength: Int = 4
        protected open var inputCodeMask: String = DEFAULT_MASKED
        protected open var mInputsContentValue: List<InputCodeExtra> = arrayListOf()
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [Drawable] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIcon(icon: Drawable): Builder<D> {
            this.icon = IconAlert(icon)
            return this
        }


        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlert(icon)
            return this
        }

        /**
         * Set icon tint.
         *
         * @see [ColorStateList]
         *
         * @param tint the color state list.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIconTint(tint: ColorStateList): Builder<D> {
            this.tintColor = IconTintAlert(tint)
            return this
        }


        /**
         * Set icon tint.
         *
         * @see [ColorInt]
         *
         * @param tint the color int. E.g. [Color.BLUE]
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIconTint(@ColorInt tint: Int): Builder<D> {
            this.tintColor = IconTintAlert().apply { tintColorInt = tint }
            return this
        }


        /**
         * Set icon tint color, Return a color-int from red, green, blue components.
         *
         * These component values should be [0..255],
         * so if they are out of range, the returned color is undefined.
         *
         * @param red to extract the red component
         * @param green to extract the green component
         * @param blue to extract the blue component
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIconTint(
            @IntRange(from = 0, to = 255) red: Int,
            @IntRange(from = 0, to = 255) green: Int,
            @IntRange(from = 0, to = 255) blue: Int
        ): Builder<D> {
            this.tintColor = IconTintAlert().apply { tintColorInt = Color.rgb(red, green, blue) }
            return this
        }


        /**
         * Set icon tint.
         *
         * @see [ColorRes]
         *
         * @param tintColor the color resource.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIconTintRes(@ColorRes tintColor: Int): Builder<D> {
            this.tintColor = IconTintAlert(tintColor)
            return this
        }

        /**
         * Set the title displayed in the dialog.
         *
         * @param title The title to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, Alert.TextAlignment.START)
        }


        /**
         * Set the title displayed in the dialog.
         *
         * @param title The title to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, Alert.TextAlignment.START)
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(title: String, alignment: Alert.TextAlignment): Builder<D> {
            this.title = TitleAlert(title, alignment)
            return this
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(@StringRes title: Int, alignment: Alert.TextAlignment): Builder<D> {
            this.title = TitleAlert(context.getString(title), alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: String, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.text(message, alignment)
            return this
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(@StringRes message: Int, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.text(context.getString(message), alignment)
            return this
        }


        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.spanned(text = message, alignment = alignment)
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
         * Set count down timer. Default interval `1000`
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(button: DialogAlertInterface.UI, millis: Long): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis)
            return this
        }

        /**
         * Set count down timer. Default interval `1000`
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param format the format of the countdown timer. Default `%s (%d)`
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            format: String
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, format = format)
            return this
        }


        /**
         * Set count down timer.
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            countInterval: Long
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval)
            return this
        }


        /**
         * Set count down timer.
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         * @param format the format of the countdown timer. Default `%s (%d)`
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            countInterval: Long,
            format: String
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval, format)
            return this
        }

        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is `false`.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Sets whether text input code is visible or not.
         *
         * @param isVisible is [Boolean] value. Default is `true`.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputCodeTextVisible(isVisible: Boolean): Builder<D> {
            this.isShowInputCode = isVisible
            return this
        }

        /**
         * Set gravity of the dialog
         *
         * @param gravity [Int] value
         * @see [android.view.Gravity]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setGravity(gravity: Int): Builder<D> {
            this.gravity = gravity
            return this
        }

        /**
         * Set input code length. Default is 4.
         *
         * @param inputCodeLength [Int] value
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputCodeLength(inputCodeLength: Int): Builder<D> {
            this.inputCodeLength = inputCodeLength
            return this
        }

        /**
         * Set input code mask. Default is [DEFAULT_MASKED].
         *
         * @param inputCodeMask [String] value
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputCodeMask(inputCodeMask: String): Builder<D> {
            this.inputCodeMask = inputCodeMask
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setNegativeButton(
                R.string.label_text_cancel,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(R.string.label_text_cancel, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(text, icon, onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(context.getString(text), icon, onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(onClickListener: DialogAlertInterface.OnClickVerificationCodeListener): Builder<D> {
            return setPositiveButton(
                R.string.label_text_accept,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is  [R.string.label_text_accept].
         *
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickVerificationCodeListener
        ): Builder<D> {
            return setPositiveButton(
                context.getString(R.string.label_text_accept),
                icon,
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickVerificationCodeListener
        ): Builder<D> {
            return setPositiveButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickVerificationCodeListener
        ): Builder<D> {
            this.positiveButton =
                initPositiveButton(text, icon, verificationCodeListener = onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickVerificationCodeListener
        ): Builder<D> {
            return setPositiveButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickVerificationCodeListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickVerificationCodeListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(
                context.getString(text),
                icon,
                verificationCodeListener = onClickListener
            )
            return this
        }

        /**
         * Creates an [MaterialAlertDialogVerificationCode] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = MaterialAlertDialogVerificationCode.Builder(context)
         *     ...
         *     .create()
         * dialog.show()
         *
         * ```
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}