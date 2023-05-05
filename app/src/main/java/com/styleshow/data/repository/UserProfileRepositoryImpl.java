package com.styleshow.data.repository;

import java.util.List;
import java.util.stream.Collectors;

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
    public Task<List<UserProfile>> getAllProfiles() {
        return dataSource.getAllProfiles();
    }

    @Override
    public Task<UserProfile> getProfileForUid(String uid) {
        return dataSource.getProfileForUid(uid);
    }

    @Override
    public List<UserProfile> filterProfiles(List<UserProfile> profiles, String query) {
        return profiles.stream()
                .filter(profile -> profile.getUsername().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList())
                ;
    }
}
