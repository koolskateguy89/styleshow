package com.styleshow.data.repository;

import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.UserProfileDataSource;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.UserProfileRepository;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final UserProfileDataSource dataSource;

    public UserProfileRepositoryImpl(UserProfileDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Task<UserProfile> getProfileForUid(String uid) {
        return dataSource.getProfileForUid(uid);
    }
}
