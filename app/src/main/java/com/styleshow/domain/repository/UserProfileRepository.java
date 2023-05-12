package com.styleshow.domain.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.DatabaseContract;
import com.styleshow.domain.model.UserProfile;

public interface UserProfileRepository {

    Task<UserProfile> getProfileForUid(@NonNull String uid);

    Task<List<UserProfile>> searchProfiles(@NonNull String query);
}
