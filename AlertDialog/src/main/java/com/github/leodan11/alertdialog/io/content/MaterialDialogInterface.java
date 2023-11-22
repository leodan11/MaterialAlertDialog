package com.github.leodan11.alertdialog.io.content;

public interface MaterialDialogInterface {

    void cancel();
    void dismiss();

    interface OnCancelListener {
        void onCancel(MaterialDialogInterface dialog);
    }

    interface OnChildClickListenerInput {
        void onClick(MaterialDialogInterface dialog, String code, String reason, Double numberDecimal, Double valuePercentage);
    }

    interface OnClickInvokedCallback {
        void onClick(MaterialDialogInterface dialog, String username, String password);
    }

    interface OnClickListener {
        void onClick(MaterialDialogInterface dialog, MaterialAlertDialog.UI whichButton);
    }

    interface OnDismissListener {
        void onDismiss(MaterialDialogInterface dialog);
    }

    interface OnShowListener {
        void onShow(MaterialDialogInterface dialog);
    }

}
