package com.styleshow.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.UserProfileDataSource;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.UserProfileRepository;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final @NonNull UserProfileDataSource dataSource;

    public UserProfileRepositoryImpl(@NonNull UserProfileDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Task<UserProfile> getProfileForUid(@NonNull String uid) {
        return dataSource.getProfileForUid(uid);
    }

    @Override
    public Task<List<UserProfile>> searchProfiles(@NonNull String query) {
        return dataSource.searchProfiles(query);
    }

    @Override
    public Task<List<UserProfile>> getAllProfilesExceptMe() {
        return dataSource.getAllProfilesExceptMe();
    }
}
