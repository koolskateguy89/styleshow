package com.styleshow.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.styleshow.domain.model.UserProfile;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {

    private @Nullable UserProfile success;
    @StringRes
    private @Nullable Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable UserProfile success) {
        this.success = success;
    }

    @Override
    public @NonNull String toString() {
        return "LoginResult{" +
                "success=" + success +
                ", error=" + error +
                '}';
    }

    @Nullable
    UserProfile getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
