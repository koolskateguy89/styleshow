package com.styleshow.ui.login;

import javax.inject.Inject;

import android.util.Patterns;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.styleshow.R;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;
    private final UserProfileRepository userProfileRepository;

    @Inject
    LoginViewModel(LoginRepository loginRepository, UserProfileRepository userProfileRepository) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(@Nullable String email, @Nullable String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(email, password)
                .continueWithTask(task -> {
                    if (!task.isSuccessful())
                        throw task.getException();

                    var uid = loginRepository.getCurrentUser().getUid();
                    return userProfileRepository.getProfileForUid(uid);
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginResult.setValue(new LoginResult(task.getResult()));
                    } else {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                })
                ;
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

    private boolean isEmailValid(@Nullable String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // i think firebase has min 6 chars
    private boolean isPasswordValid(@Nullable String password) {
        return password != null && password.trim().length() >= 6;
    }
}
