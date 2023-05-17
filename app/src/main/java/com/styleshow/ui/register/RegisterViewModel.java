package com.styleshow.ui.register;

import javax.inject.Inject;

import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.R;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;
    private final UserProfileRepository userProfileRepository;

    @Inject
    RegisterViewModel(LoginRepository loginRepository, UserProfileRepository userProfileRepository) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(@NonNull String email, @NonNull String password) {
        // can be launched in a separate asynchronous job
        loginRepository.register(email, password)
                .continueWithTask(task -> {
                    if (!task.isSuccessful())
                        throw task.getException();

                    var uid = loginRepository.getCurrentUser().getUid();
                    return userProfileRepository.getProfileForUid(uid);
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registerResult.setValue(new RegisterResult(task.getResult()));
                    } else {
                        registerResult.setValue(new RegisterResult(R.string.register_failed));
                    }
                })
        ;
    }

    public void registerDataChanged(@Nullable String email, @Nullable String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isEmailValid(@Nullable String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // firebase has min 6 chars
    private boolean isPasswordValid(@Nullable String password) {
        return password != null && password.trim().length() >= 6;
    }
}
