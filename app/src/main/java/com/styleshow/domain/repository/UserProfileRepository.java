package com.styleshow.domain.repository;

import java.util.List;

import com.google.android.gms.tasks.Task;
import com.styleshow.data.DatabaseContract;
import com.styleshow.domain.model.UserProfile;

public interface UserProfileRepository {

    Task<List<UserProfile>> getAllProfiles();

    Task<UserProfile> getProfileForUid(String uid);

    List<UserProfile> filterProfiles(List<UserProfile> profiles, String query);
}
