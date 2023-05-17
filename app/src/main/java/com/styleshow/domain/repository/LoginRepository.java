package com.styleshow.domain.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public interface LoginRepository {

    boolean isLoggedIn();

    void logout();

    Task<AuthResult> login(@Nullable String email, @Nullable String password);

    FirebaseUser getCurrentUser();

    Task<AuthResult> register(@NonNull String email, @NonNull String password);
}
