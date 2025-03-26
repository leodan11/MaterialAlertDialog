package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Spanned
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import com.github.leodan11.alertdialog.MaterialDialogAnimatedDrawable
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MDialogProgressSmallBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.helpers.toImageView
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.k_extensions.view.startAnimatedVectorDrawable
import com.github.leodan11.k_extensions.view.startAnimatedVectorDrawableLoop

abstract class AnimatedDrawableComponentBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert,
    protected open var mAnimatedVectorDrawable: Boolean,
    protected open var mAnimatedVectorDrawableLoop: Boolean,
    protected open var message: MessageAlert<*>?,
    protected open var mCancelable: Boolean,
    protected open var mTextSize: Float?,
    protected open var mTimeout: Long?
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
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
        val binding: MDialogProgressSmallBinding =
            MDialogProgressSmallBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set icon
                imageViewIconLogoDialogProgress.apply {
                    icon.toImageView(this)
                    if (mAnimatedVectorDrawable) {
                        if (mAnimatedVectorDrawableLoop) startAnimatedVectorDrawableLoop()
                        else startAnimatedVectorDrawable()
                    }
                }
                // Set Message
                textViewMessagesDialogProgress.apply {
                    message.toMessageView(this)
                    mTextSize?.let {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
                    }
                }
                // Set Timeout
                mTimeout?.let { setTimeout(it) }
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
    abstract class Builder<D : AnimatedDrawableComponentBase>(protected open val context: Context) :
        AlertBuilder() {

        protected open var icon: IconAlert =
            IconAlert(R.drawable.ic_baseline_animated_morphing_animals)
        protected open var isAnimatedVectorDrawable: Boolean = true
        protected open var isAnimatedVectorDrawableLoop: Boolean = false
        protected open var message: MessageAlert<*>? = null
        protected open var messageTextSize: Float? = null
        protected open var isCancelable: Boolean = true
        protected open var gravity: Int? = null
        protected open var onTimeout: Long? = null

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
         * Set the text size of the message.
         *
         * @param size
         *
         * @see [TypedValue.COMPLEX_UNIT_SP]
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessageTextSize(size: Float): Builder<D> {
            this.messageTextSize = size
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
         * Creates an [MaterialDialogAnimatedDrawable] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}