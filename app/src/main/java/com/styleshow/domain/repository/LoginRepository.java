package com.styleshow.domain.repository;

import java.util.Optional;

import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.data.Result;

public interface LoginRepository {

    boolean isLoggedIn();

    void logout();

    Task<AuthResult> login(@Nullable String email, @Nullable String password);

    FirebaseUser getCurrentUser();
}
