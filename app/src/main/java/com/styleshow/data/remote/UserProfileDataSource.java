package com.styleshow.data.remote;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.UserProfileDto;
import com.styleshow.domain.model.UserProfile;

public class UserProfileDataSource {

    private static final String TAG = "UserProfileDataSource";

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

                    // seems to be non-null even if id doesn't exist
                    var documentSnapshot = task.getResult();

                    var userProfileDto = documentSnapshot.toObject(UserProfileDto.class);

                    if (userProfileDto == null) {
                        Log.w(TAG, String.format("user profile for %s is null", uid));
                        return null;
                    }

                    userProfileDto.uid = documentSnapshot.getId();
                    Log.d(TAG, "userProfileDto for " + uid + ": " + userProfileDto);

                    var userProfile =  userProfileDto.toUserProfile();
                    Log.d(TAG, "userProfile for " + uid + ": " + userProfile);
                    return userProfile;
                })
                ;
    }

}
