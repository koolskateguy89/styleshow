package com.styleshow.ui.login;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @StringRes
    private final @Nullable Integer emailError;
    @StringRes
    private final @Nullable Integer passwordError;
    private final boolean isDataValid;

    LoginFormState(@StringRes @Nullable Integer emailError, @StringRes @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @StringRes
    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @StringRes
    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
