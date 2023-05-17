package com.styleshow.data.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import timber.log.Timber;

public class LoginDataSource {

    private final @NonNull FirebaseAuth mAuth;

    public LoginDataSource(@NonNull FirebaseAuth auth) {
        mAuth = auth;
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
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}
