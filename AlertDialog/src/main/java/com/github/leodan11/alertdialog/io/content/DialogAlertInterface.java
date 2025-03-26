package com.github.leodan11.alertdialog.io.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface DialogAlertInterface {

    void cancel();

    void dismiss();

    enum UI {BUTTON_POSITIVE, BUTTON_NEUTRAL, BUTTON_NEGATIVE}

    interface OnCancelListener {
        void onCancel(@NonNull DialogAlertInterface dialog);
    }

    interface OnClickVerificationCodeListener {
        void onClick(@NonNull DialogAlertInterface dialog, @NonNull String code, @Nullable String reason, @Nullable String firstInput, @Nullable String secondInput);
    }

    interface OnClickSignInListener {
        void onClick(@NonNull DialogAlertInterface dialog, @NonNull String username, @NonNull String password);
    }

    interface OnClickListener {
        void onClick(@NonNull DialogAlertInterface dialog, @NonNull UI whichButton);
    }

    interface OnClickInputListener {
        void onClick(@NonNull DialogAlertInterface dialog, @NonNull String content);
    }

    interface OnDismissListener {
        void onDismiss(@NonNull DialogAlertInterface dialog);
    }

    interface OnShowListener {
        void onShow(@NonNull DialogAlertInterface dialog);
    }

}
