package com.github.leodan11.alertdialog.io.content;

import android.view.View;

public abstract class AlertDialog {

    public enum State {
        CUSTOM, DELETE, ERROR, HELP, INFORMATION, SUCCESS, WARNING, WITHOUT_INTERNET, WITHOUT_INTERNET_MOBILE, WITHOUT_INTERNET_WIFI
    }

    public enum Input {
        DECIMAL_NUMBER, NONE, PERCENTAGE
    }

    public enum Progress {
        CIRCULAR, LINEAR
    }

    public enum TextAlignment {

        CENTER(View.TEXT_ALIGNMENT_CENTER),
        END(View.TEXT_ALIGNMENT_TEXT_END),
        START(View.TEXT_ALIGNMENT_TEXT_START);

        private final int alignment;

        TextAlignment(int alignment) {
            this.alignment = alignment;
        }

        public int getAlignment() {
            return alignment;
        }

    }

    public enum UI {
        BUTTON_POSITIVE, BUTTON_NEUTRAL, BUTTON_NEGATIVE
    }

}