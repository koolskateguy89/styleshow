package com.styleshow.data.remote;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.UserProfileDto;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class UserProfileDataSource {

    private final @NonNull CollectionReference mProfiles;

    public UserProfileDataSource(@NonNull FirebaseFirestore firestore) {
        mProfiles = firestore.collection("userProfiles");
    }

    /**
     * Parse a {@link DocumentSnapshot} into a {@link UserProfile}.
     */
    private static @Nullable UserProfile getUserProfileFromDocument(@NonNull DocumentSnapshot documentSnapshot) {
        var userProfileDto = documentSnapshot.toObject(UserProfileDto.class);

        if (userProfileDto == null) {
            return null;
        }

        userProfileDto.uid = documentSnapshot.getId();
        return userProfileDto.toUserProfile();
    }

    /**
     * Get the profile for the user with the provided UID.
     */
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

    /**
     * Firestore doesn't support full text search, so we have to do a prefix search
     *
     * @see <a href="https://stackoverflow.com/q/46568142">https://stackoverflow.com/q/46568142</a>
     */
    public Task<List<UserProfile>> searchProfiles(@NonNull String query) {
        return mProfiles
                .orderBy("username")
                .limit(10)
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(UserProfileDataSource::getUserProfileFromDocument)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to search profiles for query '%s'", query);
                })
                ;
    }

    /**
     * Get all profiles with the provided UIDs.
     */
    public Task<List<UserProfile>> getProfilesForUids(@NonNull List<String> uids) {
        return mProfiles
                .whereIn(FieldPath.documentId(), uids)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(UserProfileDataSource::getUserProfileFromDocument)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                })
                .addOnSuccessListener(userProfiles -> {
                    Timber.d("got profiles for uids %s: %s", uids, userProfiles);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to get profiles for uids '%s'", uids);
                })
                ;
    }
}
