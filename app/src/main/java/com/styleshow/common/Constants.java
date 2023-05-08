package com.styleshow.common;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The number of columns in the post preview grid.
     */
    // TODO: probably change this to a resource to have more columns on larger screens
    public static final int NUMBER_OF_POST_PREVIEW_COLUMNS = 3;

    public static final int SEARCH_QUERY_DEBOUNCE_MILLIS = 350;

    // intent extras
    public static final String PROFILE_NAME = "profile";
    public static final String POST_NAME = "post";

}
