package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.MaterialAlertDialogInput
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialAlert
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toImageView
import com.github.leodan11.alertdialog.io.helpers.toInputSampleView
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconInputDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.k_extensions.color.colorPrimary
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher

abstract class AlertDialogInputBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert?,
    protected open var iconTintColor: IconTintAlert?,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var maskedFormatter: String?,
    protected open var boxCornerRadius: BoxCornerRadiusTextField?,
    protected open var counterMax: Int?,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var startIcon: IconInputDialog?,
    protected open var endIcon: IconInputDialog?,
    protected open var inputTextHide: String,
    protected open var inputTextHelper: String?,
    protected open var inputTextError: String?,
    protected open var inputTextDefault: String?,
    protected open var isCancelable: Boolean,
    protected open var positiveButton: ButtonAlertDialog?,
    protected open var negativeButton: ButtonAlertDialog?,
) : MaterialAlert {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MAlertDialogInputBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialAlert.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialAlert.OnCancelListener? = null
    protected open var mOnShowListener: MaterialAlert.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = MAlertDialogInputBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set header layout
                layoutContentHeaderInputAlertDialog.isVisible = title != null || icon != null
                // Set Icon
                icon.toImageView(imageViewIconInputAlertDialog, iconTintColor)
                // Set Title
                title.toTitleView(textViewTitleDialogInputAlert)
                // Set Message
                message.toMessageView(textViewMessageDialogInputAlert)
                // Set Content
                mContext.toInputSampleView(
                    textInputLayoutAlert,
                    inputTextHide,
                    inputTextHelper,
                    inputTextError,
                    boxCornerRadius,
                    counterMax,
                    startIcon,
                    endIcon
                )
                textInputEditTextAlert.apply {
                    inputTextDefault?.let { setText(it) }
                    maskedFormatter?.let {
                        val formatter = MaskedFormatter(it)
                        addTextChangedListener(MaskedWatcher(formatter, this))
                    }
                }
                // Set Background Tint
                val mBackgroundTint: ColorStateList =
                    ColorStateList.valueOf(mContext.colorPrimary())
                // Set Positive Button
                inputActions.buttonActionPositiveAlertDialog.apply {
                    positiveButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        positiveButton?.onClickInputListener?.let { listener ->
                            if (validateLayoutEditText(
                                    textInputLayoutAlert,
                                    textInputEditTextAlert,
                                    inputTextError
                                )
                            ) {
                                listener.onClick(
                                    this@AlertDialogInputBase,
                                    textInputEditTextAlert.text.toString()
                                )
                            }
                        }
                    }
                }
                // Set Negative Button
                inputActions.buttonActionNegativeAlertDialog.apply {
                    negativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        negativeButton?.onClickListener?.onClick(
                            this@AlertDialogInputBase,
                            AlertDialog.UI.BUTTON_NEGATIVE
                        )
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
    open fun getButton(which: AlertDialog.UI): MaterialButton {
        return when (which) {
            AlertDialog.UI.BUTTON_POSITIVE -> binding.inputActions.buttonActionPositiveAlertDialog
            AlertDialog.UI.BUTTON_NEGATIVE -> binding.inputActions.buttonActionNegativeAlertDialog
            else -> throw IllegalArgumentException("Button type not supported")
        }
    }


    /**
     * Set the interface for callback events when the dialog is canceled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: MaterialAlert.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: MaterialAlert.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: MaterialAlert.OnShowListener) {
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

    private fun validateLayoutEditText(
        textInputLayoutAlert: TextInputLayout,
        textInputEditTextAlert: EditText,
        textError: String?,
    ): Boolean {
        return if (!TextUtils.isEmpty(textInputEditTextAlert.text.toString().trim())) {
            textInputLayoutAlert.isErrorEnabled = false
            true
        } else {
            textInputLayoutAlert.isErrorEnabled = true
            textInputLayoutAlert.error =
                textError ?: mContext.getString(R.string.label_text_this_field_is_required)
            false
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogInputBase>(protected val context: Context) :
        AlertBuilder() {

        protected open var icon: IconAlert? = null
        protected open var iconTintColor: IconTintAlert? = null
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var maskedFormatter: String? = null
        protected open var boxCornerRadius: BoxCornerRadiusTextField? = null
        protected open var counterMaxLength: Int? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var startIcon: IconInputDialog? = null
        protected open var endIcon: IconInputDialog? = null
        protected open var inputTextHide: String =
            context.getString(R.string.label_text_enter_a_value_below)
        protected open var inputTextHelper: String? = null
        protected open var inputTextError: String? = null
        protected open var inputTextDefault: String? = null
        protected open var isCancelable: Boolean = false
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
            this.iconTintColor = IconTintAlert(tint)
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
            this.iconTintColor = IconTintAlert().apply { tintColorInt = tint }
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
            this.iconTintColor =
                IconTintAlert().apply { tintColorInt = Color.rgb(red, green, blue) }
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
            this.iconTintColor = IconTintAlert(tintColor)
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
            return setTitle(title, AlertDialog.TextAlignment.START)
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
            return setTitle(title, AlertDialog.TextAlignment.START)
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [AlertDialog.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(title: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.title = TitleAlert(title, alignment)
            return this
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [AlertDialog.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(@StringRes title: Int, alignment: AlertDialog.TextAlignment): Builder<D> {
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
            return setMessage(message, AlertDialog.TextAlignment.START)
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
            return setMessage(message, AlertDialog.TextAlignment.START)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [AlertDialog.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlert.text(message, alignment)
            return this
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [AlertDialog.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(@StringRes message: Int, alignment: AlertDialog.TextAlignment): Builder<D> {
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
            return setMessage(message, AlertDialog.TextAlignment.START)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [AlertDialog.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlert.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set counter-max length
         *
         * @param maxCounter Number
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCounterMaxLength(maxCounter: Int): Builder<D> {
            this.counterMaxLength = maxCounter
            return this
        }

        /**
         * Set count down timer. Default interval `1000`
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(button: AlertDialog.UI, millis: Long): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis)
            return this
        }

        /**
         * Set count down timer. Default interval `1000`
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param format the format of the countdown timer. Default `%s (%d)`
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(button: AlertDialog.UI, millis: Long, format: String): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, format = format)
            return this
        }


        /**
         * Set count down timer.
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
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
         * Set count down timer.
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         * @param format the format of the countdown timer. Default `%s (%d)`
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: AlertDialog.UI,
            millis: Long,
            countInterval: Long,
            format: String
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval, format)
            return this
        }

        /**
         * Set masked formatter
         *
         * @param formatter The formatter to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMaskedFormatter(formatter: String): Builder<D> {
            this.maskedFormatter = formatter
            return this
        }

        /**
         * Set input hint
         *
         * @param hint The hint to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputHint(hint: String): Builder<D> {
            this.inputTextHide = hint
            return this
        }

        /**
         * Set input hint
         *
         * @param hint The hint to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputHint(@StringRes hint: Int): Builder<D> {
            this.inputTextHide = context.getString(hint)
            return this
        }

        /**
         * Set input error
         *
         * @param error The error to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputError(error: String): Builder<D> {
            this.inputTextError = error
            return this
        }

        /**
         * Set input error
         *
         * @param error The error to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputError(@StringRes error: Int): Builder<D> {
            this.inputTextError = context.getString(error)
            return this
        }

        /**
         * Set input helper text
         *
         * @param helperText The helper text to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputHelperText(helperText: String): Builder<D> {
            this.inputTextHelper = helperText
            return this
        }

        /**
         * Set input helper text
         *
         * @param helperText The helper text to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputHelperText(@StringRes helperText: Int): Builder<D> {
            this.inputTextHelper = context.getString(helperText)
            return this
        }

        /**
         * Set input default value
         *
         * @param valueHolder The default value to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setInputDefaultValue(valueHolder: String): Builder<D> {
            this.inputTextDefault = valueHolder
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
         * Set an icon at the start of the [TextInputLayout]
         *
         * @param startIcon Display icon
         * @param contentDescription Brief description about icon displayed
         * @param listener Listener when the icon is precious. Null by default
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setStartIconLayoutInput(
            @DrawableRes startIcon: Int,
            contentDescription: String,
            listener: View.OnClickListener? = null,
        ): Builder<D> {
            this.startIcon = IconInputDialog(startIcon, contentDescription, listener = listener)
            return this
        }

        /**
         * Set an icon at the start of the [TextInputLayout]
         *
         * @param startIcon Display icon
         * @param contentDescription Brief description about icon displayed
         * @param listener Listener when the icon is precious. Null by default
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setStartIconLayoutInput(
            @DrawableRes startIcon: Int,
            @StringRes contentDescription: Int,
            listener: View.OnClickListener? = null,
        ): Builder<D> {
            this.startIcon = IconInputDialog(
                startIcon,
                contentDescriptionRes = contentDescription,
                listener = listener
            )
            return this
        }

        /**
         * Set an icon at the start of the [TextInputLayout]
         *
         * @param endIcon Display icon
         * @param contentDescription Brief description about icon displayed
         * @param listener Listener when the icon is precious. Null by default
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setEndIconLayoutInput(
            @DrawableRes endIcon: Int,
            contentDescription: String,
            listener: View.OnClickListener? = null,
        ): Builder<D> {
            this.endIcon = IconInputDialog(endIcon, contentDescription, listener = listener)
            return this
        }

        /**
         * Set an icon at the start of the [TextInputLayout]
         *
         * @param endIcon Display icon
         * @param contentDescription Brief description about icon displayed
         * @param listener Listener when the icon is precious. Null by default
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setEndIconLayoutInput(
            @DrawableRes endIcon: Int,
            @StringRes contentDescription: Int,
            listener: View.OnClickListener? = null,
        ): Builder<D> {
            this.endIcon = IconInputDialog(
                endIcon,
                contentDescriptionRes = contentDescription,
                listener = listener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(onClickListener: MaterialAlert.OnClickListener): Builder<D> {
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
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            onClickListener: MaterialAlert.OnClickListener
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
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickListener
        ): Builder<D> {
            return setNegativeButton(R.string.label_text_cancel, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            onClickListener: MaterialAlert.OnClickListener
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
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [MaterialAlert.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(context.getString(text), icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(onClickListener: MaterialAlert.OnClickInputListener): Builder<D> {
            return setPositiveButton(
                R.string.label_text_accept,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickInputListener
        ): Builder<D> {
            return setPositiveButton(R.string.label_text_accept, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            onClickListener: MaterialAlert.OnClickInputListener
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
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickInputListener
        ): Builder<D> {
            this.positiveButton =
                initPositiveButton(text, icon, contentValueListener = onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            onClickListener: MaterialAlert.OnClickInputListener
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
         * @param onClickListener    The [MaterialAlert.OnClickInputListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: MaterialAlert.OnClickInputListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(
                context.getString(text),
                icon,
                contentValueListener = onClickListener
            )
            return this
        }


        /**
         * Creates an [MaterialAlertDialogInput] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = MaterialAlertDialogInput.Builder(context)
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