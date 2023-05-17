package com.styleshow.common;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int SEARCH_QUERY_DEBOUNCE_MILLIS = 350;

    // intent extras
    public static final String NAME_PROFILE = "profile";
    public static final String NAME_POST = "post";
    public static final String NAME_POST_INDEX = "post_index";

    // TODO: all firestore collection names
    public static final String COLLECTION_CHATS = "chats";
}
