package com.styleshow.data.remote.dto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.Timestamp;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;

/**
 * DTO for {@link Post}.
 *
 * @see com.styleshow.data.remote.PostDataSource
 */
public class PostDto {

    /**
     * Has to be separately set, cannot be inferred by Firestore serialization.
     */
    public String id;

    public String uid;
    public String imageUrl;
    public @Nullable String imageId;
    public String caption;
    public String shoeUrl;
    public Timestamp postedAt;
    public List<String> likes;

    // Required no-args constructor for Firestore serialization
    public PostDto() {
    }

    public Post toPost(UserProfile author, String currentUserId) {
        boolean liked = likes.contains(currentUserId);

        return new Post(
                id,
                author,
                imageUrl,
                imageId,
                caption,
                shoeUrl,
                postedAt.toDate(),
                likes.size(),
                liked
        );
    }

    @Override
    public @NonNull String toString() {
        return "PostDto{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageId='" + imageId + '\'' +
                ", caption='" + caption + '\'' +
                ", shoeUrl='" + shoeUrl + '\'' +
                ", postedAt=" + postedAt +
                ", likes=" + likes +
                '}';
    }
}
