package com.styleshow.domain.model;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;

public class Post {

    /**
     * The ID of the post.
     */
    private final @NonNull String id;

    /**
     * The author of the post.
     */
    private final @NonNull UserProfile author;

    /**
     * The URL of the post's image.
     */
    private final @NonNull Uri imageUrl;

    /**
     * The caption of the post.
     */
    private final @NonNull String caption;

    /**
     * The timestamp when the post was created.
     */
    private final @NonNull Timestamp postedAt;

    /**
     * Whether the current user has liked the post or not.
     */
    private final boolean liked;

    public Post(
            @NonNull String id,
            @NonNull UserProfile author,
            @NonNull Uri imageUrl,
            @NonNull String caption,
            @NonNull Timestamp postedAt,
            boolean liked
    ) {
        this.id = id;
        this.author = author;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.postedAt = postedAt;
        this.liked = liked;
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull UserProfile getAuthor() {
        return author;
    }

    public @NonNull Uri getImageUrl() {
        return imageUrl;
    }

    public @NonNull String getCaption() {
        return caption;
    }

    public @NonNull Timestamp getPostedAt() {
        return postedAt;
    }

    public boolean isLiked() {
        return liked;
    }

    @Override
    public @NonNull String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", author=" + author +
                ", imageUrl=" + imageUrl +
                ", caption='" + caption + '\'' +
                ", postedAt=" + postedAt +
                ", liked=" + liked +
                '}';
    }
}
