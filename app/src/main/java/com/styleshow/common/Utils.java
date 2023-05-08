package com.styleshow.common;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.google.android.material.color.MaterialColors;

public class Utils {

    /**
     * Returns the primary color of the app, in the current theme.
     *
     * @see <a href="https://stackoverflow.com/a/64509627">source</a>
     */
    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return MaterialColors.getColor(
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
        return MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorSecondary,
                "No secondary color found"
        );
    }

}
