package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.MaterialAlertDialog
import com.github.leodan11.alertdialog.dist.base.source.AlertDialogInterface
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_NEGATIVE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_NEUTRAL
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_POSITIVE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_CUSTOM
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_ERROR
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_HELP
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_INFORMATION
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_SUCCESS
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.DIALOG_STYLE_WARNING
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.NOT_ICON
import com.github.leodan11.alertdialog.dist.helpers.TextAlignment
import com.github.leodan11.alertdialog.dist.models.*
import com.google.android.material.button.MaterialButton
import com.leodan.readmoreoption.ReadMoreOption

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class AlertDefaultDialog(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var type: TypeAlertDialog,
    protected open var backgroundColorSpan: Int,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var details: DetailsAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?
): AlertDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: AlertDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: AlertDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: AlertDialogInterface.OnShowListener? = null

    protected open fun createView(layoutInflater: LayoutInflater, container: ViewGroup? = null): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.m_alert_dialog, container, false)
        // Initialize Views
        val mIconView: ImageView = dialogView.findViewById(R.id.imageViewIconAlertDialog)
        val mTitleView: TextView = dialogView.findViewById(R.id.textViewTitleAlertDialog)
        val mMessageView: TextView = dialogView.findViewById(R.id.textViewMessageAlertDialog)
        val mDetailsView: TextView = dialogView.findViewById(R.id.textViewDetailsAlertDialog)
        val mPositiveButtonView: MaterialButton = dialogView.findViewById(R.id.buttonActionPositiveAlertDialog)
        val mNeutralButtonView: MaterialButton = dialogView.findViewById(R.id.buttonActionNeutralAlertDialog)
        val mNegativeButtonView: MaterialButton = dialogView.findViewById(R.id.buttonActionNegativeAlertDialog)

        // Set Icon
        when(type.typeDialog){
            DIALOG_STYLE_ERROR -> mIconView.setImageResource(R.drawable.ic_error)
            DIALOG_STYLE_HELP -> mIconView.setImageResource(R.drawable.ic_help)
            DIALOG_STYLE_INFORMATION -> mIconView.setImageResource(R.drawable.ic_information)
            DIALOG_STYLE_SUCCESS -> mIconView.setImageResource(R.drawable.ic_success)
            DIALOG_STYLE_WARNING -> mIconView.setImageResource(R.drawable.ic_warning)
            else -> mIconView.setImageResource(icon.mDrawableResId)
        }
        // Set Icon BackgroundTint
        dialogView.findViewById<CoordinatorLayout>(R.id.coordinatorLayoutContainerAlertDialog).backgroundTintList = when(type.typeDialog){
            DIALOG_STYLE_ERROR -> { ColorStateList.valueOf(mContext.getColor(R.color.material_dialog_header_background_error)) }
            DIALOG_STYLE_HELP -> { ColorStateList.valueOf(mContext.getColor(R.color.material_dialog_header_background_help)) }
            DIALOG_STYLE_INFORMATION -> { ColorStateList.valueOf(mContext.getColor(R.color.material_dialog_header_background_information)) }
            DIALOG_STYLE_SUCCESS -> { ColorStateList.valueOf(mContext.getColor(R.color.material_dialog_header_background_success)) }
            DIALOG_STYLE_WARNING -> { ColorStateList.valueOf(mContext.getColor(R.color.material_dialog_header_background_warning)) }
            else -> { ColorStateList.valueOf(mContext.getColor(backgroundColorSpan)) }
        }
        // Set Title
        if (title != null){
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        }else mTitleView.visibility = View.GONE
        // Set Message
        if (message != null){
            mMessageView.visibility = View.VISIBLE
            mMessageView.text = message?.getText()
            mMessageView.textAlignment = message?.textAlignment!!.alignment
        }else mMessageView.visibility = View.GONE
        // Set Details
        if (details != null){
            val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(mContext.applicationContext)
                .textLength(3)
                .textLengthType(ReadMoreOption.TYPE_LINE)
                .moreLabelColor(mContext.getColor(R.color.material_dialog_negative_button_text_color))
                .lessLabelColor(mContext.getColor(R.color.material_dialog_negative_button_text_color))
                .labelUnderLine(true)
                .expandAnimation(true)
                .build()
            mDetailsView.visibility = View.VISIBLE
            readMoreOption.addReadMoreTo(mDetailsView, details?.getText()!!)
        }else mDetailsView.visibility = View.GONE
        // Set Positive Button
        if (mPositiveButton != null){
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != NOT_ICON) mPositiveButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
            mPositiveButtonView.setOnClickListener { mPositiveButton?.onClickListener?.onClick(this, BUTTON_POSITIVE) }
        }else mPositiveButtonView.visibility = View.GONE
        // Set Neutral Button
        if (mNeutralButton != null){
            mNeutralButtonView.visibility = View.VISIBLE
            mNeutralButtonView.text = mNeutralButton?.title
            if (mNeutralButton?.icon != NOT_ICON) mNeutralButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mNeutralButton?.icon!!)
            mNeutralButtonView.setOnClickListener { mNeutralButton?.onClickListener?.onClick(this, BUTTON_NEUTRAL) }
        }else mNeutralButtonView.visibility = View.GONE
        // Set Negative Button
        if (mNegativeButton != null){
            mNegativeButtonView.visibility = View.VISIBLE
            mNegativeButtonView.text = mNegativeButton?.title
            if (mNegativeButton?.icon != NOT_ICON) mNegativeButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mNegativeButton?.icon!!)
            mNegativeButtonView.setOnClickListener { mNegativeButton?.onClickListener?.onClick(this, BUTTON_NEGATIVE) }
        }else mNegativeButtonView.visibility = View.GONE
        // Apply Styles
        val typeArray: TypedArray = mContext.theme.obtainStyledAttributes(R.styleable.MaterialDialog)
        try {
            // Set Dialog Background
            dialogView.setBackgroundColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_background, mContext.getColor(R.color.material_dialog_background)))
            // Set Title Text Color
            mTitleView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_title_text_color, mContext.getColor(R.color.material_dialog_title_text_color)))
            // Set Message Text Color
            mMessageView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_message_text_color, mContext.getColor(R.color.material_dialog_message_text_color)))
            // Set Details Text Color
            mDetailsView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_details_text_color, mContext.getColor(R.color.material_dialog_message_text_color)))
            // Set Positive Button Icon Tint
            var mPositiveButtonTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_positive_button_text_color)
            if (mPositiveButtonTint == null) mPositiveButtonTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_positive_button_text_color)
            mPositiveButtonView.setTextColor(mPositiveButtonTint)
            mPositiveButtonView.iconTint = mPositiveButtonTint
            // Set Neutral Button Icon & Text Tint
            var mNeutralButtonTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_negative_button_text_color)
            if (mNeutralButtonTint == null) mNeutralButtonTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_negative_button_text_color)
            mNeutralButtonView.setTextColor(mNeutralButtonTint)
            mNeutralButtonView.iconTint = mNeutralButtonTint
            // Set Negative Button Icon & Text Tint
            var mNegativeButtonTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_negative_button_text_color)
            if (mNegativeButtonTint == null) mNegativeButtonTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_negative_button_text_color)
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            // Set Positive Button Background Tint
            var mBackgroundTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_positive_button_color)
            if (mBackgroundTint == null) mBackgroundTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_positive_button_color)
            mPositiveButtonView.backgroundTintList = mBackgroundTint
            if (mBackgroundTint != null){
                mNeutralButtonView.rippleColor = mBackgroundTint.withAlpha(75)
                mNegativeButtonView.rippleColor = mBackgroundTint.withAlpha(75)
            }
        }catch (e: Exception){ e.printStackTrace() } finally { typeArray.recycle() }
        return dialogView
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
    override fun show() {
        if (mDialog != null) mDialog?.show()
        else throwNullDialog()
    }


    /**
     * Set interface for callback events when dialog is cancelled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: AlertDialogInterface.OnCancelListener){
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener{ cancelCallback() }
    }

    /**
     * Set interface for callback events when dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: AlertDialogInterface.OnDismissListener){
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnCancelListener{ dismissCallback() }
    }

    /**
     * Set interface for callback events when dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: AlertDialogInterface.OnShowListener){
        this.mOnShowListener = onShowListener
        mDialog?.setOnCancelListener{ showCallback() }
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

    private fun throwNullDialog(){
        throw NullPointerException("Called method on null Dialog. Create dialog using `Builder` before calling on Dialog")
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D: AlertDefaultDialog>(protected open val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(R.drawable.ic_help)
        protected open var backgroundColorSpan: Int = R.color.material_dialog_header_background_help
        protected open var type: TypeAlertDialog = TypeAlertDialog()
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
         * [DIALOG_STYLE_ERROR], [DIALOG_STYLE_HELP], [DIALOG_STYLE_INFORMATION],
         * [DIALOG_STYLE_SUCCESS], [DIALOG_STYLE_WARNING], [DIALOG_STYLE_CUSTOM].
         *
         * @param dialogType By default it is used [DIALOG_STYLE_SUCCESS].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setType(dialogType: Int): Builder<D> {
            this.type = TypeAlertDialog(typeDialog = dialogType)
            return this
        }

        /**
         * Set background [ColorRes].
         *
         * @param color Color resource. Eg: [R.color.material_dialog_background].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpan(@ColorRes color: Int): Builder<D> {
            this.backgroundColorSpan = color
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String, alignment: TextAlignment): Builder<D> {
            this.title = TitleAlertDialog(title = title, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int, alignment: TextAlignment): Builder<D> {
            this.title = TitleAlertDialog(title = context.getString(title), textAlignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, TextAlignment.CENTER)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, TextAlignment.CENTER)
        }

        /**
         * Sets the message to display. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String, alignment: TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the message to display. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int, alignment: TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.text(text = context.getString(message), alignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, TextAlignment.CENTER)
        }

        /**
         * Sets the message to display. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param detail The details to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: String): Builder<D> {
            this.details = DetailsAlertDialog.text(text = detail)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param detail The details to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setDetails(@StringRes detail: Int): Builder<D> {
            this.details = DetailsAlertDialog.text(text = context.getString(detail))
            return this
        }

        /**
         * Sets the message to display.
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
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(buttonText: String?, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setPositiveButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(@StringRes buttonText: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setPositiveButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(buttonText: String?, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            val valueText = if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_accept)
            else buttonText
            positiveButton = ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(@StringRes buttonText: Int, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            positiveButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(buttonText: String?, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setNeutralButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(@StringRes buttonText: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setNeutralButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(buttonText: String?, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            val valueText = if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_decline)
            else buttonText
            neutralButton = ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(@StringRes buttonText: Int, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            neutralButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(buttonText: String?, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setNegativeButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(@StringRes buttonText: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            return setNegativeButton(buttonText, NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(buttonText: String?, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            val valueText = if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_cancel)
            else buttonText
            negativeButton = ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [AlertDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(@StringRes buttonText: Int, @DrawableRes icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            negativeButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onClickListener = onClickListener)
            return this
        }


        /**
         * Creates an [MaterialAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}