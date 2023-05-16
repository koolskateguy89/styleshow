package com.styleshow.data.remote.dto;


import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.model.UserProfile;

public class CommentDto {

    /**
     * Has to be separately set, cannot be inferred by Firestore serialization.
     */
    public String id;

    public String uid;
    public String content;
    public Timestamp postedAt;

    public Comment toComment(UserProfile author) {
        return new Comment(
                id,
                author.getUid(),
                author.getUsername(),
                author.getProfilePictureUrl(),
                content,
                postedAt.toDate()
        );
    }

    @Override
    public @NonNull String toString() {
        return "CommentDto{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                ", postedAt=" + postedAt +
                '}';
    }
}