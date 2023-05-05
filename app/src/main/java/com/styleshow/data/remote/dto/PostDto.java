package com.styleshow.data.remote.dto;

import java.util.List;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;

/**
 * DTO for {@link Post}.
 */
public class PostDto {

    /**
     * Has to be separately set, cannot be inferred by Firestore serialization.
     */
    public String id;
    /**
     * Has to be separately set, cannot be inferred by Firestore serialization.
     */
    public UserProfile author;

    public String uid;
    public String imageUrl;
    public String caption;
    public Timestamp postedAt;
    public List<String> likes;

    // Required no-args constructor for Firestore serialization
    public PostDto() {
    }

    public Post toPost(String currentUserId) {
        boolean liked = likes.contains(currentUserId);

        return new Post(
                id,
                author,
                Uri.parse(imageUrl),
                caption,
                postedAt,
                liked
        );
    }

    @Override
    public @NonNull String toString() {
        return "PostDto{" +
                "id='" + id + '\'' +
                ", author=" + author +
                ", imageUrl='" + imageUrl + '\'' +
                ", caption='" + caption + '\'' +
                ", postedAt=" + postedAt +
                ", likes=" + likes +
                '}';
    }
}
