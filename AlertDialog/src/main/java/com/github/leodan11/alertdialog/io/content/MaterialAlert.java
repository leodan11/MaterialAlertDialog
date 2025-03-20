package com.github.leodan11.alertdialog.io.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface MaterialAlert {

    void cancel();

    void dismiss();

    interface OnCancelListener {
        void onCancel(@NonNull MaterialAlert dialog);
    }

    interface OnClickVerificationCodeListener {
        void onClick(@NonNull MaterialAlert dialog, @NonNull String code, @Nullable String reason, @Nullable String firstInput, @Nullable String secondInput);
    }

    interface OnClickSignInListener {
        void onClick(@NonNull MaterialAlert dialog, @NonNull String username, @NonNull String password);
    }

    interface OnClickListener {
        void onClick(@NonNull MaterialAlert dialog, @NonNull AlertDialog.UI whichButton);
    }

    interface OnClickInputListener {
        void onClick(@NonNull MaterialAlert dialog, @NonNull String content);
    }

    interface OnDismissListener {
        void onDismiss(@NonNull MaterialAlert dialog);
    }

    interface OnShowListener {
        void onShow(@NonNull MaterialAlert dialog);
    }

}
