package com.styleshow.data.remote;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.UserProfileDto;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class UserProfileDataSource {

    private final @NonNull CollectionReference mProfiles;

    public UserProfileDataSource(@NonNull FirebaseFirestore firestore) {
        mProfiles = firestore.collection("userProfiles");
    }

    private static @Nullable UserProfile getUserProfileFromDocument(@NonNull DocumentSnapshot documentSnapshot) {
        var userProfileDto = documentSnapshot.toObject(UserProfileDto.class);

        if (userProfileDto == null) {
            return null;
        }

        userProfileDto.uid = documentSnapshot.getId();
        return userProfileDto.toUserProfile();
    }

    public Task<List<UserProfile>> getAllProfiles() {
        return mProfiles.get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(UserProfileDataSource::getUserProfileFromDocument)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                })
                ;
    }

    public Task<UserProfile> getProfileForUid(String uid) {
        return mProfiles.document(uid)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documentSnapshot = task.getResult();

                    var userProfile = getUserProfileFromDocument(documentSnapshot);

                    if (userProfile == null) {
                        Timber.w("user profile for %s is null", uid);
                    }

                    return userProfile;
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        var userProfile = task.getResult();
                        Timber.d("userProfile for %s: %s", uid, userProfile);
                    } else {
                        Timber.w(task.getException(), "failed to get userProfile for %s", uid);
                    }
                })
                ;
    }
}
