package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.MaterialCircularProgressAlertDialog
import com.github.leodan11.alertdialog.dist.base.source.AlertDialogInterface
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_NEGATIVE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.NOT_ICON
import com.github.leodan11.alertdialog.dist.helpers.TextAlignment
import com.github.leodan11.alertdialog.dist.models.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class CircularProgressAlertDialog(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mIndeterminate: Boolean,
    protected open var mNegativeButton: ButtonAlertDialog?
): AlertDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open lateinit var mProgressCircular: CircularProgressIndicator
    protected open var mOnDismissListener: AlertDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: AlertDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: AlertDialogInterface.OnShowListener? = null

    protected open fun createView(layoutInflater: LayoutInflater, container: ViewGroup? = null): View{
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.m_dialog_progress_circular, container, false)
        // Initialize Views
        val mIconView: ImageView = dialogView.findViewById(R.id.image_view_icon_circular_progress_indicator)
        val mTitleView: TextView = dialogView.findViewById(R.id.text_view_title_dialog_circular_progress_indicator)
        val mHeaderLayout: RelativeLayout = dialogView.findViewById(R.id.layout_content_header_circular_progress_indicator)
        val mMessageView: TextView = dialogView.findViewById(R.id.text_view_messages_dialog_circular_progress_indicator)
        mProgressCircular = dialogView.findViewById(R.id.circularProgressIndicator)
        val mNegativeButtonView: MaterialButton = dialogView.findViewById(R.id.buttonActionNegativeCircularProgressIndicator)

        // Set Icon
        if (icon != null){
            mIconView.visibility = View.VISIBLE
            mIconView.setImageResource(icon!!.mDrawableResId)
        }else mIconView.visibility = View.GONE
        // Set Title
        if (title != null){
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        }else mHeaderLayout.visibility = View.GONE
        // Set Message
        if (message != null){
            mMessageView.visibility = View.VISIBLE
            mMessageView.text = message?.getText()
            mMessageView.textAlignment = message?.textAlignment!!.alignment
        }else mMessageView.visibility = View.GONE
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
            // Set Icon Color
            if (tintColor != null){
                if (tintColor?.iconColorRes != null) mIconView.setColorFilter(ContextCompat.getColor(mContext.applicationContext, tintColor?.iconColorRes!!))
                else if (tintColor?.iconColorInt != null) mIconView.setColorFilter(tintColor?.iconColorInt!!)
            }else mIconView.setColorFilter(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_color_primary_dark, mContext.getColor(R.color.material_dialog_color_primary_dark)))
            // Set Title Text Color
            mTitleView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_title_text_color, mContext.getColor(R.color.material_dialog_title_text_color)))
            // Set Message Text Color
            mMessageView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_message_text_color, mContext.getColor(R.color.material_dialog_message_text_color)))
            // set Indeterminate Progress Circular & Tint
            mProgressCircular.isIndeterminate = mIndeterminate
            var mProgressCircularTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_progress_circular)
            if (mProgressCircularTint == null) mProgressCircularTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_progress_circular)
            mProgressCircular.progressTintList = mProgressCircularTint
            // Set Negative Button Icon & Text Tint
            var mNegativeButtonTint: ColorStateList? = typeArray.getColorStateList(R.styleable.MaterialDialog_material_dialog_negative_button_text_color)
            if (mNegativeButtonTint == null) mNegativeButtonTint = ContextCompat.getColorStateList(mContext.applicationContext, R.color.material_dialog_negative_button_text_color)
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
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
     * Sets the current progress.
     *
     * @param progress the current progress, a value between 0 and 100.
     * @param animated the animated activated, a value [Boolean].
     */
    open fun setProgress(progress: Int, animated: Boolean = true){
        mProgressCircular.setProgressCompat(progress, animated)
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
    abstract class Builder<D: CircularProgressAlertDialog>(protected open val context: Context) {

        protected open var icon: IconAlertDialog? = null
        protected open var tintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var isCancelable: Boolean = true
        protected open var isIndeterminate: Boolean = false
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
         * Set icon tint of [ColorRes].
         *
         * @param tintColor the color resource.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@ColorRes tintColor: Int): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorRes = tintColor)
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
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIconTintColor(@IntRange(from = 0, to = 255) red: Int, @IntRange(from = 0, to = 255) green: Int, @IntRange(from = 0, to = 255) blue: Int): Builder<D> {
            this.tintColor = IconTintAlertDialog(iconColorInt = Color.rgb(red, green, blue))
            return this
        }

        /**
         * Set the title displayed in the [MaterialCircularProgressAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialCircularProgressAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialCircularProgressAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?, alignment: TextAlignment): Builder<D> {
            val valueText = if (title.isNullOrEmpty()) context.getString(R.string.text_value_information)
            else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialCircularProgressAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
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
        fun setMessage(message: String?): Builder<D> {
            return setMessage(message, TextAlignment.START)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String?, alignment: TextAlignment): Builder<D> {
            val valueText = if (message.isNullOrEmpty()) context.getString(R.string.text_value_charging)
            else message
            this.message = MessageAlertDialog.text(text = valueText, alignment = alignment)
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
            return setMessage(message, TextAlignment.START)
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
        fun setNegativeButton(buttonText: String?, icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
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
        fun setNegativeButton(@StringRes buttonText: Int, icon: Int, onClickListener: AlertDialogInterface.OnClickListener): Builder<D> {
            negativeButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onClickListener = onClickListener)
            return this
        }


        /**
         * Creates an [MaterialCircularProgressAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}