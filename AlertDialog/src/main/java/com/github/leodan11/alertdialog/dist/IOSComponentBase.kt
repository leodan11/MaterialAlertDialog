package com.github.leodan11.alertdialog.dist

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
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.IOSDialog
import com.github.leodan11.alertdialog.io.content.IOSDialog.Orientation
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.k_extensions.context.isNightModeActive
import com.google.android.material.textview.MaterialTextView

abstract class IOSComponentBase(
    protected open var mContext: Context,
    protected open var orientationButton: Orientation,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: IosAlertDialogBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: DialogAlertInterface.OnDismissListener? = null
    protected open var mOnCancelListener: DialogAlertInterface.OnCancelListener? = null
    protected open var mOnShowListener: DialogAlertInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCustomCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = IosAlertDialogBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set Title
                title.toTitleView(textViewTitleAlertDialog)
                // Set Message
                message.toMessageView(textViewMessageAlertDialog)
                // Set Content
                layoutContentHorizontalButton.isVisible =
                    orientationButton == Orientation.HORIZONTAL
                layoutContentVerticalButton.isVisible = orientationButton == Orientation.VERTICAL
                // Set Background Tint
                val mBackgroundTint: ColorStateList = ColorStateList.valueOf(
                    if (mContext.isNightModeActive()) Color.rgb(
                        10,
                        132,
                        255
                    ) else Color.rgb(0, 122, 255)
                )
                // Set Buttons
                when (orientationButton) {
                    Orientation.HORIZONTAL -> {
                        materialDividerBodyFooter.isVisible =
                            mNeutralButton != null || mNegativeButton != null || mPositiveButton != null
                        // Set Negative Button
                        mNegativeButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionNegativeAlertDialogHorizontal,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_NEGATIVE
                        )
                        // Set Neutral Button
                        mNeutralButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionNeutralAlertDialogHorizontal,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_NEUTRAL
                        )
                        // Set Positive Button
                        mPositiveButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionPositiveAlertDialogHorizontal,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                    }

                    Orientation.VERTICAL -> {
                        materialDividerBodyFooter.isVisible =
                            mNeutralButton != null || mNegativeButton != null || mPositiveButton != null
                        // Set Negative Button
                        mNegativeButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionNegativeAlertDialogVertical,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_NEGATIVE
                        )
                        // Set Neutral Button
                        mNeutralButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionNeutralAlertDialogVertical,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_NEUTRAL
                        )
                        // Set Positive Button
                        mPositiveButton.toButtonView(
                            this@IOSComponentBase,
                            buttonActionPositiveAlertDialogVertical,
                            mBackgroundTint,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                        // Extra
                        splitNeutralPositiveVertical.isVisible =
                            buttonActionNeutralAlertDialogVertical.isVisible && buttonActionPositiveAlertDialogVertical.isVisible
                        splitNegativeNeutralVertical.isVisible =
                            buttonActionNeutralAlertDialogVertical.isVisible && buttonActionNegativeAlertDialogVertical.isVisible
                        splitNegativeNeutralVertical.isVisible =
                            buttonActionPositiveAlertDialogVertical.isVisible && buttonActionNegativeAlertDialogVertical.isVisible
                    }
                }
                // Set countDownTimer to button
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
     * @return [MaterialTextView]
     *
     * @throws IllegalArgumentException
     *
     */
    @Throws(IllegalArgumentException::class)
    open fun getButton(which: DialogAlertInterface.UI): MaterialTextView {
        return if (orientationButton == Orientation.HORIZONTAL) {
            when (which) {
                DialogAlertInterface.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogHorizontal
                DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogHorizontal
                DialogAlertInterface.UI.BUTTON_NEUTRAL -> binding.buttonActionNeutralAlertDialogHorizontal
            }
        } else when (which) {
            DialogAlertInterface.UI.BUTTON_POSITIVE -> binding.buttonActionPositiveAlertDialogVertical
            DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.buttonActionNegativeAlertDialogVertical
            DialogAlertInterface.UI.BUTTON_NEUTRAL -> binding.buttonActionNeutralAlertDialogVertical
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

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : IOSComponentBase>(protected open val context: Context) :
        AlertBuilder() {

        protected open var orientationButton: Orientation = Orientation.HORIZONTAL
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var isCancelable: Boolean = true
        protected open var gravity: Int? = null
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
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_decline].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setNeutralButton(
                R.string.label_text_decline,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_decline].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(R.string.label_text_decline, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.neutralButton = initNeutralButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.neutralButton = initNeutralButton(context.getString(text), icon, onClickListener)
            return this
        }

        /**
         *  [DialogAlertInterface.OnClickListener]
         */

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setPositiveButton(
                R.string.label_text_accept,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
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
         * - Default button text is [R.string.label_text_accept].
         *
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setPositiveButton(R.string.label_text_accept, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
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
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(context.getString(text), icon, onClickListener)
            return this
        }


        /**
         * Creates an [IOSAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = IOSAlertDialog.Builder(context)
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