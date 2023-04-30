package com.styleshow.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    private boolean success = false;
    @StringRes
    private @Nullable Integer error;

    @Override
    public String toString() {
        return "LoginResult{" +
                "success=" + success +
                ", error=" + error +
                '}';
    }

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(boolean success) {
        this.success = success;
    }

    boolean getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
