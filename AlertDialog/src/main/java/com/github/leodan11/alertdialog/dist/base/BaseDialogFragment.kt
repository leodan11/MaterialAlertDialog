package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.github.leodan11.alertdialog.MaterialProgressSmallDialog
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MDialogProgressSmallBinding
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog.onAnimatedVectorDrawable
import com.github.leodan11.alertdialog.dist.helpers.TextAlignment
import com.github.leodan11.alertdialog.dist.models.IconAlertDialog
import com.github.leodan11.alertdialog.dist.models.MessageAlertDialog

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class BaseDialogFragment(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var mAnimatedVectorDrawable: Boolean,
    protected open var message: MessageAlertDialog<*>?,
    protected open var mCancelable: Boolean
) : DialogFragment() {

    private var _binding: MDialogProgressSmallBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = MDialogProgressSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // Set Icon
            imageViewIconLogoDialogProgress.setImageResource(icon.mDrawableResId)
            // Set isAnimator Icon
            if (mAnimatedVectorDrawable) onAnimatedVectorDrawable(imageViewIconLogoDialogProgress)
            // Set Message
            if (message != null) {
                textViewMessagesDialogProgress.visibility = View.VISIBLE
                textViewMessagesDialogProgress.text = message?.getText()
                textViewMessagesDialogProgress.textAlignment = message?.textAlignment!!.alignment
            } else textViewMessagesDialogProgress.visibility = View.GONE
            // Set Cancelable
            dialog?.setCancelable(mCancelable)

        }

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Start the dialog and display it on screen.
     * The window is placed in the application layer and opaque.
     * Note that you should not override this method to do initialization when the dialog is shown.
     *
     * @param fragmentManager   Required FragmentManager
     */
    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, TAG)
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : BaseDialogFragment>(protected open val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(R.drawable.ic_animated_default)
        protected open var isAnimatedVectorDrawable: Boolean = true
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
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String?): Builder<D> {
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
        fun setMessage(message: String?, alignment: TextAlignment): Builder<D> {
            val valueText =
                if (message.isNullOrEmpty()) context.getString(R.string.text_value_charging_please) else message
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
         * Creates an [MaterialProgressSmallDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

    companion object {
        private val TAG: String = MaterialProgressSmallDialog::class.java.simpleName
    }

}