package com.github.leodan11.alertdialog.io.helpers;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class DisplayUtil {

    public static int dp2px(@NonNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dp(@NonNull Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    @NonNull
    public static Point getScreenSize(@NonNull Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

}