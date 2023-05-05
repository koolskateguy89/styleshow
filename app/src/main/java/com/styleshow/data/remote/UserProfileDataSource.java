package com.styleshow.data.remote;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.UserProfileDto;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class UserProfileDataSource {

    private final @NonNull CollectionReference mProfiles;

    public UserProfileDataSource(@NonNull FirebaseFirestore firestore) {
        mProfiles = firestore.collection("userProfiles");
    }

    public Task<UserProfile> getProfileForUid(String uid) {
        return mProfiles.document(uid)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documentSnapshot = task.getResult();

                    var userProfileDto = documentSnapshot.toObject(UserProfileDto.class);

                    if (userProfileDto == null) {
                        Timber.w("user profile for %s is null", uid);
                        return null;
                    }

                    userProfileDto.uid = documentSnapshot.getId();

                    var userProfile = userProfileDto.toUserProfile();
                    Timber.d("userProfile for %s: %s", uid, userProfile);
                    return userProfile;
                })
                ;
    }
}
