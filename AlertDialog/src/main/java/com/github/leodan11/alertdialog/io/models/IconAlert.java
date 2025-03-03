package com.github.leodan11.alertdialog.io.models;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class IconAlert {

    @Nullable
    private Drawable drawable;

    @Nullable
    @DrawableRes
    private Integer drawableResId;

    public IconAlert(@Nullable Drawable drawable) {
        this.drawable = drawable;
    }

    public IconAlert(@Nullable @DrawableRes Integer drawableResId) {
        this.drawableResId = drawableResId;
    }

    @Nullable
    public Drawable getDrawable() {
        return drawable;
    }

    @Nullable
    public Integer getDrawableResId() {
        return drawableResId;
    }

}
