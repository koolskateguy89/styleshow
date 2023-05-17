package com.styleshow.ui.register;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @StringRes
    private final @Nullable Integer emailError;
    @StringRes
    private final @Nullable Integer passwordError;
    private final boolean isDataValid;

    RegisterFormState(@StringRes @Nullable Integer emailError, @StringRes @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
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
