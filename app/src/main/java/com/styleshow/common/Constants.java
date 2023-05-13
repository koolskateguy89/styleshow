package com.styleshow.common;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int SEARCH_QUERY_DEBOUNCE_MILLIS = 350;

    // intent extras
    public static final String PROFILE_NAME = "profile";
    public static final String POST_NAME = "post";
    public static final String POST_INDEX_NAME = "post_index";

}
