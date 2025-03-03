package com.github.leodan11.alertdialog.dist.base

import androidx.annotation.DrawableRes
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert

/**
 * Creates a builder for an alert dialog that uses the default alert dialog theme.
 *
 *
 */
open class AlertBuilder {


    /**
     * Set negative button.
     *
     * @constructor Creates a new [ButtonAlertDialog].
     *
     * @param title The title to display in the dialog.
     * @param icon The [DrawableRes] to be set as an icon for the button.
     * @param voidListener The [MaterialDialogInterface.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNegativeButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialDialogInterface.OnClickListener
    ): ButtonAlertDialog {
        return ButtonAlertDialog(title, icon, voidListener)
    }


    /**
     * Set neutral button.
     *
     * @constructor Creates a new [ButtonAlertDialog].
     *
     * @param title The title to display in the dialog.
     * @param icon The [DrawableRes] to be set as an icon for the button.
     * @param voidListener The [MaterialDialogInterface.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNeutralButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialDialogInterface.OnClickListener
    ): ButtonAlertDialog {
        return ButtonAlertDialog(title, icon, voidListener)
    }


    /**
     * Set positive button.
     *
     * @constructor Creates a new [ButtonAlertDialog].
     *
     * @param title The title to display in the dialog.
     * @param icon The [DrawableRes] to be set as an icon for the button.
     * @param voidListener The [MaterialDialogInterface.OnClickListener] to use. Default is `null`.
     * @param contentValueListener The [MaterialDialogInterface.OnClickInputListener] to use. Default is `null`.
     * @param verificationCodeListener The [MaterialDialogInterface.OnClickVerificationCodeListener] to use. Default is `null`.
     * @param signInListener The [MaterialDialogInterface.OnClickSignInListener] to use. Default is `null`.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initPositiveButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialDialogInterface.OnClickListener? = null,
        contentValueListener: MaterialDialogInterface.OnClickInputListener? = null,
        verificationCodeListener: MaterialDialogInterface.OnClickVerificationCodeListener? = null,
        signInListener: MaterialDialogInterface.OnClickSignInListener? = null
    ): ButtonAlertDialog {
        return ButtonAlertDialog(
            title,
            icon,
            voidListener,
            contentValueListener,
            verificationCodeListener,
            signInListener
        )
    }

}