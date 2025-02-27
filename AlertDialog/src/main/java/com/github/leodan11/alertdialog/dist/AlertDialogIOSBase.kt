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
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.github.leodan11.alertdialog.IOSAlertDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.IosAlertDialogBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.IOSDialog
import com.github.leodan11.alertdialog.io.content.IOSDialog.Orientation
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.context.isNightModeActive
import com.google.android.material.textview.MaterialTextView
import java.util.Locale
import java.util.concurrent.TimeUnit

abstract class AlertDialogIOSBase(
    protected open var mContext: Context,
    protected open var orientationButton: Orientation,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: IosAlertDialogBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @SuppressLint("WrongConstant")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCustomCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = IosAlertDialogBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        val mTitleView = binding.textViewTitleAlertDialog
        val mMessageView = binding.textViewMessageAlertDialog

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
        // Set Button View
        when (orientationButton) {
            Orientation.HORIZONTAL -> {
                with(binding) {
                    layoutContentHorizontalButton.visibility = View.VISIBLE
                    layoutContentVerticalButton.visibility = View.GONE
                    materialDividerBodyFooter.visibility =
                        if (mPositiveButton != null || mNeutralButton != null || mNegativeButton != null) View.VISIBLE else View.GONE
                    // Set Positive Button
                    if (mPositiveButton != null) {
                        buttonActionPositiveAlertDialogHorizontal.let {
                            it.visibility = View.VISIBLE
                            it.text = mPositiveButton?.title
                            it.setOnClickListener {
                                mPositiveButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_POSITIVE
                                )
                            }
                        }
                    } else {
                        buttonActionPositiveAlertDialogHorizontal.visibility = View.GONE
                    }
                    // Set Neutral Button
                    if (mNeutralButton != null) {
                        buttonActionNeutralAlertDialogHorizontal.let {
                            it.visibility = View.VISIBLE
                            it.text = mNeutralButton?.title
                            it.setOnClickListener {
                                mNeutralButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_NEUTRAL
                                )
                            }
                        }
                    } else {
                        buttonActionNeutralAlertDialogHorizontal.visibility = View.GONE
                    }
                    // Set Negative Button
                    if (mNegativeButton != null) {
                        buttonActionNegativeAlertDialogHorizontal.let {
                            it.visibility = View.VISIBLE
                            it.text = mNegativeButton?.title
                            it.setOnClickListener {
                                mNegativeButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_NEGATIVE
                                )
                            }

                        }
                    } else {
                        buttonActionNegativeAlertDialogHorizontal.visibility = View.GONE
                    }
                }
            }

            Orientation.VERTICAL -> {
                with(binding) {
                    layoutContentHorizontalButton.visibility = View.GONE
                    layoutContentVerticalButton.visibility = View.VISIBLE
                    materialDividerBodyFooter.visibility =
                        if (mPositiveButton != null || mNeutralButton != null || mNegativeButton != null) View.VISIBLE else View.GONE
                    // Set Positive Button
                    if (mPositiveButton != null) {
                        buttonActionPositiveAlertDialogVertical.let {
                            it.visibility = View.VISIBLE
                            it.text = mPositiveButton?.title
                            it.setOnClickListener {
                                mPositiveButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_POSITIVE
                                )
                            }
                        }
                    } else {
                        buttonActionPositiveAlertDialogVertical.visibility = View.GONE
                    }
                    // Set Neutral Button
                    if (mNeutralButton != null) {
                        buttonActionNeutralAlertDialogVertical.let {
                            it.visibility = View.VISIBLE
                            it.text = mNeutralButton?.title
                            it.setOnClickListener {
                                mNeutralButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_NEUTRAL
                                )
                            }
                        }
                    } else {
                        buttonActionNeutralAlertDialogVertical.visibility = View.GONE
                    }
                    // Set Negative Button
                    if (mNegativeButton != null) {
                        buttonActionNegativeAlertDialogVertical.let {
                            it.visibility = View.VISIBLE
                            it.text = mNegativeButton?.title
                            it.setOnClickListener {
                                mNegativeButton?.onClickListener?.onClick(
                                    this@AlertDialogIOSBase,
                                    AlertDialog.UI.BUTTON_NEGATIVE
                                )
                            }
                        }
                    } else {
                        buttonActionNegativeAlertDialogVertical.visibility = View.GONE
                    }
                    splitNeutralPositiveVertical.isVisible =
                        buttonActionNeutralAlertDialogVertical.isVisible && buttonActionPositiveAlertDialogVertical.isVisible
                    splitNegativeNeutralVertical.isVisible =
                        buttonActionNeutralAlertDialogVertical.isVisible && buttonActionNegativeAlertDialogVertical.isVisible
                    splitNegativeNeutralVertical.isVisible =
                        buttonActionPositiveAlertDialogVertical.isVisible && buttonActionNegativeAlertDialogVertical.isVisible
                }
            }
        }
        // Apply Styles
        try {
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set Background Tint
            val mBackgroundTint: ColorStateList = ColorStateList.valueOf(
                if (mContext.isNightModeActive()) Color.rgb(10, 132, 255) else Color.rgb(
                    0,
                    122,
                    255
                )
            )
            with(binding) {
                // Set Positive Button Icon Tint
                val mPositiveButtonTint: ColorStateList = mBackgroundTint
                /**
                 * Horizontal
                 */
                buttonActionPositiveAlertDialogHorizontal.setTextColor(mPositiveButtonTint)
                /**
                 * Vertical
                 */
                buttonActionPositiveAlertDialogVertical.setTextColor(mPositiveButtonTint)
                // Set Neutral Button Icon & Text Tint
                val mNeutralButtonTint: ColorStateList = mBackgroundTint
                /**
                 * Horizontal
                 */
                buttonActionNeutralAlertDialogHorizontal.setTextColor(mNeutralButtonTint)
                /**
                 * Vertical
                 */
                buttonActionNeutralAlertDialogVertical.setTextColor(mNeutralButtonTint)
                // Set Negative Button Icon & Text Tint
                val mNegativeButtonTint: ColorStateList = mBackgroundTint
                /**
                 * Horizontal
                 */
                buttonActionNegativeAlertDialogHorizontal.setTextColor(mNegativeButtonTint)
                /**
                 * Vertical
                 */
                buttonActionNegativeAlertDialogVertical.setTextColor(mNegativeButtonTint)
            }
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
     * @return [MaterialTextView]
     *
     * @throws IllegalArgumentException
     *
     */
    @Throws(IllegalArgumentException::class)
    open fun getButton(which: AlertDialog.UI): MaterialTextView {
        return if (orientationButton == Orientation.HORIZONTAL) {
            when (which) {
                AlertDialog.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogHorizontal
                AlertDialog.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogHorizontal
                AlertDialog.UI.BUTTON_NEUTRAL -> binding.buttonActionNeutralAlertDialogHorizontal
            }
        } else when (which) {
            AlertDialog.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogVertical
            AlertDialog.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogVertical
            AlertDialog.UI.BUTTON_NEUTRAL -> binding.buttonActionNeutralAlertDialogVertical
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

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogIOSBase>(protected open val context: Context) {

        protected open var orientationButton: Orientation = Orientation.HORIZONTAL
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var isCancelable: Boolean = true
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var neutralButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null


        /**
         * Set material dialog type. Use the following types
         * [IOSDialog.Orientation.HORIZONTAL], [IOSDialog.Orientation.VERTICAL]
         *
         * @param orientation By default, it is used [IOSDialog.Orientation.HORIZONTAL].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setOrientationButton(orientation: Orientation): Builder<D> {
            this.orientationButton = orientation
            return this
        }

        /**
         * Set the title displayed in the [IOSAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [IOSAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [IOSAlertDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.title = TitleAlertDialog(title = title, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [IOSAlertDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
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
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
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
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
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
         * Set count down timer. Default `1000`
         *
         * @param button [AlertDialog.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
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
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_accept) else buttonText
            positiveButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_decline) else buttonText
            neutralButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            neutralButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
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
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_cancel) else buttonText
            negativeButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
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
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
            return this
        }


        /**
         * Creates an [IOSAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}