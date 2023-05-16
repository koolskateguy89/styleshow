package com.styleshow.domain.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a comment.
 */
public class Comment {

    private final @NonNull String id;

    private final @NonNull String authorId;

    private final @NonNull String authorUsername;

    private final @NonNull String authorImage;

    private final @NonNull String content;

    private final @NonNull Date postedAt;

    public Comment(
            @NonNull String id,
            @NonNull String authorId,
            @NonNull String authorUsername,
            @NonNull String authorImage,
            @NonNull String content,
            @NonNull Date postedAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.authorImage = authorImage;
        this.content = content;
        this.postedAt = postedAt;
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull String getAuthorId() {
        return authorId;
    }

    public @NonNull String getAuthorUsername() {
        return authorUsername;
    }

    public @NonNull String getAuthorImage() {
        return authorImage;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @NonNull Date getPostedAt() {
        return postedAt;
    }

    @Override
    public @NonNull String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", authorId='" + authorId + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", authorImage='" + authorImage + '\'' +
                ", content='" + content + '\'' +
                ", postedAt=" + postedAt +
                '}';
    }
}
