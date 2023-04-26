package com.styleshow.ui.login;

import javax.inject.Inject;

import android.util.Patterns;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.R;
import com.styleshow.data.Result;
import com.styleshow.domain.repository.LoginRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    @Inject
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(@Nullable String email, @Nullable String password) {
        // can be launched in a separate asynchronous job
        Result<FirebaseUser> result = loginRepository.login(email, password);

        if (result instanceof Result.Success) {
            FirebaseUser data = ((Result.Success<FirebaseUser>) result).getData();
            //loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            //loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(@Nullable String email, @Nullable String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(@Nullable String email) {
        if (email == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(@Nullable String password) {
        // i think firebase has min 6 chars
        return password != null && password.trim().length() >= 6;
    }
}
