package com.styleshow.common;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.google.android.material.color.MaterialColors;

public class ThemeColor {

    private ThemeColor() {
        throw new IllegalStateException("Utility class");
    }

    @ColorInt
    public static int getThemeColor(@NonNull Context context, @AttrRes int attr) {
        return getThemeColor(context, attr, "No theme color found");
    }

    @ColorInt
    public static int getThemeColor(@NonNull Context context, @AttrRes int attr, String errorMessage) {
        return MaterialColors.getColor(
                context,
                attr,
                errorMessage
        );
    }

    /**
     * Returns the primary color of the app, in the current theme.
     *
     * @see <a href="https://stackoverflow.com/a/64509627">source</a>
     */
    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getThemeColor(
                context,
                com.google.android.material.R.attr.colorPrimary,
                "No primary color found"
        );
    }

    /**
     * Returns the secondary color of the app, in the current theme.
     *
     * @see <a href="https://stackoverflow.com/a/64509627">source</a>
     */
    @ColorInt
    public static int getSecondaryColor(@NonNull Context context) {
        return getThemeColor(
                context,
                com.google.android.material.R.attr.colorSecondary,
                "No secondary color found"
        );
    }
}
