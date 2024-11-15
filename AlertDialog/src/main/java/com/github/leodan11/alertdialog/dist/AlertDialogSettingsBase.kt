package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.Spanned
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.SettingsAlertDialog
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class AlertDialogSettingsBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var title: TitleAlertDialog,
    protected open var message: MessageAlertDialog<*>,
    protected open var launch: ActivityResultLauncher<Intent>,
    protected open var openOnNewTask: Boolean,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : DialogInterface.OnClickListener {

    protected open var mDialog: Dialog? = null

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

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun setBuilderView(builder: MaterialAlertDialogBuilder) {
        builder.setIcon(icon.mDrawableResId)
        title.let { builder.setTitle(it.title) }
        message.let { builder.setMessage(it.getText()) }
        mPositiveButton?.let {
            builder.setPositiveButton(it.title, this)
        }
        mNegativeButton?.let {
            builder.setNegativeButton(it.title, this)
        }
        mNeutralButton?.let {
            builder.setNeutralButton(it.title, this)
        }
    }

    private fun throwNullDialog() {
        throw NullPointerException("Called method on null Dialog. Create dialog using `Builder` before calling on Dialog")
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     *
     * @param context – the parent context
     *
     * @return [Builder] object to allow for chaining of calls to set methods
     *
     */
    abstract class Builder<D : AlertDialogSettingsBase>(protected val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(context.applicationInfo.icon)
        protected open var title: TitleAlertDialog = TitleAlertDialog(
            title = context.getString(R.string.label_text_title_settings_dialog),
            textAlignment = AlertDialog.TextAlignment.START
        )
        protected open var message: MessageAlertDialog<*> = MessageAlertDialog.text(
            text = context.getString(R.string.label_text_rationale_ask_again),
            alignment = AlertDialog.TextAlignment.START
        )
        protected open lateinit var launch: ActivityResultLauncher<Intent>
        protected open var openOnNewTask: Boolean = false
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
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }

        /**
         * Set the title displayed in the [SettingsAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            this.title =
                TitleAlertDialog(title = title, textAlignment = AlertDialog.TextAlignment.START)
            return this
        }

        /**
         * Set the title displayed in the [SettingsAlertDialog].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            this.title = TitleAlertDialog(
                title = context.getString(title),
                textAlignment = AlertDialog.TextAlignment.START
            )
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String): Builder<D> {
            this.message =
                MessageAlertDialog.text(text = message, alignment = AlertDialog.TextAlignment.START)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            this.message = MessageAlertDialog.text(
                text = context.getString(message),
                alignment = AlertDialog.TextAlignment.START
            )
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned): Builder<D> {
            this.message = MessageAlertDialog.spanned(
                text = message,
                alignment = AlertDialog.TextAlignment.START
            )
            return this
        }

        /**
         * Set launcher for intent
         *
         * @param launch A launcher for a previously registered [Intent].
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setLaunch(launch: ActivityResultLauncher<Intent>): Builder<D> {
            this.launch = launch
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
         * Sets whether the intent will open in a new task or not.
         *
         * @param openOnNewTask is [Boolean] value. Default is false.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setOpenOnNewTask(openOnNewTask: Boolean): Builder<D> {
            this.openOnNewTask = openOnNewTask
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
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_accept) else buttonText
            positiveButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
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
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
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
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_decline) else buttonText
            neutralButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
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
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            neutralButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
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
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_cancel) else buttonText
            negativeButton = ButtonAlertDialog(
                title = valueText,
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
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
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener
        ): Builder<D> {
            negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = MATERIAL_ALERT_DIALOG_UI_NOT_ICON,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Creates an [SettingsAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}
