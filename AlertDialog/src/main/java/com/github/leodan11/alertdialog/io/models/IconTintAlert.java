package com.github.leodan11.alertdialog.io.models;

import android.content.res.ColorStateList;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class IconTintAlert {

    @Nullable
    @ColorInt
    private Integer tintColorInt;

    @Nullable
    @ColorRes
    private Integer tintColorRes;

    @Nullable
    private ColorStateList tintColorStateList;

    public IconTintAlert() {

    }

    public IconTintAlert(@Nullable @ColorRes Integer tintColorRes) {
        this.tintColorRes = tintColorRes;
    }

    public IconTintAlert(@Nullable ColorStateList tintColorStateList) {
        this.tintColorStateList = tintColorStateList;
    }

    public void setTintColorInt(@Nullable Integer tintColorInt) {
        this.tintColorInt = tintColorInt;
    }

    @Nullable
    public Integer getTintColorInt() {
        return tintColorInt;
    }

    @Nullable
    public Integer getTintColorRes() {
        return tintColorRes;
    }

    @Nullable
    public ColorStateList getTintColorStateList() {
        return tintColorStateList;
    }

}
