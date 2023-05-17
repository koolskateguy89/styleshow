package com.styleshow.ui.messages;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MessagesViewModel extends ViewModel {

    private final @NonNull UserProfileRepository userProfileRepository;

    private final MutableLiveData<List<UserProfile>> mUsers = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public MessagesViewModel(@NonNull UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public LiveData<List<UserProfile>> getUsers() {
        return mUsers;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    @MainThread
    public void loadUsers() {
        mLoadingState.setValue(LoadingState.LOADING);

        userProfileRepository.getAllProfilesExceptMe().addOnSuccessListener(users -> {
            mUsers.setValue(users);
            mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
        }).addOnFailureListener(e -> {
            mLoadingState.setValue(LoadingState.ERROR);
        });
    }
}
