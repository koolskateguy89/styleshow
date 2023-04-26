package com.styleshow.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private Object success; // TODO: replace with FirebaseUser?
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable Object success) {
        this.success = success;
    }

    @Nullable
    Object getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
