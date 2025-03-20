package com.github.leodan11.alertdialog.dist.base

import androidx.annotation.DrawableRes
import com.github.leodan11.alertdialog.io.content.MaterialAlert
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
     * @param voidListener The [MaterialAlert.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNegativeButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialAlert.OnClickListener
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
     * @param voidListener The [MaterialAlert.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNeutralButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialAlert.OnClickListener
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
     * @param voidListener The [MaterialAlert.OnClickListener] to use. Default is `null`.
     * @param contentValueListener The [MaterialAlert.OnClickInputListener] to use. Default is `null`.
     * @param verificationCodeListener The [MaterialAlert.OnClickVerificationCodeListener] to use. Default is `null`.
     * @param signInListener The [MaterialAlert.OnClickSignInListener] to use. Default is `null`.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initPositiveButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: MaterialAlert.OnClickListener? = null,
        contentValueListener: MaterialAlert.OnClickInputListener? = null,
        verificationCodeListener: MaterialAlert.OnClickVerificationCodeListener? = null,
        signInListener: MaterialAlert.OnClickSignInListener? = null
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