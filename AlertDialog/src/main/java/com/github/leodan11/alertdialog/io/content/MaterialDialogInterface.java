package com.github.leodan11.alertdialog.io.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface MaterialDialogInterface {

    void cancel();

    void dismiss();

    interface OnCancelListener {
        void onCancel(@NonNull MaterialDialogInterface dialog);
    }

    interface OnClickVerificationCodeListener {
        void onClick(@NonNull MaterialDialogInterface dialog, @NonNull String code, @Nullable String reason, @Nullable Double decimal, @Nullable Double percentage);
    }

    interface OnClickSignInListener {
        void onClick(@NonNull MaterialDialogInterface dialog, @NonNull String username, @NonNull String password);
    }

    interface OnClickListener {
        void onClick(@NonNull MaterialDialogInterface dialog, @NonNull AlertDialog.UI whichButton);
    }

    interface OnClickInputListener {
        void onClick(@NonNull MaterialDialogInterface dialog, @NonNull String content);
    }

    interface OnDismissListener {
        void onDismiss(@NonNull MaterialDialogInterface dialog);
    }

    interface OnShowListener {
        void onShow(@NonNull MaterialDialogInterface dialog);
    }

}
