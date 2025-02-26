package com.github.leodan11.alertdialog.dist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.ProgressMaterialDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MDialogProgressBarBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.color.backgroundColor
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.color.colorPrimary
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.text.NumberFormat
import java.util.Locale

abstract class ProgressDialogBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var progressType: AlertDialog.Progress,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var detailsLinearProgress: MessageAlertDialog<*>?,
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

    @SuppressLint("WrongConstant")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = MDialogProgressBarBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        val mIconView = binding.imageViewIconProgressIndicator
        val mTitleView = binding.textViewTitleDialogProgressIndicator
        val mHeaderLayout = binding.layoutContentHeaderProgressIndicator
        val mMessageView = when (progressType) {
            AlertDialog.Progress.CIRCULAR -> binding.textViewMessagesDialogCircularProgressIndicator
            else -> binding.textViewMessagesDialogLinearProgressIndicator
        }
        mProgressCircular = binding.circularProgressIndicator
        mProgressLinear = binding.linearProgressIndicator
        mTextViewLinear = binding.textViewProgressDialogLinearIndicator
        val mNegativeButtonView = binding.buttonActionNegativeCircularProgressIndicator

        // Set Icon
        mIconView.isVisible = icon != null
        icon?.let { mIconView.setImageResource(it.mDrawableResId) }
        // Set Title
        if (title != null) {
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        } else mHeaderLayout.visibility = View.GONE
        // Set Message
        if (message != null) {
            mMessageView.visibility = View.VISIBLE
            mMessageView.text = message?.getText()
            mMessageView.textAlignment = message?.textAlignment!!.alignment
        } else mMessageView.visibility = View.GONE
        // Set Max
        if (mMax > 0) {
            if (progressType === AlertDialog.Progress.CIRCULAR) mProgressCircular.max = mMax
            else mProgressLinear.max = mMax
        }
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
            // Set Dialog Background
            binding.root.setBackgroundColor(mContext.backgroundColor())
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
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set Content
            when (progressType) {
                AlertDialog.Progress.CIRCULAR -> {
                    // set Indeterminate Progress Circular & Tint
                    mProgressCircular.isIndeterminate = mIndeterminate
                    mProgressCircular.setIndicatorColor(mContext.colorPrimary())
                    binding.layoutContentBodyCircularProgressIndicator.visibility = View.VISIBLE
                    binding.layoutContentBodyLinearProgressIndicator.visibility = View.GONE
                }

                else -> {
                    // set Indeterminate Progress Linear & Tint
                    mProgressLinear.isIndeterminate = mIndeterminate
                    binding.textViewProgressDialogLinearIndicator.isVisible = !mIndeterminate
                    binding.textViewNumberDialogLinearIndicator.isVisible =
                        !mIndeterminate || detailsLinearProgress != null
                    if (!mIndeterminate) {
                        mViewUpdateHandler = Handler(mContext.mainLooper) {
                            val progress = mProgressLinear.progress
                            val max = mProgressLinear.max
                            binding.textViewNumberDialogLinearIndicator.text = String.format(
                                Locale.getDefault(),
                                mProgressNumberFormat,
                                progress,
                                max
                            )
                            val percent = progress.toDouble() / max.toDouble()
                            val tmp = SpannableString(mProgressPercentFormat.format(percent))
                            tmp.setSpan(
                                StyleSpan(Typeface.BOLD),
                                0,
                                tmp.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            binding.textViewProgressDialogLinearIndicator.text = tmp
                            true
                        }
                    }
                    detailsLinearProgress?.let {
                        binding.textViewNumberDialogLinearIndicator.text = it.getText()
                    }
                    mProgressLinear.setIndicatorColor(mContext.colorPrimary())
                    binding.layoutContentBodyCircularProgressIndicator.visibility = View.GONE
                    binding.layoutContentBodyLinearProgressIndicator.visibility = View.VISIBLE
                }
            }
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList =
                ColorStateList.valueOf(mContext.colorPrimary())
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            onProgressChanged()
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
     * @param context – the parent context
     */
    abstract class Builder<D : ProgressDialogBase>(protected open val context: Context) {

        protected open var icon: IconAlertDialog? = null
        protected open var tintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var detailsLinearProgress: MessageAlertDialog<*>? = null
        protected open var progressType: AlertDialog.Progress = AlertDialog.Progress.CIRCULAR
        protected open var max: Int = 0
        protected open var isCancelable: Boolean = true
        protected open var isIndeterminate: Boolean = false
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
         * Set the title displayed in the [ProgressMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            val valueText = title.ifEmpty { context.getString(R.string.label_text_information) }
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
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
         * Sets the maximum allowed progress value.
         */
        fun setMax(max: Int): Builder<D> {
            this.max = max
            return this
        }

        /**
         * Sets the details to display.
         *
         * @param details The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsLinearProgress(details: String): Builder<D> {
            this.detailsLinearProgress = MessageAlertDialog.text(
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
            this.detailsLinearProgress = MessageAlertDialog.text(
                text = context.getString(details),
                alignment = AlertDialog.TextAlignment.END
            )
            return this
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            val valueText = message.ifEmpty { context.getString(R.string.label_text_charging) }
            this.message = MessageAlertDialog.text(text = valueText, alignment = alignment)
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
         * @param isIndeterminate Sets Indeterminable property of Material Dialog.
         * @return this, for chaining.
         */
        fun setIndeterminable(isIndeterminate: Boolean): Builder<D> {
            this.isIndeterminate = isIndeterminate
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
         * Creates an [ProgressMaterialDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}