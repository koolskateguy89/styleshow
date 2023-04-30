package com.styleshow.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.data.remote.LoginDataSource;
import com.styleshow.domain.repository.LoginRepository;

public class LoginRepositoryImpl implements LoginRepository {

    private final @NonNull LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    //private FirebaseUser user = null;

    public LoginRepositoryImpl(@NonNull LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public @Nullable FirebaseUser getCurrentUser() {
        return dataSource.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public void logout() {
        dataSource.logout();
    }

    @Override
    public Task<AuthResult> login(@Nullable String email, @Nullable String password) {
        return dataSource.login(email, password);
    }
}
