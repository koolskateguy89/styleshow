package com.styleshow.data.remote.dto;

import androidx.annotation.NonNull;
import com.styleshow.domain.model.UserProfile;

/**
 * DTO for {@link UserProfile}.
 */
public class UserProfileDto {

    /**
     * Has to be separately set, cannot be inferred by Firestore serialization.
     */
    public String uid;

    public String username;
    public String bio;
    public String imageUrl;

    // Required no-args constructor for Firestore serialization
    public UserProfileDto() {
    }

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
