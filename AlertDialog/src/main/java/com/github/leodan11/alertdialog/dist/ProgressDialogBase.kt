package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.ProgressMaterialDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MDialogProgressBarBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.text.NumberFormat
import java.util.Locale

abstract class ProgressDialogBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert?,
    protected open var tintColor: IconTintAlert?,
    protected open var progressType: AlertDialog.Progress,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var detailsLinearProgress: MessageAlert<*>?,
    protected open var mCancelable: Boolean,
    protected open var mIndeterminate: Boolean,
    protected open var mMax: Int,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MDialogProgressBarBinding
    private val mProgressPercentFormat: NumberFormat by lazy {
        NumberFormat.getPercentInstance().apply {
            maximumFractionDigits = 0
        }
    }
    private var mViewUpdateHandler: Handler? = null
    private val mProgressNumberFormat: String = "%1d/%2d"
    protected open var mDialog: Dialog? = null
    protected open lateinit var mProgressCircular: CircularProgressIndicator
    protected open lateinit var mProgressLinear: LinearProgressIndicator
    protected open lateinit var mTextViewLinear: TextView
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
        binding = MDialogProgressBarBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        mProgressCircular = binding.circularProgressIndicator
        mProgressLinear = binding.linearProgressIndicator
        mTextViewLinear = binding.textViewNumberDialogLinearIndicator
        try {
            with(binding) {
                // Set header layout
                layoutContentHeaderProgressIndicator.isVisible = title != null || icon != null
                // Set icon
                icon.toImageView(imageViewIconProgressIndicator, tintColor)
                // Set Title
                title.toTitleView(textViewTitleDialogProgressIndicator)
                // Set header
                layoutContentHeaderProgressIndicator.isVisible = icon != null || title != null
                // Set content
                layoutContentBodyCircularProgressIndicator.isVisible =
                    progressType == AlertDialog.Progress.CIRCULAR
                layoutContentBodyLinearProgressIndicator.isVisible =
                    progressType == AlertDialog.Progress.LINEAR
                // Set Background Tint
                val mBackgroundTint: ColorStateList =
                    ColorStateList.valueOf(mContext.colorPrimary())
                // Set type
                when (progressType) {
                    AlertDialog.Progress.CIRCULAR -> {
                        // Set Message
                        message.toMessageView(textViewMessagesDialogCircularProgressIndicator)
                        // Set Progress
                        mProgressCircular.apply {
                            setIndicatorColor(mContext.colorPrimary())
                            isIndeterminate = mIndeterminate
                        }
                    }

                    AlertDialog.Progress.LINEAR -> {
                        // Set Message
                        message.toMessageView(textViewMessagesDialogLinearProgressIndicator)
                        // Set Progress
                        mProgressLinear.apply {
                            if (mMax != 0) {
                                this@ProgressDialogBase.setMax(mMax)
                            }
                            setIndicatorColor(mContext.colorPrimary())
                            isIndeterminate = mIndeterminate
                            textViewProgressDialogLinearIndicator.isVisible = !mIndeterminate
                            textViewNumberDialogLinearIndicator.isVisible =
                                !mIndeterminate || detailsLinearProgress != null
                            if (!mIndeterminate) {
                                mViewUpdateHandler = Handler(mContext.mainLooper) {
                                    val progress = mProgressLinear.progress
                                    val max = mProgressLinear.max
                                    textViewNumberDialogLinearIndicator.text = String.format(
                                        Locale.getDefault(),
                                        mProgressNumberFormat,
                                        progress,
                                        max
                                    )
                                    val percent = progress.toDouble() / max.toDouble()
                                    val tmp =
                                        SpannableString(mProgressPercentFormat.format(percent))
                                    tmp.setSpan(
                                        StyleSpan(Typeface.BOLD),
                                        0,
                                        tmp.length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                    textViewProgressDialogLinearIndicator.text = tmp
                                    true
                                }
                            }
                            detailsLinearProgress?.let {
                                textViewNumberDialogLinearIndicator.text = it.getText()
                            }
                        }
                    }
                }
                // Set Negative Button
                buttonActionNegativeCircularProgressIndicator.apply {
                    mNegativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNegativeButton?.onClickListener?.onClick(
                            this@ProgressDialogBase,
                            AlertDialog.UI.BUTTON_NEGATIVE
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
            AlertDialog.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeCircularProgressIndicator
            else -> throw IllegalArgumentException("Button type not supported")
        }
    }

    /**
     * Gets the maximum allowed progress value. The default value is 100.
     *
     * @return the maximum value
     */
    open fun getMax(): Int {
        return if (progressType === AlertDialog.Progress.CIRCULAR) mProgressCircular.max
        else mProgressLinear.max
    }

    /**
     * Gets the current progress.
     *
     * @return the current progress, a value between 0 and max.
     */
    open fun getProgress(): Int {
        return if (progressType === AlertDialog.Progress.CIRCULAR) mProgressCircular.progress
        else mProgressLinear.progress
    }

    /**
     * Sets the maximum allowed progress value.
     *
     * @param max the maximum value
     */
    open fun setMax(max: Int) {
        if (progressType === AlertDialog.Progress.CIRCULAR) mProgressCircular.max = max
        else mProgressLinear.max = max
        onProgressChanged()
    }

    /**
     * Sets the current progress.
     *
     * @param progress the current progress, a value between 0 and 100.
     * @param animated the animated activated, a value [Boolean].
     */
    open fun setProgress(progress: Int, animated: Boolean = true) {
        if (!mProgressCircular.isIndeterminate && progressType === AlertDialog.Progress.CIRCULAR) {
            mProgressCircular.setProgressCompat(progress, animated)
        } else if (!mProgressLinear.isIndeterminate && progressType === AlertDialog.Progress.LINEAR) {
            mProgressLinear.setProgressCompat(progress, animated)
        }
        onProgressChanged()
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

    private fun onProgressChanged() {
        if (progressType === AlertDialog.Progress.LINEAR) {
            if (mViewUpdateHandler != null && !mViewUpdateHandler!!.hasMessages(0)) {
                mViewUpdateHandler!!.sendEmptyMessage(0)
            }
        }
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
     *
     * @param context – the parent context
     *
     */
    abstract class Builder<D>(protected open val context: Context) : AlertBuilder() {

        protected open var icon: IconAlert? = null
        protected open var iconTint: IconTintAlert? = null
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var detailsLinearProgress: MessageAlert<*>? = null
        protected open var progressType: AlertDialog.Progress = AlertDialog.Progress.CIRCULAR
        protected open var isCancelable: Boolean = true
        protected open var max: Int = 0
        protected open var isIndeterminate: Boolean = false
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
            this.iconTint = IconTintAlert(tint)
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
            this.iconTint = IconTintAlert().apply { tintColorInt = tint }
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
            this.iconTint = IconTintAlert().apply { tintColorInt = Color.rgb(red, green, blue) }
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
            this.iconTint = IconTintAlert(tintColor)
            return this
        }

        /**
         * Set style progress [AlertDialog.Progress.CIRCULAR], [AlertDialog.Progress.LINEAR]
         *
         * @param style [AlertDialog.Progress]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setStyleProgress(style: AlertDialog.Progress): Builder<D> {
            this.progressType = style
            return this
        }

        /**
         * Sets the maximum allowed progress value.
         */
        fun setMax(max: Int): Builder<D> {
            this.max = max
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
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
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
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
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
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
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
         * Sets the details to display.
         *
         * @param details The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsLinearProgress(details: String): Builder<D> {
            this.detailsLinearProgress = MessageAlert.text(
                text = details,
                alignment = AlertDialog.TextAlignment.END
            )
            return this
        }

        /**
         * Sets the details to display.
         *
         * @param details The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsLinearProgress(@StringRes details: Int): Builder<D> {
            this.detailsLinearProgress = MessageAlert.text(
                text = context.getString(details),
                alignment = AlertDialog.TextAlignment.END
            )
            return this
        }

        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is true.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Sets Indeterminable property of Material Dialog.
         *
         * @param isIndeterminate Sets Indeterminable property of Material Dialog.
         *
         * @return this, for chaining.
         *
         */
        fun setIndeterminable(isIndeterminate: Boolean): Builder<D> {
            this.isIndeterminate = isIndeterminate
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(onClickListener: MaterialDialogInterface.OnClickListener): Builder<D> {
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
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            onClickListener: MaterialDialogInterface.OnClickListener
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
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            icon: ButtonIconAlert,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(R.string.label_text_cancel, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            onClickListener: MaterialDialogInterface.OnClickListener
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
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(context.getString(text), icon, onClickListener)
            return this
        }

        /**
         * Creates an [ProgressMaterialDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = ProgressMaterialDialog.Builder(context)
         *     ...
         *     .create()
         * dialog.show()
         *
         * ```
         *
         * @see [show]
         *
         * @return [D] object to allow for chaining of calls to set methods
         *
         */
        abstract fun create(): D

    }

}