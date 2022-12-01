package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.github.leodan11.alertdialog.MaterialCodeAlertDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogInputCodeBinding
import com.github.leodan11.alertdialog.dist.base.source.AlertDialogInterface
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_NEGATIVE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.BUTTON_POSITIVE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.INPUT_TYPE_DECIMAL_NUMBER
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.INPUT_TYPE_PERCENTAGE
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.NOT_ICON
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.getColorDefaultOnSurfaceTheme
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.getColorDefaultPrimaryTheme
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.getColorDefaultSurfaceTheme
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.onCallbackRequestFocus
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.onValidateTextField
import com.github.leodan11.alertdialog.dist.helpers.TextAlignment
import com.github.leodan11.alertdialog.dist.models.*

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class CodeAlertDialog(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog?,
    protected open var tintColor: IconTintAlertDialog?,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mInputsContentValue: List<InputAlertDialog>,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?
): AlertDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: AlertDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: AlertDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: AlertDialogInterface.OnShowListener? = null

    protected open fun createView(layoutInflater: LayoutInflater, container: ViewGroup? = null): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val binding: MAlertDialogInputCodeBinding = MAlertDialogInputCodeBinding.inflate(layoutInflater)
        // Initialize Views
        val mIconView = binding.imageViewIconCodeAlertDialog
        val mTitleView = binding.textViewTitleDialogCodeAlert
        val mTitleCodeView = binding.textViewTitleCodeDialogCodeAlert
        val mMessageView = binding.textViewMessageDialogCodeAlert
        val mHeaderLayout = binding.layoutContentHeaderCodeAlertDialog
        val mEditTextOne = binding.editTextTextValueOne
        val mEditTextTwo = binding.editTextTextValueTwo
        val mEditTextThree = binding.editTextTextValueThree
        val mEditTextFour = binding.editTextTextValueFour
        val mEditTextFive = binding.editTextTextValueFive
        val mEditTextSix = binding.editTextTextValueSix
        val mEditTextReasonLayout = binding.textInputLayoutCodeReason
        val mEditTextReasonInfo = binding.textInputEditTextCodeReason
        val mEditTextDecimalNumberLayout = binding.textInputLayoutCodeDecimalNumber
        val mEditTextDecimalNumberInfo = binding.textInputEditTextCodeDecimalNumber
        val mEditTextPercentageLayout = binding.textInputLayoutCodePercentage
        val mEditTextPercentageInfo = binding.textInputEditTextCodePercentage
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialogCode
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialogCode

        // Set Icon
        if (icon != null) icon?.let { mIconView.setImageResource(it.mDrawableResId) }
        // Set Title
        if (title != null){
            mTitleView.visibility = View.VISIBLE
            mTitleView.text = title?.title
            mTitleView.textAlignment = title?.textAlignment!!.alignment
        }else mHeaderLayout.visibility = View.GONE
        // Set Code
        onCallbackRequestFocus(mEditTextOne, mEditTextTwo)
        onCallbackRequestFocus(mEditTextTwo, mEditTextThree)
        onCallbackRequestFocus(mEditTextThree, mEditTextFour)
        onCallbackRequestFocus(mEditTextFour, mEditTextFive)
        onCallbackRequestFocus(mEditTextFive, mEditTextSix)

        // Set Content
        if (mInputsContentValue.isNotEmpty()){
            for (item in mInputsContentValue){
                when(item.inputType){
                    INPUT_TYPE_PERCENTAGE -> {
                        mEditTextPercentageLayout.hint = item.textHide
                        item.textHelper?.let { mEditTextPercentageLayout.helperText = it }
                        item.textHelperRes?.let { mEditTextPercentageLayout.helperText = mContext.getString(it) }
                        mEditTextPercentageLayout.visibility = View.VISIBLE
                        onCallbackRequestFocus(mEditTextSix, mEditTextPercentageInfo)
                    }
                    INPUT_TYPE_DECIMAL_NUMBER -> {
                        mEditTextDecimalNumberLayout.hint = item.textHide
                        item.textHelper?.let { mEditTextDecimalNumberLayout.helperText = it }
                        item.textHelperRes?.let { mEditTextDecimalNumberLayout.helperText = mContext.getString(it) }
                        mEditTextDecimalNumberLayout.visibility = View.VISIBLE
                        onCallbackRequestFocus(mEditTextSix, mEditTextDecimalNumberInfo)
                    }
                    else -> {
                        mEditTextPercentageLayout.visibility = View.GONE
                        mEditTextDecimalNumberLayout.visibility = View.GONE
                    }
                }
            }
        }else onCallbackRequestFocus(mEditTextSix, mEditTextReasonInfo)
        // Set Message
        if (message != null){
            mMessageView.visibility = View.VISIBLE
            mMessageView.text = message?.getText()
            mMessageView.textAlignment = message?.textAlignment!!.alignment
        }else mMessageView.visibility = View.GONE
        // Set Positive Button
        if (mPositiveButton != null){
            mPositiveButtonView.visibility = View.VISIBLE
            mPositiveButtonView.text = mPositiveButton?.title
            if (mPositiveButton?.icon != NOT_ICON) mPositiveButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mPositiveButton?.icon!!)
            mPositiveButtonView.setOnClickListener {
                if (mPositiveButton?.onChildClickListener != null){
                    if (validateCodeEditText(mEditTextOne) && validateCodeEditText(mEditTextTwo) && validateCodeEditText(mEditTextThree) &&
                        validateCodeEditText(mEditTextFour) && validateCodeEditText(mEditTextFive) && validateCodeEditText(mEditTextSix)){
                        val code = "${mEditTextOne.text}${mEditTextTwo.text}${mEditTextThree.text}${mEditTextFour.text}${mEditTextFive.text}${mEditTextSix.text}"
                        if (onValidateTextField(mEditTextReasonLayout, mEditTextReasonInfo, mContext.getString(R.string.text_value_reason_error))){
                            val reason = mEditTextReasonInfo.text.toString()
                            if (mInputsContentValue.isNotEmpty()){
                                var decimal: Double? = null
                                var percentage: Double? = null
                                var isFinish = false
                                for (item in mInputsContentValue){
                                    when(item.inputType){
                                        INPUT_TYPE_PERCENTAGE -> {
                                            if (onValidateTextField(mEditTextPercentageLayout,
                                                    mEditTextPercentageInfo,
                                                    if (item.textError != null) item.textError!! else mContext.getString(item.textErrorRes))){
                                                percentage = mEditTextPercentageInfo.text.toString().toDouble()
                                                isFinish = true
                                            }
                                        }
                                        INPUT_TYPE_DECIMAL_NUMBER ->{
                                            if (onValidateTextField(mEditTextDecimalNumberLayout,
                                                    mEditTextDecimalNumberInfo,
                                                    if (item.textError != null) item.textError!! else mContext.getString(item.textErrorRes))){
                                                decimal = mEditTextDecimalNumberInfo.text.toString().toDouble()
                                                isFinish = true
                                            }
                                        }
                                        else -> {
                                            Toast.makeText(mContext, mContext.getString(R.string.text_value_unknown_error), Toast.LENGTH_LONG).show()
                                            isFinish = false
                                        }
                                    }
                                }
                                if (isFinish) mPositiveButton?.onChildClickListener?.onClick(this, code, reason, decimal, percentage)
                            }else{
                                mPositiveButton?.onChildClickListener?.onClick(this, code, reason, null, null)
                            }
                        }
                    }else Toast.makeText(mContext, mContext.getString(R.string.text_value_code_error), Toast.LENGTH_LONG).show()
                }else if (mPositiveButton?.onClickListener != null) mPositiveButton?.onClickListener?.onClick(this, BUTTON_POSITIVE)
            }
        }else mPositiveButtonView.visibility = View.GONE
        // Set Negative Button
        if (mNegativeButton != null){
            mNegativeButtonView.visibility = View.VISIBLE
            mNegativeButtonView.text = mNegativeButton?.title
            if (mNegativeButton?.icon != NOT_ICON) mNegativeButtonView.icon = ContextCompat.getDrawable(mContext.applicationContext, mNegativeButton?.icon!!)
            mNegativeButtonView.setOnClickListener { mNegativeButton?.onClickListener?.onClick(this, BUTTON_NEGATIVE) }
        }else mNegativeButtonView.visibility = View.GONE
        // Apply Styles
        try {
            // Set Icon Color
            if (tintColor != null){
                if (tintColor?.iconColorRes != null) mIconView.setColorFilter(ContextCompat.getColor(mContext.applicationContext, tintColor?.iconColorRes!!))
                else if (tintColor?.iconColorInt != null) mIconView.setColorFilter(tintColor?.iconColorInt!!)
            }
            // Set Title Text Color
            mTitleView.setTextColor(getColorDefaultOnSurfaceTheme(mContext))
            // Set Title Code Text Color
            mTitleCodeView.setTextColor(getColorDefaultOnSurfaceTheme(mContext))
            // Set Message Text Color
            mMessageView.setTextColor(getColorDefaultOnSurfaceTheme(mContext))
            // Set InputLayout Decimal Number Color
            mEditTextDecimalNumberLayout.boxStrokeColor = getColorDefaultPrimaryTheme(mContext)
            mEditTextDecimalNumberLayout.hintTextColor = ColorStateList.valueOf(getColorDefaultPrimaryTheme(mContext))
            // Set InputLayout Percentage Color
            mEditTextPercentageLayout.boxStrokeColor = getColorDefaultPrimaryTheme(mContext)
            mEditTextPercentageLayout.hintTextColor = ColorStateList.valueOf(getColorDefaultPrimaryTheme(mContext))
            // Set InputLayout Reason Color
            mEditTextReasonLayout.boxStrokeColor = getColorDefaultPrimaryTheme(mContext)
            mEditTextReasonLayout.hintTextColor = ColorStateList.valueOf(getColorDefaultPrimaryTheme(mContext))
            // Set Positive Button Icon Tint
            val mPositiveButtonTint: ColorStateList = ColorStateList.valueOf(getColorDefaultSurfaceTheme(mContext))
            mPositiveButtonView.setTextColor(mPositiveButtonTint)
            mPositiveButtonView.iconTint = mPositiveButtonTint
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList = ColorStateList.valueOf(getColorDefaultPrimaryTheme(mContext))
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            // Set Positive Button Background Tint
            val mBackgroundTint: ColorStateList = ColorStateList.valueOf(getColorDefaultPrimaryTheme(mContext))
            mPositiveButtonView.backgroundTintList = mBackgroundTint
            mNegativeButtonView.rippleColor = mBackgroundTint.withAlpha(75)
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
        mDialog?.setOnDismissListener{ dismissCallback() }
    }

    /**
     * Set interface for callback events when dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: AlertDialogInterface.OnShowListener){
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

    private fun validateCodeEditText(editText: EditText): Boolean {
        if (TextUtils.isEmpty(editText.text.toString().trim())) return false
        return true
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D: CodeAlertDialog>(protected open val context: Context){

        protected open var icon: IconAlertDialog? = null
        protected open var tintColor: IconTintAlertDialog? = null
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var isCancelable: Boolean = false
        protected open var mInputsContentValue: List<InputAlertDialog> = arrayListOf()
        protected open var positiveButton: ButtonAlertDialog? = null
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
         * Set the title displayed in the [MaterialCodeAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String?): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialCodeAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, TextAlignment.START)
        }

        /**
         * Set the title displayed in the [MaterialCodeAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
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
         * Set the title displayed in the [MaterialCodeAlertDialog]. With text alignment: [TextAlignment.START], [TextAlignment.CENTER], [TextAlignment.END].
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
         * Set inputs to be displayed. Use class [InputAlertDialog].
         *
         * @param listInputs the list of input.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setInputs(@Size(max = 2) listInputs: List<InputAlertDialog> = arrayListOf()): Builder<D> {
            this.mInputsContentValue = listInputs
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
         * @param onChildClickListenerInput    The [AlertDialogInterface.OnChildClickListenerInput] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(buttonText: String?, onChildClickListenerInput: AlertDialogInterface.OnChildClickListenerInput): Builder<D> {
            return setPositiveButton(buttonText, NOT_ICON, onChildClickListenerInput)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onChildClickListenerInput    The [AlertDialogInterface.OnChildClickListenerInput] to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(@StringRes buttonText: Int, onChildClickListenerInput: AlertDialogInterface.OnChildClickListenerInput): Builder<D> {
            return setPositiveButton(buttonText, NOT_ICON, onChildClickListenerInput)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onChildClickListenerInput    The [AlertDialogInterface.OnChildClickListenerInput] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(buttonText: String?, @DrawableRes icon: Int, onChildClickListenerInput: AlertDialogInterface.OnChildClickListenerInput): Builder<D> {
            val valueText = if (buttonText.isNullOrEmpty()) context.getString(R.string.text_value_got_it)
            else buttonText
            positiveButton = ButtonAlertDialog(title = valueText, icon = icon, onChildClickListener = onChildClickListenerInput)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onChildClickListenerInput    The [AlertDialogInterface.OnChildClickListenerInput] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(@StringRes buttonText: Int, @DrawableRes icon: Int, onChildClickListenerInput: AlertDialogInterface.OnChildClickListenerInput): Builder<D> {
            positiveButton = ButtonAlertDialog(title = context.getString(buttonText), icon = icon, onChildClickListener = onChildClickListenerInput)
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
         * Creates an [MaterialCodeAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}