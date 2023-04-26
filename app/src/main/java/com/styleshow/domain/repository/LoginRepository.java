package com.styleshow.domain.repository;

import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.data.Result;

public interface LoginRepository {

    boolean isLoggedIn();

    void logout();

    Result<FirebaseUser> login(@Nullable String username, @Nullable String password);
}
