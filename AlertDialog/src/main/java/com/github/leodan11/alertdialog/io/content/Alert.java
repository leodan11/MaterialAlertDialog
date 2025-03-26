package com.github.leodan11.alertdialog.io.content;

import android.os.Build;

import androidx.annotation.RequiresApi;

public abstract class Alert {

    public enum State {
        CUSTOM, DELETE, ERROR, HELP, INFORMATION, SUCCESS, WARNING, WITHOUT_INTERNET, WITHOUT_INTERNET_MOBILE, WITHOUT_INTERNET_WIFI
    }

    public enum Progress {
        CIRCULAR, LINEAR
    }

    public enum TextAlignment {

        CENTER, END, INHERIT, @RequiresApi(Build.VERSION_CODES.Q) JUSTIFY, START

    }

    public enum IconGravity {

        END, START, TEXT_END, TEXT_START, TEXT_TOP, TOP

    }

}