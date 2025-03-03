package com.github.leodan11.alertdialog.io.helpers;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import org.jetbrains.annotations.NotNull;

public class DisplayUtil {

    public static int dp2px(@NotNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @NotNull
    public static Point getScreenSize(@NotNull Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

}