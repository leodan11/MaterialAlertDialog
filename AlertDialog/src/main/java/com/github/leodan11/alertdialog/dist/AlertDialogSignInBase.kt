package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.MaterialAlertDialogSignIn
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.config.Init.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.databinding.MAlertDialogLoginBinding
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultOnSurfaceTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultPrimaryTheme
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class AlertDialogSignInBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open lateinit var mTextInputLayoutUsername: TextInputLayout
    protected open lateinit var mTextInputEditTextUsername: TextInputEditText
    protected open lateinit var mTextInputLayoutPassword: TextInputLayout
    protected open lateinit var mTextInputEditTextPassword: TextInputEditText
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val binding: MAlertDialogLoginBinding = MAlertDialogLoginBinding.inflate(layoutInflater)
        // Initialize Views
        val mIconView = binding.imageViewIconLoginDialog
        val mTitleView = binding.textViewTitleDialogLogin
        mTextInputLayoutUsername = binding.textInputLayoutUsername
        mTextInputEditTextUsername = binding.textInputEditTextUsername
        mTextInputLayoutPassword = binding.textInputLayoutPassword
        mTextInputEditTextPassword = binding.textInputEditTextPassword
        val mPositiveButtonView = binding.buttonActionPositiveLoginDialog
        val mNegativeButtonView = binding.buttonActionNegativeLoginDialog
        // Set Icon
        if (icon != null) icon?.let { mIconView.setImageResource(it.mDrawableResId) }
        // Set Title
        if (title != null) {
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        }
        // Set Positive Button
        if (mPositiveButton != null) {
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
            mPositiveButtonView.setOnClickListener {
                if (mPositiveButton?.onClickInvokedCallback != null) {
                    if (onValidateInputValue()) mPositiveButton?.onClickInvokedCallback?.onClick(
                        this,
                        mTextInputEditTextUsername.text.toString().trim(),
                        mTextInputEditTextPassword.text.toString()
                    )
                } else mPositiveButton?.onClickListener?.onClick(
                    this,
                    MaterialAlertDialog.UI.BUTTON_POSITIVE
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
                    MaterialAlertDialog.UI.BUTTON_NEGATIVE
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
            mTitleView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // Set InputLayout Username Color
            mTextInputLayoutUsername.boxStrokeColor = mContext.getColorDefaultPrimaryTheme()
            mTextInputLayoutUsername.hintTextColor =
                ColorStateList.valueOf(mContext.getColorDefaultPrimaryTheme())
            // Set InputLayout Password Color
            mTextInputLayoutPassword.boxStrokeColor = mContext.getColorDefaultPrimaryTheme()
            mTextInputLayoutPassword.hintTextColor =
                ColorStateList.valueOf(mContext.getColorDefaultPrimaryTheme())
            // Set Background Tint
            val mBackgroundTint: ColorStateList =
                ColorStateList.valueOf(mContext.getColorDefaultPrimaryTheme())
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
     * Note that you should not override this method to do cleanup when the dialog is cancelled.
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
     * Set interface for callback events when dialog is cancelled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: MaterialDialogInterface.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set interface for callback events when dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: MaterialDialogInterface.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set interface for callback events when dialog is shown.
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

    private fun onValidateInputValue(): Boolean {
        if (TextUtils.isEmpty(mTextInputEditTextUsername.text.toString().trim())) {
            mTextInputLayoutUsername.error = mContext.getString(R.string.text_value_username_error)
            mTextInputLayoutUsername.isErrorEnabled = true
            return false
        } else mTextInputLayoutUsername.isErrorEnabled = false
        if (TextUtils.isEmpty(mTextInputEditTextPassword.text.toString().trim())) {
            mTextInputLayoutPassword.error = mContext.getString(R.string.text_value_password_error)
            mTextInputLayoutPassword.isErrorEnabled = true
            return false
        } else mTextInputLayoutPassword.isErrorEnabled = false
        return true
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogSignInBase>(protected open val context: Context) {

        protected open var icon: IconAlertDialog? = null
        protected open var tintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
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
            this.tintColor = IconTintAlertDialog(iconColorRes = tintColor)
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
         * Set the title displayed in the [MaterialAlertDialogSignIn].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogSignIn].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogSignIn]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            val valueText = if (title.isNullOrEmpty()) context.getString(R.string.text_value_login)
            else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogSignIn]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
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
         * @param onClickInvokedCallback    The [MaterialDialogInterface.OnClickInvokedCallback] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            onClickInvokedCallback: MaterialDialogInterface.OnClickInvokedCallback,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickInvokedCallback
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickInvokedCallback    The [MaterialDialogInterface.OnClickInvokedCallback] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickInvokedCallback: MaterialDialogInterface.OnClickInvokedCallback,
        ): Builder<D> {
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickInvokedCallback
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickInvokedCallback    The [MaterialDialogInterface.OnClickInvokedCallback] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            @DrawableRes icon: Int,
            onClickInvokedCallback: MaterialDialogInterface.OnClickInvokedCallback,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_login_in)
                else buttonText
            positiveButton = ButtonAlertDialog(
                title = valueText,
                icon = icon,
                onClickInvokedCallback = onClickInvokedCallback
            )
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickInvokedCallback    The [MaterialDialogInterface.OnClickInvokedCallback] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickInvokedCallback: MaterialDialogInterface.OnClickInvokedCallback,
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickInvokedCallback = onClickInvokedCallback
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
         * Creates an [MaterialAlertDialogSignIn] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}