package com.styleshow.ui.profile;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.domain.repository.LoginRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final LoginRepository loginRepository;

    @Inject
    public ProfileViewModel(@NonNull LoginRepository loginRepository) {
        var user = loginRepository.getCurrentUser();

        mText = new MutableLiveData<>();
        //mText.setValue("This is profile fragment");
        mText.setValue(String.format("""
                This is profile fragment.
                My name is %s.
                My email is %s.
                """, user.getDisplayName(), user.getEmail()));

        this.loginRepository = loginRepository;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void logout() {
        loginRepository.logout();

        var user = loginRepository.getCurrentUser();
        mText.setValue(String.format("""
                This is profile fragment.
                User = {%s}.
                """, user));
    }
}
