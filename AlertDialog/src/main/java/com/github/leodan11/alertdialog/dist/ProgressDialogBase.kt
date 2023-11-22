package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.ProgressMaterialDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.config.Init.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.databinding.MDialogProgressCircularBinding
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.content.MaterialAlertDialog
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultBackgroundTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultOnSurfaceTheme
import com.github.leodan11.alertdialog.io.extensions.getColorDefaultPrimaryTheme
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.progressindicator.CircularProgressIndicator

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class ProgressDialogBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mIndeterminate: Boolean,
    protected open var mNegativeButton: ButtonAlertDialog?
): MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open lateinit var mProgressCircular: CircularProgressIndicator
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    protected open fun createView(layoutInflater: LayoutInflater, container: ViewGroup? = null): View{
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val binding: MDialogProgressCircularBinding = MDialogProgressCircularBinding.inflate(layoutInflater)
        // Initialize Views
        val mIconView = binding.imageViewIconCircularProgressIndicator
        val mTitleView = binding.textViewTitleDialogCircularProgressIndicator
        val mHeaderLayout = binding.layoutContentHeaderCircularProgressIndicator
        val mMessageView = binding.textViewMessagesDialogCircularProgressIndicator
        mProgressCircular = binding.circularProgressIndicator
        val mNegativeButtonView = binding.buttonActionNegativeCircularProgressIndicator

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
            if (mNegativeButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNegativeButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mNegativeButton?.icon!!)
            mNegativeButtonView.setOnClickListener { mNegativeButton?.onClickListener?.onClick(this, MaterialAlertDialog.UI.BUTTON_NEGATIVE) }
        }else mNegativeButtonView.visibility = View.GONE
        // Apply Styles
        try {
            // Set Dialog Background
            binding.root.setBackgroundColor(mContext.getColorDefaultBackgroundTheme())
            // Set Icon Color
            if (tintColor != null){
                if (tintColor?.iconColorRes != null) mIconView.setColorFilter(ContextCompat.getColor(mContext.applicationContext, tintColor?.iconColorRes!!))
                else if (tintColor?.iconColorInt != null) mIconView.setColorFilter(tintColor?.iconColorInt!!)
            }
            // Set Title Text Color
            mTitleView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.getColorDefaultOnSurfaceTheme())
            // set Indeterminate Progress Circular & Tint
            mProgressCircular.isIndeterminate = mIndeterminate
            mProgressCircular.setIndicatorColor(mContext.getColorDefaultPrimaryTheme())
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList = ColorStateList.valueOf(mContext.getColorDefaultPrimaryTheme())
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            mNegativeButtonView.rippleColor = mNegativeButtonTint.withAlpha(75)
        }catch (e: Exception){ e.printStackTrace() }
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
    open fun setOnCancelListener(onCancelListener: MaterialDialogInterface.OnCancelListener){
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener{ cancelCallback() }
    }

    /**
     * Set interface for callback events when dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: MaterialDialogInterface.OnDismissListener){
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener{ dismissCallback() }
    }

    /**
     * Set interface for callback events when dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: MaterialDialogInterface.OnShowListener){
        this.mOnShowListener = onShowListener
        mDialog?.setOnShowListener{ showCallback() }
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
    abstract class Builder<D: ProgressDialogBase>(protected open val context: Context) {

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
         * Set the title displayed in the [ProgressMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            val valueText = if (title.isNullOrEmpty()) context.getString(R.string.text_value_information)
            else title
            this.title = TitleAlertDialog(title = valueText, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [ProgressMaterialDialog]. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
        }

        /**
         * Sets the message to display. With text alignment: [MaterialAlertDialog.TextAlignment.START], [MaterialAlertDialog.TextAlignment.CENTER], [MaterialAlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [MaterialAlertDialog.TextAlignment.CENTER].
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String?, alignment: MaterialAlertDialog.TextAlignment): Builder<D> {
            val valueText = if (message.isNullOrEmpty()) context.getString(R.string.text_value_charging)
            else message
            this.message = MessageAlertDialog.text(text = valueText, alignment = alignment)
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
            return setMessage(message, MaterialAlertDialog.TextAlignment.START)
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
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(buttonText: String?, onClickListener: MaterialDialogInterface.OnClickListener): Builder<D> {
            return setNegativeButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(@StringRes buttonText: Int, onClickListener: MaterialDialogInterface.OnClickListener): Builder<D> {
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
        fun setNegativeButton(buttonText: String?, icon: Int, onClickListener: MaterialDialogInterface.OnClickListener): Builder<D> {
            val valueText = if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_cancel)
            else buttonText
            negativeButton = ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
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
        fun setNegativeButton(@StringRes buttonText: Int, icon: Int, onClickListener: MaterialDialogInterface.OnClickListener): Builder<D> {
            negativeButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onClickListener = onClickListener)
            return this
        }


        /**
         * Creates an [ProgressMaterialDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}