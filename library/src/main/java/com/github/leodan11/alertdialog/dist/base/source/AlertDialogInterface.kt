package com.github.leodan11.alertdialog.dist.base.source

interface AlertDialogInterface {

    fun cancel()
    fun dismiss()
    fun show()

    interface OnCancelListener {
        fun onCancel(dialog: AlertDialogInterface)
    }

    interface OnChildClickListenerInput {
        fun onClick(dialog: AlertDialogInterface, code: String, reason: String, numberDecimal: Double? = null, valuePercentage: Double? = null)
    }

    interface OnClickInvokedCallback {
        fun onClick(dialog: AlertDialogInterface, username: String, password: String)
    }

    interface OnClickListener {
        fun onClick(dialog: AlertDialogInterface, which: Int)
    }

    interface OnDismissListener {
        fun onDismiss(dialog: AlertDialogInterface)
    }

    interface OnShowListener {
        fun onShow(dialog: AlertDialogInterface)
    }

}