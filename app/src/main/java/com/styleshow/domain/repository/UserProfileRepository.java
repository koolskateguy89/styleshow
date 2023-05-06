package com.styleshow.domain.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.DatabaseContract;
import com.styleshow.domain.model.UserProfile;

public interface UserProfileRepository {

    Task<List<UserProfile>> getAllProfiles();

    Task<UserProfile> getProfileForUid(@NonNull String uid);

    List<UserProfile> filterProfiles(@NonNull List<UserProfile> profiles, @NonNull String query);
}
