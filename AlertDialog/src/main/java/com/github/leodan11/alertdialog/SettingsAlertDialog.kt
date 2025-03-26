package com.github.leodan11.alertdialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import com.github.leodan11.alertdialog.dist.SettingsComponentBase
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsAlertDialog private constructor(
    mContext: Context,
    icon: IconAlert,
    title: TitleAlert,
    message: MessageAlert<*>,
    launch: ActivityResultLauncher<Intent>,
    openOnNewTask: Boolean,
    mCancelable: Boolean,
    mGravity: Int?,
    mPositiveButton: ButtonAlertDialog?,
    mNeutralButton: ButtonAlertDialog?,
    mNegativeButton: ButtonAlertDialog?,
) : SettingsComponentBase(
    mContext = mContext,
    icon = icon,
    title = title,
    message = message,
    launch = launch,
    openOnNewTask = openOnNewTask,
    mCancelable = mCancelable,
    mPositiveButton = mPositiveButton,
    mNeutralButton = mNeutralButton,
    mNegativeButton = mNegativeButton
) {

    // Init Dialog
    init {
        val builder = MaterialAlertDialogBuilder(mContext)
        setBuilderView(builder)
        // Set Cancelable property
        builder.setCancelable(mCancelable)
        // Create and show dialog
        mDialog = builder.create()
        mDialog?.window?.apply {
            mGravity?.let { setGravity(it) }
        }
    }


    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", mContext.packageName, null)
                    flags = if (openOnNewTask) Intent.FLAG_ACTIVITY_NEW_TASK else 0
                }
                launch.launch(intent)
            }

            DialogInterface.BUTTON_NEGATIVE -> mDialog?.dismiss()
            DialogInterface.BUTTON_NEUTRAL -> mDialog?.dismiss()
        }
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     *
     * @param context the parent context
     *
     * @return [Builder] object to allow for chaining of calls to set methods
     */
    class Builder(context: Context) :
        SettingsComponentBase.Builder<SettingsAlertDialog>(context = context) {

        override fun create(): SettingsAlertDialog {
            return SettingsAlertDialog(
                mContext = context,
                icon = icon,
                title = title,
                message = message,
                launch = launch,
                openOnNewTask = openOnNewTask,
                mCancelable = isCancelable,
                mGravity = gravity,
                mPositiveButton = positiveButton,
                mNeutralButton = neutralButton,
                mNegativeButton = negativeButton
            )
        }
    }

}
