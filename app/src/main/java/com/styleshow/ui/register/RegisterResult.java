package com.styleshow.ui.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.styleshow.domain.model.UserProfile;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {

    private @Nullable UserProfile success;
    @StringRes
    private @Nullable Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable UserProfile success) {
        this.success = success;
    }

    @Override
    public @NonNull String toString() {
        return "RegisterResult{" +
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
