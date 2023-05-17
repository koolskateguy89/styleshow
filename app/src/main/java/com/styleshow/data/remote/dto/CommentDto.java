package com.styleshow.data.remote.dto;


import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.model.UserProfile;

/**
 * DTO for {@link Comment}.
 *
 * @see com.styleshow.data.remote.CommentDataSource
 */
public class CommentDto {

    @DocumentId
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
