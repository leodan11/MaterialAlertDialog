package com.github.leodan11.alertdialog.io.models;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.github.leodan11.alertdialog.io.content.Alert;

import org.jetbrains.annotations.NotNull;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class TitleAlert {

    @NotNull
    private final String title;

    @NotNull
    private final Alert.TextAlignment textAlignment;

    public TitleAlert(@NonNull String title) {
        this.title = title;
        this.textAlignment = Alert.TextAlignment.START;
    }

    public TitleAlert(@NonNull String title, @NonNull Alert.TextAlignment textAlignment) {
        this.title = title;
        this.textAlignment = textAlignment;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public Alert.TextAlignment getTextAlignment() {
        return textAlignment;
    }

}
