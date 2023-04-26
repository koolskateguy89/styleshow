package com.styleshow.data;

// Idk if this is in correct package

import android.net.Uri;

public class ContentProvider {
    public static final String CONTENT_AUTHORITY = "com.styleshow";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final String CONTENT_TYPE_DIR =
            String.format("vnd.android.cursor.dir/%s/%s", CONTENT_AUTHORITY, PATH_MOVIE);
    public static final String CONTENT_TYPE_ITEM =
            String.format("vnd.android.cursor.item/%s/%s", CONTENT_AUTHORITY, PATH_MOVIE);
}
