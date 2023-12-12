package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.MaterialAlertDialogCentered
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.config.Init.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.databinding.MAlertDialogCenteredBinding
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.core.colorOnSurface
import com.github.leodan11.k_extensions.core.colorPrimary

abstract class AlertDialogCenteredBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var backgroundColorInt: Int?,
    protected open var backgroundColorResource: Int?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCreateViewDialogContent(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        val binding: MAlertDialogCenteredBinding = MAlertDialogCenteredBinding.inflate(layoutInflater)
        // Initialize Views
        val mIconView = binding.imageViewIconAlertDialog
        val mTitleView = binding.textViewTitleAlertDialog
        val mMessageView = binding.textViewMessageAlertDialog
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialog
        val mNeutralButtonView = binding.buttonActionNeutralAlertDialog
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialog
        // Set Icon
        mIconView.setImageResource(icon.mDrawableResId)
        // Set Icon BackgroundTint
        backgroundColorInt?.let { mIconView.setColorFilter(it, PorterDuff.Mode.SRC_IN) }
        backgroundColorResource?.let { mIconView.setColorFilter(mContext.getColor(it), PorterDuff.Mode.SRC_IN) }
        // Set Title
        if (title != null) {
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        } else mTitleView.visibility = View.GONE
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
            mPositiveButton?.let {
                if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                    ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            }
            mPositiveButtonView.setOnClickListener {
                mPositiveButton?.onClickListener?.onClick(
                    this,
                    MaterialAlertDialog.UI.BUTTON_POSITIVE
                )
            }
        } else mPositiveButtonView.visibility = View.GONE
        // Set Neutral Button
        if (mNeutralButton != null) {
            mNeutralButtonView.visibility = View.VISIBLE
            mNeutralButtonView.text = mNeutralButton?.title
            mNeutralButton?.let {
                if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNeutralButtonView.icon =
                    ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            }
            mNeutralButtonView.setOnClickListener {
                mNeutralButton?.onClickListener?.onClick(
                    this,
                    MaterialAlertDialog.UI.BUTTON_NEUTRAL
                )
            }
        } else mNeutralButtonView.visibility = View.GONE
        // Set Negative Button
        if (mNegativeButton != null) {
            mNegativeButtonView.visibility = View.VISIBLE
            mNegativeButtonView.text = mNegativeButton?.title
            mNegativeButton?.let {
                if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNegativeButtonView.icon =
                    ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            }
            mNegativeButtonView.setOnClickListener {
                mNegativeButton?.onClickListener?.onClick(
                    this,
                    MaterialAlertDialog.UI.BUTTON_NEGATIVE
                )
            }
        } else mNegativeButtonView.visibility = View.GONE
        // Apply Styles
        try {
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set Background Tint
            val mBackgroundTint: ColorStateList = ColorStateList.valueOf(mContext.colorPrimary())
            // Set Positive Button Icon Tint
            val mPositiveButtonTint: ColorStateList = mBackgroundTint
            mPositiveButtonView.setTextColor(mPositiveButtonTint)
            mPositiveButtonView.iconTint = mPositiveButtonTint
            // Set Neutral Button Icon & Text Tint
            val mNeutralButtonTint: ColorStateList = mBackgroundTint
            mNeutralButtonView.setTextColor(mNeutralButtonTint)
            mNeutralButtonView.iconTint = mNeutralButtonTint
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList = mBackgroundTint
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            // Set Button Ripple Color
            mPositiveButtonView.rippleColor = mBackgroundTint.withAlpha(75)
            mNeutralButtonView.rippleColor = mBackgroundTint.withAlpha(75)
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
    abstract class Builder<D : AlertDialogCenteredBase>(protected open val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(R.drawable.ic_information)
        protected open var backgroundColorResource: Int? = null
        protected open var backgroundColorInt: Int? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var isCancelable: Boolean = true
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var neutralButton: ButtonAlertDialog? = null
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
         * Set background tint [ColorInt].
         *
         * @param color Color resource. Eg: [Color.BLUE].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconBackgroundTintRGB(@ColorInt color: Int): Builder<D> {
            this.backgroundColorInt = color
            return this
        }

        /**
         * Set background tint [ColorRes].
         *
         * @param color Color resource. Eg: [R.color.Success].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconBackgroundTint(@ColorRes color: Int): Builder<D> {
            this.backgroundColorResource = color
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogCentered].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogCentered].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogCentered]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            this.title = TitleAlertDialog(title = title, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogCentered]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, MaterialAlertDialog.TextAlignment.CENTER)
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.CENTER)
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
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setPositiveButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setPositiveButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String?,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_accept)
                else buttonText
            positiveButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String?,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNeutralButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNeutralButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String?,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_decline)
                else buttonText
            neutralButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            neutralButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
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
            @DrawableRes icon: Int,
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
            @DrawableRes icon: Int,
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
         * Creates an [MaterialAlertDialogCentered] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}