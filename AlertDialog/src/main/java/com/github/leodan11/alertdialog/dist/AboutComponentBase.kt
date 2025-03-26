package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
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
import com.github.leodan11.alertdialog.AboutAlertDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAboutDialogBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_CHART_SEQUENCE_LENGTH
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_CHART_SEQUENCE_LENGTH_DETAILS
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toDetailsView
import com.github.leodan11.alertdialog.io.helpers.toImageView
import com.github.leodan11.alertdialog.io.helpers.toMessageView
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.DetailsAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.k_extensions.color.colorPrimary
import com.google.android.material.button.MaterialButton

abstract class AboutComponentBase protected constructor(
    protected open var mContext: Context,
    protected open var icon: IconAlert,
    protected open var iconStore: IconAlert,
    protected open var backgroundIconTintColor: IconTintAlert?,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var span: DetailsAlert<*>?,
    protected open var details: DetailsAlert<*>?,
    protected open var maxLength: Int,
    protected open var maxLengthDetails: Int,
    protected open var mCancelable: Boolean,
    protected open var mIconStore: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MAboutDialogBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: DialogAlertInterface.OnDismissListener? = null
    protected open var mOnCancelListener: DialogAlertInterface.OnCancelListener? = null
    protected open var mOnShowListener: DialogAlertInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCreateViewDialogContent(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = MAboutDialogBinding.inflate(layoutInflater, container, false)
        try {
            with(binding) {
                // Set Icon
                icon.toImageView(imageViewApplicationIcon, backgroundIconTintColor)
                iconStore.toImageView(imageViewApplicationVersion)
                imageViewApplicationVersion.isVisible = mIconStore
                // Set Background Tint
                val mBackgroundTint: ColorStateList =
                    ColorStateList.valueOf(mContext.colorPrimary())
                // Set Title
                title.toTitleView(textViewApplicationName)
                // Set Message
                message.toMessageView(textViewApplicationVersion)
                // Set Span
                span.toDetailsView(
                    mContext,
                    textViewApplicationLegalese,
                    maxLength
                )
                // Set Details
                details.toDetailsView(
                    mContext,
                    nestedScrollViewContainerDetails,
                    textViewDetailsAlertDialog, maxLengthDetails
                )
                // Set Positive Button
                aboutActions.buttonActionPositiveAlertDialog.apply {
                    mPositiveButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mPositiveButton?.onClickListener?.onClick(
                            this@AboutComponentBase,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                    }
                }
                // Set Neutral Button
                aboutActions.buttonActionNeutralAlertDialog.apply {
                    mNeutralButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNeutralButton?.onClickListener?.onClick(
                            this@AboutComponentBase,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                    }
                }
                // Set Negative Button
                aboutActions.buttonActionNegativeAlertDialog.apply {
                    mNegativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNegativeButton?.onClickListener?.onClick(
                            this@AboutComponentBase,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                    }
                }
                // Set CountDownTimer to button
                countDownTimer?.let { timer ->
                    val button = getButton(timer.button)
                    mCountDownTimer = timer.toButtonView(button)
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
    open fun getButton(which: DialogAlertInterface.UI): MaterialButton {
        return when (which) {
            DialogAlertInterface.UI.BUTTON_POSITIVE -> binding.aboutActions.buttonActionPositiveAlertDialog
            DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.aboutActions.buttonActionNegativeAlertDialog
            DialogAlertInterface.UI.BUTTON_NEUTRAL -> binding.aboutActions.buttonActionNeutralAlertDialog
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
     * @return The PopupDialog instance.
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
    abstract class Builder<D : AboutComponentBase>(protected val context: Context) : AlertBuilder() {

        protected open var icon: IconAlert = IconAlert(context.applicationInfo.icon)
        protected open var iconStore: IconAlert = IconAlert(R.drawable.ic_baseline_git_svg)
        protected open var backgroundIconTintColor: IconTintAlert? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var span: DetailsAlert<*>? = null
        protected open var details: DetailsAlert<*>? = null
        protected open var maxLength: Int = DEFAULT_CHART_SEQUENCE_LENGTH
        protected open var maxLengthDetails: Int = DEFAULT_CHART_SEQUENCE_LENGTH_DETAILS
        protected open var isCancelable: Boolean = true
        protected open var gravity: Int? = null
        protected open var isIconStore: Boolean = true
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var neutralButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlert(icon)
            return this
        }

        /**
         * Set the [DrawableRes] to be used in the version.
         *
         * @param icon Drawable to use as the store.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationIconStore(@DrawableRes icon: Int): Builder<D> {
            this.iconStore = IconAlert(icon)
            return this
        }

        /**
         * Set icon tint of [ColorInt].
         *
         * @param tintColor The color int. E.g. [Color.BLUE]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@ColorInt tintColor: Int): Builder<D> {
            this.backgroundIconTintColor = IconTintAlert().apply { tintColorInt = tintColor }
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
            this.backgroundIconTintColor =
                IconTintAlert().apply { tintColorInt = Color.rgb(red, green, blue) }
            return this
        }

        /**
         * Set icon tint of [ColorRes].
         *
         * @param tintColor The color resource.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconTintColorRes(@ColorRes tintColor: Int): Builder<D> {
            this.backgroundIconTintColor = IconTintAlert(tintColor)
            return this
        }

        /**
         * Set the application name displayed in the [AboutAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(title: String): Builder<D> {
            return setApplicationName(title, Alert.TextAlignment.START)
        }

        /**
         * Set the application name displayed in the [AboutAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(@StringRes title: Int): Builder<D> {
            return setApplicationName(title, Alert.TextAlignment.START)
        }

        /**
         * Set the application name displayed in the [AboutAlertDialog]. With text alignment.
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(title: String, alignment: Alert.TextAlignment): Builder<D> {
            this.title = TitleAlert(title, alignment)
            return this
        }

        /**
         * Set the application name displayed in the [AboutAlertDialog]. With text alignment.
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(
            @StringRes title: Int,
            alignment: Alert.TextAlignment
        ): Builder<D> {
            this.title = TitleAlert(context.getString(title), alignment)
            return this
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(message: String): Builder<D> {
            return setApplicationVersion(message, Alert.TextAlignment.START)
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(@StringRes message: Int): Builder<D> {
            return setApplicationVersion(message, Alert.TextAlignment.START)
        }

        /**
         * Sets the application version to display. With text alignment.
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            message: String,
            alignment: Alert.TextAlignment
        ): Builder<D> {
            this.message = MessageAlert.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the application version to display. With text alignment.
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            @StringRes message: Int,
            alignment: Alert.TextAlignment
        ): Builder<D> {
            this.message =
                MessageAlert.text(text = context.getString(message), alignment = alignment)
            return this
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(message: Spanned): Builder<D> {
            return setApplicationVersion(message, Alert.TextAlignment.START)
        }

        /**
         * Sets the application version to display. With text alignment.
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            message: Spanned,
            alignment: Alert.TextAlignment,
        ): Builder<D> {
            this.message = MessageAlert.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(detail: String): Builder<D> {
            this.span = DetailsAlert.text(text = detail)
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(@StringRes detail: Int): Builder<D> {
            this.span = DetailsAlert.text(text = context.getString(detail))
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(detail: Spanned): Builder<D> {
            this.span = DetailsAlert.spanned(text = detail)
            return this
        }


        /**
         * Set the maximum length of the application legalese to display.
         *  - Then the option to [ReadMoreOption] is added.
         *
         * @param maxLength The maximum length.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMaxLength(maxLength: Int): Builder<D> {
            this.maxLength = maxLength
            return this
        }

        /**
         * Set the maximum length of the application details to display.
         *  - Then the option to [ReadMoreOption] is added.
         *
         * @param maxLength The maximum length.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMaxLengthDetails(maxLength: Int): Builder<D> {
            this.maxLengthDetails = maxLength
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(details: String): Builder<D> {
            this.details = DetailsAlert.text(text = details)
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(@StringRes details: Int): Builder<D> {
            this.details = DetailsAlert.text(text = context.getString(details))
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(details: Spanned): Builder<D> {
            this.details = DetailsAlert.spanned(text = details)
            return this
        }

        /**
         * Set count down timer. Default `1000`
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(button: DialogAlertInterface.UI, millis: Long): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis)
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
         * Sets whether the dialog shows store icon or not.
         *
         * @param isVisible is [Boolean] value. Default is true.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconStoreVisible(isVisible: Boolean): Builder<D> {
            this.isIconStore = isVisible
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
         * Creates an [AboutAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = AboutMaterialDialog.Builder(context)
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