package com.styleshow.domain.model;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a post.
 */
public class Post implements Serializable {

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
    private final @NonNull String imageUrl;

    /**
     * The caption of the post.
     */
    private final @NonNull String caption;

    /**
     * The timestamp when the post was created.
     */
    private final @NonNull Date postedAt;

    /**
     * Whether the current user has liked the post or not.
     */
    private final boolean liked;

    public Post(
            @NonNull String id,
            @NonNull UserProfile author,
            @NonNull String imageUrl,
            @NonNull String caption,
            @NonNull Date postedAt,
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

    public @NonNull String getImageUrl() {
        return imageUrl;
    }

    public @NonNull String getCaption() {
        return caption;
    }

    public @NonNull Date getPostedAt() {
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

    public Post withLiked(boolean liked) {
        return new Post(id, author, imageUrl, caption, postedAt, liked);
    }
}
