package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
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
import com.github.leodan11.alertdialog.MaterialAlertDialogAnimatedDrawable
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MDialogProgressBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toImageView
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.view.startAnimatedVectorDrawable
import com.github.leodan11.k_extensions.view.startAnimatedVectorDrawableLoop
import com.google.android.material.button.MaterialButton

abstract class AnimatedVectorDrawableComponentBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert?,
    protected open var tintColor: IconTintAlert?,
    protected open var iconVectorDrawable: IconAlert,
    protected open var mAnimatedVectorDrawable: Boolean,
    protected open var mAnimatedVectorDrawableLoop: Boolean,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var mCancelable: Boolean,
    protected open var mNegativeButton: ButtonAlertDialog?,
    protected open var mTimeout: Long?
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MDialogProgressBinding
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
        binding = MDialogProgressBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set header layout
                layoutContentHeaderDialogProgress.isVisible = title != null || icon != null
                // Set icon
                icon.toImageView(imageViewIconProgressIndicator, tintColor)
                // Set icon vector animation
                imageViewIconLogoDialogProgress.apply {
                    iconVectorDrawable.toImageView(this)
                    if (mAnimatedVectorDrawable) {
                        if (mAnimatedVectorDrawableLoop) startAnimatedVectorDrawableLoop()
                        else startAnimatedVectorDrawable()
                    }
                }
                // Set Title
                title.toTitleView(textViewTitleDialogProgress)
                // Set Message
                message.toMessageView(textViewMessagesDialogProgress)
                // Set Background Tint
                val mBackgroundTint: ColorStateList =
                    ColorStateList.valueOf(mContext.colorPrimary())
                // Set Timeout
                mTimeout?.let { setTimeout(it) }
                // Set Negative Button
                buttonActionNegativeCircularProgressIndicator.apply {
                    mNegativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNegativeButton?.onClickListener?.onClick(
                            this@AnimatedVectorDrawableComponentBase,
                            DialogAlertInterface.UI.BUTTON_NEGATIVE
                        )
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
    open fun show() {
        if (mDialog != null) mDialog?.show()
        else throwNullDialog()
    }


    /**
     * Get the button with the specified type.
     *
     * @return [MaterialButton]
     *
     * @throws IllegalArgumentException
     *
     */
    @Throws(IllegalArgumentException::class)
    open fun getButton(which: DialogAlertInterface.UI): MaterialButton {
        return when (which) {
            DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeCircularProgressIndicator
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

    /**
     * Sets a timeout for the dialog to automatically dismiss after a specified duration.
     *
     * @param milliseconds The duration in milliseconds before the dialog is automatically dismissed.
     *
     */
    open fun setTimeout(milliseconds: Long) {
        Handler(mContext.mainLooper).postDelayed({
            if (mDialog?.isShowing == true) mDialog?.dismiss()
        }, milliseconds)
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
    abstract class Builder<D : AnimatedVectorDrawableComponentBase>(protected open val context: Context) :
        AlertBuilder() {

        protected open var icon: IconAlert? = null
        protected open var tintColor: IconTintAlert? = null
        protected open var iconVectorDrawable: IconAlert =
            IconAlert(R.drawable.ic_baseline_animated_search_to_close)
        protected open var isAnimatedVectorDrawable: Boolean = true
        protected open var isAnimatedVectorDrawableLoop: Boolean = false
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var isCancelable: Boolean = true
        protected open var onTimeout: Long? = null
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
         * Set animated vector [DrawableRes] to be used as progress.
         *
         * @param icon Drawable to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconVectorDrawable(@DrawableRes icon: Int): Builder<D> {
            this.iconVectorDrawable = IconAlert(icon)
            return this
        }


        /**
         * Set animated vector [DrawableRes] to be used as progress.
         *
         * @param isAnimated value [Boolean]. Default value true.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimatedVectorDrawable(isAnimated: Boolean): Builder<D> {
            this.isAnimatedVectorDrawable = isAnimated
            return this
        }

        /**
         * Set animated vector loop [DrawableRes] to be used as progress.
         *
         * @param isAnimatedLoop value [Boolean]. Default value false.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setLoopAnimatedVectorDrawable(isAnimatedLoop: Boolean): Builder<D> {
            this.isAnimatedVectorDrawableLoop = isAnimatedLoop
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
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is true.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Sets the timeout for the dialog.
         *
         * @param milliseconds the timeout in milliseconds
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTimeout(milliseconds: Long): Builder<D> {
            onTimeout = milliseconds
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
         * Creates an [MaterialAlertDialogAnimatedDrawable] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = MaterialAlertDialogProgress.Builder(context)
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