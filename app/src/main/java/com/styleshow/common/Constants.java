package com.styleshow.common;

public class Constants {

    public static final int SEARCH_QUERY_DEBOUNCE_MILLIS = 350;

    // intent extras
    public static final String NAME_PROFILE = "profile";
    public static final String NAME_POST = "post";
    public static final String NAME_POST_INDEX = "post_index";

    /**
     * @see com.styleshow.data.remote.UserProfileDataSource
     */
    public static class UserProfile {

        public static final String COLLECTION_NAME = "userProfiles";

        /**
         * {@link String}
         */
        public static final String FIELD_USERNAME = "username";
        /**
         * {@link String}
         */
        public static final String FIELD_BIO = "bio";
        /**
         * {@link String}
         */
        public static final String FIELD_IMAGE_URL = "imageUrl";
    }

    /**
     * @see com.styleshow.data.remote.PostDataSource
     */
    public static class Post {

        public static final String COLLECTION_NAME = "posts";
        public static final String IMAGES_STORAGE_PATH = "postImages";

        /**
         * {@link String}
         */
        public static final String FIELD_AUTHOR_UID = "uid";
        /**
         * {@link String}
         */
        public static final String FIELD_IMAGE_URL = "imageUrl";
        /**
         * {@link String}
         */
        public static final String FIELD_IMAGE_ID = "imageId";
        /**
         * {@link String}
         */
        public static final String FIELD_CAPTION = "caption";
        /**
         * {@link String String?}
         */
        public static final String FIELD_SHOE_URL = "shoeUrl";
        /**
         * {@link com.google.firebase.Timestamp}
         */
        public static final String FIELD_POSTED_AT = "postedAt";
        /**
         * {@link java.util.List<String>}
         */
        public static final String FIELD_LIKES = "likes";
    }

    /**
     * @see com.styleshow.data.remote.CommentDataSource
     */
    public static class Comment {

        public static final String COLLECTION_NAME = "comments";

        /**
         * {@link String}
         */
        public static final String FIELD_POSTER_UID = "uid";
        /**
         * {@link String}
         */
        public static final String FIELD_CONTENT = "content";
        /**
         * {@link com.google.firebase.Timestamp}
         */
        public static final String FIELD_POSTED_AT = "postedAt";
    }

    /**
     * @see com.styleshow.data.remote.ChatDataSource
     */
    public static class Chat {

        public static final String COLLECTION_NAME = "chats";

        /**
         * {@link String}
         */
        public static final String FIELD_SENDER_UID = "senderUid";
        /**
         * {@link String}
         */
        public static final String FIELD_RECEIVER_UID = "receiverUid";
        /**
         * {@link String}
         */
        public static final String FIELD_CONTENT = "content";
        /**
         * {@link com.google.firebase.Timestamp}
         */
        public static final String FIELD_SENT_AT = "sentAt";
    }
}
