package com.styleshow.domain.repository;

import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.UserProfile;

public interface UserProfileRepository {

    Task<UserProfile> getProfileForUid(String uid);
}
