package com.github.leodan11.alertdialog.io.content;

public interface MaterialDialogInterface {

    void cancel();
    void dismiss();

    interface OnCancelListener {
        void onCancel(MaterialDialogInterface dialog);
    }

    interface OnClickVerificationCodeListener {
        void onClick(MaterialDialogInterface dialog, String code, String reason, Double numberDecimal, Double valuePercentage);
    }

    interface OnClickSignInListener {
        void onClick(MaterialDialogInterface dialog, String username, String password);
    }

    interface OnClickListener {
        void onClick(MaterialDialogInterface dialog, AlertDialog.UI whichButton);
    }

    interface OnClickInputListener {
        void onClick(MaterialDialogInterface dialog, String contentValue);
    }

    interface OnDismissListener {
        void onDismiss(MaterialDialogInterface dialog);
    }

    interface OnShowListener {
        void onShow(MaterialDialogInterface dialog);
    }

}
