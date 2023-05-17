package com.styleshow.data.remote.dto;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentId;
import com.styleshow.domain.model.UserProfile;

/**
 * DTO for {@link UserProfile}.
 *
 * @see com.styleshow.data.remote.UserProfileDataSource
 */
public class UserProfileDto {

    @DocumentId
    public String uid;

    public String username;
    public String bio;
    public String imageUrl;

    public UserProfile toUserProfile() {
        return new UserProfile(
                uid,
                username,
                bio,
                imageUrl
        );
    }

    @Override
    public @NonNull String toString() {
        return "UserProfileDto{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
