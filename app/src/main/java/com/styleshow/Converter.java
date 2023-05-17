package com.styleshow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Converter {

    private static final DateFormat df = SimpleDateFormat.getDateTimeInstance(
            SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);

    public static @NonNull String formatDate(@Nullable Date date) {
        return date == null ? "" : df.format(date);
    }
}
