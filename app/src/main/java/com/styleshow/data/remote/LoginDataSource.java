package com.styleshow.data.remote;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginDataSource {

    private static final String TAG = "LoginDataSource";

    private final @NonNull FirebaseAuth mAuth;

    public LoginDataSource(@NonNull FirebaseAuth auth) {
        mAuth = auth;
    }

    public @Nullable FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Task<AuthResult> login(String email, String password) {
        Log.d("LoginDataSource",
                String.format("Logging in as %s, with password %s", email, password)
        );

        var signInTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                    }
                });

        return signInTask;
    }

    public void logout() {
        if (getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

}
