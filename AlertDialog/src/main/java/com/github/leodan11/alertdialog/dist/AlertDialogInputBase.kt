package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.MaterialAlertDialogInput
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputBinding
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconInputDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.InputAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.core.colorOnSurface
import com.github.leodan11.k_extensions.core.colorPrimary
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class AlertDialogInputBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var iconTintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var counterMax: Int?,
    protected open var startIcon: IconInputDialog?,
    protected open var endIcon: IconInputDialog?,
    protected open var inputBase: InputAlertDialog,
    protected open var isCancelable: Boolean,
    protected open var positiveButton: ButtonAlertDialog?,
    protected open var negativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        val binding: MAlertDialogInputBinding = MAlertDialogInputBinding.inflate(layoutInflater)
        val mIconView = binding.imageViewIconInputAlertDialog
        val mContentHeader = binding.layoutContentHeaderInputAlertDialog
        val mTitleView = binding.textViewTitleDialogInputAlert
        val mMessageView = binding.textViewMessageDialogInputAlert
        val mTextInputLayoutAlert = binding.textInputLayoutAlert
        val mTextInputEditTextAlert = binding.textInputEditTextAlert
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialog
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialog

        // Set Icon
        if (icon != null) {
            icon?.let { mIconView.setImageResource(it.mDrawableResId) }
            mIconView.visibility = View.VISIBLE
            mContentHeader.visibility = View.VISIBLE
        } else mIconView.visibility = View.GONE
        // Set Title
        if (title != null) {
            mTitleView.apply {
                text = title?.title
                textAlignment = title?.textAlignment!!.alignment
                visibility = View.VISIBLE
            }
            mContentHeader.visibility = View.VISIBLE
        } else mTitleView.visibility = View.GONE
        // Set Message
        if (message != null) {
            mMessageView.apply {
                text = message?.getText()
                textAlignment = message?.textAlignment!!.alignment
                visibility = View.VISIBLE
            }
        } else mMessageView.visibility = View.GONE
        // Set Content
        mTextInputLayoutAlert.apply {
            hint = inputBase.textHide
            inputBase.textHelper?.let { srt -> helperText = srt }
            inputBase.textHelperRes?.let { res -> helperText = mContext.getString(res) }
            isHelperTextEnabled = helperText != null
        }
        mTextInputEditTextAlert.inputType = when (inputBase.inputType) {
            MaterialAlertDialog.InputType.DECIMAL_NUMBER -> EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
            MaterialAlertDialog.InputType.EMAIL -> {
                mTextInputLayoutAlert.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            MaterialAlertDialog.InputType.NUMBER -> EditorInfo.TYPE_CLASS_NUMBER
            MaterialAlertDialog.InputType.PASSWORD -> {
                mTextInputLayoutAlert.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            }

            MaterialAlertDialog.InputType.PHONE -> EditorInfo.TYPE_CLASS_PHONE
            MaterialAlertDialog.InputType.TEXT -> {
                mTextInputLayoutAlert.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                EditorInfo.TYPE_CLASS_TEXT
            }

            else -> throw IllegalArgumentException("Bad Input Type")
        }
        counterMax?.let {
            mTextInputLayoutAlert.counterMaxLength = it
            mTextInputLayoutAlert.isCounterEnabled = true
        }
        startIcon?.let {
            mTextInputLayoutAlert.apply {
                startIconDrawable = ContextCompat.getDrawable(mContext, it.icon)
                startIconContentDescription =
                    it.contentDescription ?: mContext.getString(it.contentDescriptionRes!!)
                setStartIconOnClickListener(it.listener)
            }
        }
        endIcon?.let {
            mTextInputLayoutAlert.apply {
                endIconDrawable = ContextCompat.getDrawable(mContext, it.icon)
                endIconContentDescription =
                    it.contentDescription ?: mContext.getString(it.contentDescriptionRes!!)
                setEndIconOnClickListener(it.listener)
            }
        }
        // Set Positive Button
        if (positiveButton != null) {
            mPositiveButtonView.apply {
                this.text = positiveButton?.title
                positiveButton?.let {
                    if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) this.icon =
                        ContextCompat.getDrawable(mContext.applicationContext, it.icon)
                }
                setOnClickListener {
                    if (positiveButton?.onClickInputListener != null) {
                        if (validateLayoutEditText(
                                mTextInputLayoutAlert,
                                mTextInputEditTextAlert,
                                inputBase
                            )
                        ) {
                            positiveButton?.onClickInputListener?.onClick(
                                this@AlertDialogInputBase,
                                mTextInputEditTextAlert.text.toString()
                            )
                        }
                    }
                }
                visibility = View.VISIBLE
            }
        } else mPositiveButtonView.visibility = View.GONE
        // Set Negative Button
        if (negativeButton != null) {
            mNegativeButtonView.apply {
                this.text = negativeButton?.title
                negativeButton?.let {
                    if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) this.icon =
                        ContextCompat.getDrawable(mContext.applicationContext, it.icon)
                }
                setOnClickListener {
                    negativeButton?.onClickListener?.onClick(
                        this@AlertDialogInputBase,
                        MaterialAlertDialog.UI.BUTTON_NEGATIVE
                    )
                }
                visibility = View.VISIBLE
            }
        } else mNegativeButtonView.visibility = View.GONE
        try {
            // Apply Styles
            // Set Icon Color
            if (iconTintColor != null) {
                if (iconTintColor?.iconColorRes != null) mIconView.setColorFilter(
                    ContextCompat.getColor(
                        mContext.applicationContext,
                        iconTintColor?.iconColorRes!!
                    )
                )
                else if (iconTintColor?.iconColorInt != null) mIconView.setColorFilter(
                    iconTintColor?.iconColorInt!!
                )
            }
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set InputLayout Color
            mTextInputLayoutAlert.boxStrokeColor = mContext.colorPrimary()
            mTextInputLayoutAlert.hintTextColor =
                ColorStateList.valueOf(mContext.colorPrimary())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) mTextInputLayoutAlert.cursorColor =
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

    private fun validateLayoutEditText(
        textInputLayoutAlert: TextInputLayout,
        textInputEditTextAlert: TextInputEditText,
        inputBase: InputAlertDialog,
    ): Boolean {
        return if (!TextUtils.isEmpty(textInputEditTextAlert.text.toString().trim())) {
            textInputLayoutAlert.isErrorEnabled = false
            true
        } else {
            textInputLayoutAlert.isErrorEnabled = true
            textInputLayoutAlert.error = inputBase.textError
                ?: mContext.getString(inputBase.textErrorRes)
            false
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogInputBase>(protected val context: Context) {

        protected open var icon: IconAlertDialog? = null
        protected open var iconTintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var counterMaxLength: Int? = null
        protected open var startIcon: IconInputDialog? = null
        protected open var endIcon: IconInputDialog? = null
        protected open var inputBase: InputAlertDialog = InputAlertDialog(
            inputType = MaterialAlertDialog.InputType.TEXT,
            textHide = context.getString(R.string.text_value_enter_a_value_below)
        )
        protected open var isCancelable: Boolean = false
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
         * Set icon tint of [ColorRes].
         *
         * @param tintColor the color resource.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@ColorRes tintColor: Int): Builder<D> {
            this.iconTintColor = IconTintAlertDialog(iconColorRes = tintColor)
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
            this.iconTintColor = IconTintAlertDialog(iconColorInt = Color.rgb(red, green, blue))
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogInput].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogInput].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogInput]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            val valueText =
                if (title.isNullOrEmpty()) context.getString(R.string.text_value_information) else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogInput]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(
            @StringRes title: Int,
            alignment: MaterialAlertDialog.TextAlignment,
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the message to display. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(
            @StringRes message: Int,
            alignment: MaterialAlertDialog.TextAlignment,
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set counter-max length
         *
         * @param maxCounter Number
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setCounterMaxLength(maxCounter: Int): Builder<D> {
            this.counterMaxLength = maxCounter
            return this
        }

        /**
         * Set inputs to be displayed. Use class [InputAlertDialog].
         * [MaterialAlertDialog.InputType.DECIMAL_NUMBER], [MaterialAlertDialog.InputType.EMAIL], [MaterialAlertDialog.InputType.NUMBER], [MaterialAlertDialog.InputType.PASSWORD], [MaterialAlertDialog.InputType.PHONE] and [MaterialAlertDialog.InputType.TEXT]
         *
         * @param inputSource The input source.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setInitInput(inputSource: InputAlertDialog): Builder<D> {
            this.inputBase = inputSource
            return this
        }

        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is true.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Set an icon at the start of the [TextInputLayout]
         *
         * @param startIcon Display icon
         * @param contentDescription Brief description about icon displayed
         * @param listener Listener when the icon is precious. Null by default
         * @return This Builder object to allow for chaining of calls to set methods
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
         * @return This Builder object to allow for chaining of calls to set methods
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
         * @return This Builder object to allow for chaining of calls to set methods
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
         * @return This Builder object to allow for chaining of calls to set methods
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
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListenerInput    The [MaterialDialogInterface.OnClickInputListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            onClickListenerInput: MaterialDialogInterface.OnClickInputListener,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListenerInput
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListenerInput    The [MaterialDialogInterface.OnClickInputListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickListenerInput: MaterialDialogInterface.OnClickInputListener,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListenerInput
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListenerInput    The [MaterialDialogInterface.OnClickInputListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            @DrawableRes icon: Int,
            onClickListenerInput: MaterialDialogInterface.OnClickInputListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_accept) else buttonText
            this.positiveButton = ButtonAlertDialog(
                title = valueText,
                icon = icon,
                onClickInputListener = onClickListenerInput
            )
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListenerInput    The [MaterialDialogInterface.OnClickInputListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListenerInput: MaterialDialogInterface.OnClickInputListener,
        ): Builder<D> {
            this.positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickInputListener = onClickListenerInput
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
            buttonText: String?,
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
            buttonText: String?,
            icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_cancel) else buttonText
            this.negativeButton =
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
            this.negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }


        /**
         * Creates an [MaterialAlertDialogInput] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D
    }

}