package com.github.leodan11.alertdialog.dist

import android.annotation.SuppressLint
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
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.github.leodan11.alertdialog.AboutMaterialDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAboutDialogBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.DetailsAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.view.onTextViewTextSize
import com.leodan.readmoreoption.ReadMoreOption

abstract class AboutDialogBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var backgroundIconTintColor: IconTintAlertDialog?,
    protected open var detailsScrollHeightSpan: Int,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var span: DetailsAlertDialog<*>?,
    protected open var details: DetailsAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @SuppressLint("WrongConstant")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCreateViewDialogContent(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        val binding: MAboutDialogBinding =
            MAboutDialogBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        val mIconView = binding.imageViewApplicationIcon
        val mTitleView = binding.textViewApplicationName
        val mMessageView = binding.textViewApplicationVersion
        val mSpanView = binding.textViewApplicationLegalese
        val mDetailsViewContainer = binding.nestedScrollViewContainerDetails
        val mDetailsView = binding.textViewDetailsAlertDialog
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialog
        val mNeutralButtonView = binding.buttonActionNeutralAlertDialog
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialog

        // Set Icon
        mIconView.setImageResource(icon.mDrawableResId)

        // Set Icon Tint
        backgroundIconTintColor?.let {
            if (it.iconColorRes != null) mIconView.setColorFilter(
                ContextCompat.getColor(
                    mContext.applicationContext,
                    it.iconColorRes!!
                )
            )
            else if (it.iconColorInt != null) mIconView.setColorFilter(it.iconColorInt!!)
        }

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

        // Set Span
        if (span != null) {
            mSpanView.visibility = View.VISIBLE
            mSpanView.text = span?.getText()
        } else mSpanView.visibility = View.GONE

        // Set Details
        if (details != null) {
            val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(mContext.applicationContext)
                .textLength(4)
                .textLengthType(ReadMoreOption.TYPE_LINE)
                .moreLabelColor(mContext.colorPrimary())
                .lessLabelColor(mContext.colorPrimary())
                .labelUnderLine(true)
                .expandAnimation(true)
                .build()
            readMoreOption.addReadMoreTo(mDetailsView, details?.getText().toString())
            mDetailsView.addTextChangedListener {
                it?.let {
                    val bounds = mDetailsView.onTextViewTextSize(it.toString())
                    mDetailsViewContainer.apply {
                        layoutParams.height =
                            if (bounds.width() > 6000) detailsScrollHeightSpan else ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
            mDetailsViewContainer.visibility = View.VISIBLE
        } else mDetailsViewContainer.visibility = View.GONE

        // Set Positive Button
        if (mPositiveButton != null) {
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
            mPositiveButtonView.setOnClickListener {
                mPositiveButton?.onClickListener?.onClick(
                    this,
                    AlertDialog.UI.BUTTON_POSITIVE
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
                    AlertDialog.UI.BUTTON_NEUTRAL
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
                    AlertDialog.UI.BUTTON_NEGATIVE
                )
            }
        } else mNegativeButtonView.visibility = View.GONE

        // Apply Styles
        try {
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set Details Text Color
            mSpanView.setTextColor(mContext.colorOnSurface())
            mDetailsView.setTextColor(mContext.colorOnSurface())
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
    abstract class Builder<D : AboutDialogBase>(protected val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(context.applicationInfo.icon)
        protected open var backgroundIconTintColor: IconTintAlertDialog? = null
        protected open var detailsScrollHeightSpan: Int = DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var span: DetailsAlertDialog<*>? = null
        protected open var details: DetailsAlertDialog<*>? = null
        protected open var isCancelable: Boolean = true
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
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }

        /**
         * Set icon tint of [ColorInt].
         *
         * @param tintColor The color int. E.g. [Color.BLUE]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@ColorInt tintColor: Int): Builder<D> {
            this.backgroundIconTintColor = IconTintAlertDialog(iconColorInt = tintColor)
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
                IconTintAlertDialog(iconColorInt = Color.rgb(red, green, blue))
            return this
        }

        /**
         * Set icon tint of [ColorRes].
         *
         * @param tintColor The color resource.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIconTintColorRes(@ColorRes tintColor: Int): Builder<D> {
            this.backgroundIconTintColor = IconTintAlertDialog(iconColorRes = tintColor)
            return this
        }

        /**
         * Set the maximum scroll size. Default 400.
         *
         * @param heightSpan height.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsScrollHeightSpan(heightSpan: Int): Builder<D> {
            this.detailsScrollHeightSpan = heightSpan
            return this
        }

        /**
         * Set the application name displayed in the [AboutMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(title: String): Builder<D> {
            return setApplicationName(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the application name displayed in the [AboutMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(@StringRes title: Int): Builder<D> {
            return setApplicationName(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the application name displayed in the [AboutMaterialDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(
            title: String,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.title = TitleAlertDialog(title = title, textAlignment = alignment)
            return this
        }

        /**
         * Set the application name displayed in the [AboutMaterialDialog]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationName(
            @StringRes title: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.title =
                TitleAlertDialog(title = context.getString(title), textAlignment = alignment)
            return this
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(message: String): Builder<D> {
            return setApplicationVersion(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(@StringRes message: Int): Builder<D> {
            return setApplicationVersion(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the application version to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            message: String,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.message = MessageAlertDialog.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the application version to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            @StringRes message: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.message =
                MessageAlertDialog.text(text = context.getString(message), alignment = alignment)
            return this
        }

        /**
         * Sets the application version to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(message: Spanned): Builder<D> {
            return setApplicationVersion(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the application version to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationVersion(
            message: Spanned,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(detail: String): Builder<D> {
            this.span = DetailsAlertDialog.text(text = detail)
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(@StringRes detail: Int): Builder<D> {
            this.span = DetailsAlertDialog.text(text = context.getString(detail))
            return this
        }

        /**
         * Set the application legalese to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationLegalese(detail: Spanned): Builder<D> {
            this.span = DetailsAlertDialog.spanned(text = detail)
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(details: String): Builder<D> {
            this.details = DetailsAlertDialog.text(text = details)
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(@StringRes details: Int): Builder<D> {
            this.details = DetailsAlertDialog.text(text = context.getString(details))
            return this
        }

        /**
         * Set the application details to display.
         *
         * @param details The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setApplicationDetails(details: Spanned): Builder<D> {
            this.details = DetailsAlertDialog.spanned(text = details)
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
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
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
            return setPositiveButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNeutralButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
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
            return setNeutralButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String? = null,
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNegativeButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
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
            return setNegativeButton(
                buttonText,
                MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener
            )
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
         * @return [Builder] object to allow for chaining of calls to set methods
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
         * Creates an [AboutMaterialDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}