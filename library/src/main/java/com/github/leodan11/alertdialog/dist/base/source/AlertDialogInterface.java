package com.github.leodan11.alertdialog.dist.base.source;

public interface AlertDialogInterface {

    void cancel();
    void dismiss();

    interface OnCancelListener {
        void onCancel(AlertDialogInterface dialog);
    }

    interface OnChildClickListenerInput {
        void onClick(AlertDialogInterface dialog, String code, String reason, Double numberDecimal, Double valuePercentage);
    }

    interface OnClickInvokedCallback {
        void onClick(AlertDialogInterface dialog, String username, String password);
    }

    interface OnClickListener {
        void onClick(AlertDialogInterface dialog, int which);
    }

    interface OnDismissListener {
        void onDismiss(AlertDialogInterface dialog);
    }

    interface OnShowListener {
        void onShow(AlertDialogInterface dialog);
    }

}
