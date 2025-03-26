package com.github.leodan11.alertdialog.dist.base

import androidx.annotation.DrawableRes
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
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
     * @param voidListener The [DialogAlertInterface.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNegativeButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: DialogAlertInterface.OnClickListener
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
     * @param voidListener The [DialogAlertInterface.OnClickListener] to use.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initNeutralButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: DialogAlertInterface.OnClickListener
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
     * @param voidListener The [DialogAlertInterface.OnClickListener] to use. Default is `null`.
     * @param contentValueListener The [DialogAlertInterface.OnClickInputListener] to use. Default is `null`.
     * @param verificationCodeListener The [DialogAlertInterface.OnClickVerificationCodeListener] to use. Default is `null`.
     * @param signInListener The [DialogAlertInterface.OnClickSignInListener] to use. Default is `null`.
     *
     * @return Builder object to allow for chaining of calls to set methods
     *
     */
    protected open fun initPositiveButton(
        title: String,
        icon: ButtonIconAlert,
        voidListener: DialogAlertInterface.OnClickListener? = null,
        contentValueListener: DialogAlertInterface.OnClickInputListener? = null,
        verificationCodeListener: DialogAlertInterface.OnClickVerificationCodeListener? = null,
        signInListener: DialogAlertInterface.OnClickSignInListener? = null
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