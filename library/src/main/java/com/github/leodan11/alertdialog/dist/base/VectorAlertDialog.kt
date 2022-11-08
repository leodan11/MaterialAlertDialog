package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
import android.content.Context
import android.content.res.TypedArray
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.MaterialProgressAlertDialog
import com.github.leodan11.alertdialog.dist.base.source.AlertDialogInterface
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.onAnimatedVectorDrawable
import com.github.leodan11.alertdialog.dist.helpers.TextAlignment
import com.github.leodan11.alertdialog.dist.models.IconAlertDialog
import com.github.leodan11.alertdialog.dist.models.MessageAlertDialog
import com.github.leodan11.alertdialog.dist.models.TitleAlertDialog

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class VectorAlertDialog(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var mAnimatedVectorDrawable: Boolean,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean
): AlertDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: AlertDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: AlertDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: AlertDialogInterface.OnShowListener? = null

    protected open fun createView(layoutInflater: LayoutInflater, container: ViewGroup? = null): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.m_dialog_progress, container, false)
        // Initialize Views
        val mIconView: ImageView = dialogView.findViewById(R.id.imageViewIconLogoDialogProgress)
        val mTitleView: TextView = dialogView.findViewById(R.id.text_view_title_dialog_progress)
        val mMessageView: TextView = dialogView.findViewById(R.id.text_view_messages_dialog_progress)
        val mHeaderLayout: RelativeLayout = dialogView.findViewById(R.id.layout_content_header_dialog_progress)

        // Set Icon
        mIconView.setImageResource(icon.mDrawableResId)
        // Set Icon Animator
        if (mAnimatedVectorDrawable) onAnimatedVectorDrawable(mIconView)
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
        // Apply Styles
        val typeArray: TypedArray = mContext.theme.obtainStyledAttributes(R.styleable.MaterialDialog)
        try {
            // Set Dialog Background
            dialogView.setBackgroundColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_background, mContext.getColor(R.color.material_dialog_background)))
            // Set Title Text Color
            mTitleView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_title_text_color, mContext.getColor(R.color.material_dialog_title_text_color)))
            // Set Message Text Color
            mMessageView.setTextColor(typeArray.getColor(R.styleable.MaterialDialog_material_dialog_message_text_color, mContext.getColor(R.color.material_dialog_message_text_color)))
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
    abstract class Builder<D: VectorAlertDialog>(protected open val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(R.drawable.ic_animated_default)
        protected open var isAnimatedVectorDrawable: Boolean = true
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var isCancelable: Boolean = true

        /**
         * Set animated vector [DrawableRes] to be used as progress.
         *
         * @param icon Drawable to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }


        /**
         * Set animated vector [DrawableRes] to be used as progress.
         *
         * @param isAnimated value [Boolean]. Default value true.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setAnimatedVectorDrawable(isAnimated: Boolean): Builder<D> {
            this.isAnimatedVectorDrawable = isAnimated
            return this
        }

        /**
         * Set the title displayed in the [MaterialProgressAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialProgressAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialProgressAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
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
         * Set the title displayed in the [MaterialProgressAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
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
            val valueText = if (message.isNullOrEmpty()) context.getString(R.string.text_value_charging_please)
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
         * Creates an [MaterialProgressAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}