package com.styleshow.data.remote;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.common.Constants;
import com.styleshow.data.remote.dto.UserProfileDto;
import timber.log.Timber;

public class LoginDataSource {

    private final @NonNull FirebaseAuth mAuth;
    private final @NonNull CollectionReference mProfiles;

    private final Executor executor = Executors.newSingleThreadExecutor();

    public LoginDataSource(@NonNull FirebaseAuth auth, @NonNull FirebaseFirestore firestore) {
        mAuth = auth;
        mProfiles = firestore.collection(Constants.UserProfile.COLLECTION_NAME);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Task<AuthResult> login(String email, String password) {
        Timber.d("Logging in as %s, with password %s", email, password);

        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        var uid = task.getResult().getUser().getUid();
                        Timber.d("signInWithEmail:success, uid: %s", uid);
                    } else {
                        // Sign in failed
                        Timber.w(task.getException(), "signInWithEmail:failure");
                    }
                })
                ;
    }

    public void logout() {
        if (getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    public Task<AuthResult> register(@NonNull String email, @NonNull String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(executor, task -> {
                    if (!task.isSuccessful())
                        return task;

                    var uid = task.getResult().getUser().getUid();

                    var dto = new UserProfileDto();
                    dto.bio = "Hello, I'm new here!";
                    dto.imageUrl = "https://picsum.photos/200";
                    dto.username = email.substring(0, email.indexOf('@'));

                    mProfiles.document(uid).set(dto)
                            .addOnSuccessListener(ignore -> {
                                Timber.d("Successfully created user profile");
                            })
                            .addOnFailureListener(e -> {
                                Timber.w(e, "Failed to create user profile");
                            });

                    return task;
                })
                .addOnSuccessListener(res -> {
                    Timber.d("Successful sign up");
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to sign up");
                })
                ;
    }
}
