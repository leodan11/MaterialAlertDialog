package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.core.widget.addTextChangedListener
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.config.Init.DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
import com.github.leodan11.alertdialog.config.Init.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.databinding.MAlertDialogBinding
import com.github.leodan11.alertdialog.io.content.AlertDialogEvents.Type
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultErrorTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultOnSurfaceTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultPrimaryTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultSecondaryTheme
import com.github.leodan11.alertdialog.io.helpers.Functions.onTextViewTextSize
import com.github.leodan11.alertdialog.io.models.*
import com.leodan.readmoreoption.ReadMoreOption

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class AlertDialogEventsBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var type: Type,
    protected open var backgroundColorSpanInt: Int?,
    protected open var backgroundColorSpanResource: Int?,
    protected open var detailsScrollHeightSpan: Int,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var details: DetailsAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val binding: MAlertDialogBinding = MAlertDialogBinding.inflate(layoutInflater)
        // Initialize Views
        val mIconView = binding.imageViewIconAlertDialog
        val mTitleView = binding.textViewTitleAlertDialog
        val mMessageView = binding.textViewMessageAlertDialog
        val mDetailsViewContainer = binding.nestedScrollViewContainerDetails
        val mDetailsView = binding.textViewDetailsAlertDialog
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialog
        val mNeutralButtonView = binding.buttonActionNeutralAlertDialog
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialog

        // Set Icon
        when (type) {
            Type.ERROR -> mIconView.setImageResource(R.drawable.ic_error)
            Type.HELP -> mIconView.setImageResource(R.drawable.ic_help)
            Type.INFORMATION -> mIconView.setImageResource(R.drawable.ic_information)
            Type.SUCCESS -> mIconView.setImageResource(R.drawable.ic_success)
            Type.WARNING -> mIconView.setImageResource(R.drawable.ic_warning)
            else -> mIconView.setImageResource(icon.mDrawableResId)
        }
        // Set Icon BackgroundTint
        binding.cardViewContainerHeader.setCardBackgroundColor(
            when (type) {
                Type.ERROR -> mContext.getColorDefaultErrorTheme()
                Type.HELP -> mContext.getColorDefaultSecondaryTheme()
                Type.INFORMATION -> mContext.getColorDefaultPrimaryTheme()
                Type.SUCCESS -> mContext.getColor(R.color.Success)
                Type.WARNING -> mContext.getColor(R.color.Warning)
                else -> {
                    if (backgroundColorSpanInt != null) backgroundColorSpanInt!!
                    else if (backgroundColorSpanResource != null) mContext.getColor(backgroundColorSpanResource!!) else mContext.getColorDefaultSecondaryTheme()
                }
            }
        )
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
        // Set Details
        details?.let {
            val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(mContext.applicationContext)
                .textLength(4)
                .textLengthType(ReadMoreOption.TYPE_LINE)
                .moreLabelColor(mContext.getColorDefaultPrimaryTheme())
                .lessLabelColor(mContext.getColorDefaultPrimaryTheme())
                .labelUnderLine(true)
                .expandAnimation(true)
                .build()
            readMoreOption.addReadMoreTo(mDetailsView, it.getText().toString())
            mDetailsView.addTextChangedListener { editable ->
                editable?.let {
                    val bounds = onTextViewTextSize(mDetailsView, editable.toString())
                    mDetailsViewContainer.apply {
                        layoutParams.height =
                            if (bounds.width() > 6000) detailsScrollHeightSpan else ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
            mDetailsViewContainer.visibility = View.VISIBLE
        }
        // Set Positive Button
        if (mPositiveButton != null) {
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
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
            if (mNeutralButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNeutralButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mNeutralButton?.icon!!)
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
            // Set Title Text Color
            mTitleView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // Set Details Text Color
            mDetailsView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // Set Background Tint
            val mBackgroundTint: ColorStateList = ColorStateList.valueOf(mContext.getColorDefaultPrimaryTheme())
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

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : AlertDialogEventsBase>(protected open val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(R.drawable.ic_help)
        protected open var backgroundColorSpanInt: Int? = null
        protected open var backgroundColorSpan: Int? = null
        protected open var detailsScrollHeightSpan: Int = DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
        protected open var type: Type = Type.CUSTOM
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var details: DetailsAlertDialog<*>? = null
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
         * Set material dialog type. Use the following types
         * [Type.CUSTOM], [Type.ERROR], [Type.HELP],
         * [Type.INFORMATION], [Type.SUCCESS], [Type.WARNING].
         *
         * @param dialogType By default it is used [Type.CUSTOM].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setType(dialogType: Type): Builder<D> {
            this.type = dialogType
            return this
        }

        /**
         * Set background [ColorInt].
         *
         * @param color Color resource. Eg: [Color.GREEN].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpanRGB(@ColorInt color: Int): Builder<D> {
            this.backgroundColorSpanInt = color
            return this
        }

        /**
         * Set background [ColorRes].
         *
         * @param color Color resource. Eg: [R.color.Success].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpan(@ColorRes color: Int): Builder<D> {
            this.backgroundColorSpan = color
            return this
        }

        /**
         * Set the maximum scroll size. Default 400.
         *
         * @param heightSpan height.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetailsScrollHeightSpan(heightSpan: Int): Builder<D> {
            this.detailsScrollHeightSpan = heightSpan
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
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
         * Set the title displayed in the [MaterialAlertDialogEvents]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
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
        fun setMessage(@StringRes message: Int, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
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
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: String): Builder<D> {
            this.details = DetailsAlertDialog.text(text = detail)
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetails(@StringRes detail: Int): Builder<D> {
            this.details = DetailsAlertDialog.text(text = context.getString(detail))
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: Spanned): Builder<D> {
            this.details = DetailsAlertDialog.spanned(text = detail)
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
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
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }


        /**
         * Creates an [MaterialAlertDialogEvents] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}