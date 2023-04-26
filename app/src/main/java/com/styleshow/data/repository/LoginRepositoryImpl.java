package com.styleshow.data.repository;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.data.Result;
import com.styleshow.data.remote.LoginDataSource;
import com.styleshow.domain.repository.LoginRepository;

// TODO: store logged in user
public class LoginRepositoryImpl implements LoginRepository {

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private FirebaseUser user = null;

    public LoginRepositoryImpl(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        // TODO
    }

    public Result<FirebaseUser> login(@Nullable String username, @Nullable String password) {
        // TODO
        return new Result.Success<>(null);
    }
}
