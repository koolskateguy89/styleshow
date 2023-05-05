package com.styleshow.domain.model;

import android.net.Uri;
import androidx.annotation.NonNull;

public class UserProfile {

    /**
     * The user's ID.
     */
    private final @NonNull String uid;

    /**
     * The user's username.
     */
    private final @NonNull String username;

    /**
     * The user's bio.
     */
    private final @NonNull String bio;

    /**
     * The user's profile picture URL.
     */
    private final @NonNull Uri profilePictureUrl;

    public UserProfile(
            @NonNull String uid,
            @NonNull String username,
            @NonNull String bio,
            @NonNull Uri profilePictureUrl
    ) {
        this.uid = uid;
        this.username = username;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public @NonNull String getUid() {
        return uid;
    }

    public @NonNull String getUsername() {
        return username;
    }

    public @NonNull String getBio() {
        return bio;
    }

    public @NonNull Uri getProfilePictureUrl() {
        return profilePictureUrl;
    }

    @Override
    public @NonNull String toString() {
        return "UserProfile{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof UserProfile other) {
            return uid.equals(other.uid);
        }

        return false;
    }
}