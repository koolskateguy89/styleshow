package com.styleshow.ui.profile;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.PostRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class ProfileViewModel extends ViewModel {

    private final LoginRepository loginRepository;
    private final UserProfileRepository userProfileRepository;
    private final PostRepository postRepository;

    private final MutableLiveData<String> mText;

    private final MutableLiveData<UserProfile> mUserProfile;
    private final MutableLiveData<LoadingState> mLoadingState;

    private final MutableLiveData<List<Post>> mPosts;

    @Inject
    public ProfileViewModel(
            LoginRepository loginRepository,
            UserProfileRepository userProfileRepository,
            PostRepository postRepository
    ) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
        this.postRepository = postRepository;

        var user = loginRepository.getCurrentUser();

        mText = new MutableLiveData<>();
        // mText.setValue("This is profile fragment");
        mText.setValue(String.format("""
                This is profile fragment.
                My name is %s.
                My email is %s.
                My UID is %s.
                """, user.getDisplayName(), user.getEmail(), user.getUid()));

        mUserProfile = new MutableLiveData<>();
        mLoadingState = new MutableLiveData<>(LoadingState.IDLE);
        mPosts = new MutableLiveData<>();
    }

    // TODO: remove
    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<UserProfile> getUserProfile() {
        return mUserProfile;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public void logout() {
        loginRepository.logout();

        var user = loginRepository.getCurrentUser();
        mText.setValue(String.format("""
                This is profile fragment.
                User = {%s}.
                """, user));
    }

    public void loadProfile() {
        Timber.d("loading profile");

        String uid = loginRepository.getCurrentUser().getUid();
        mLoadingState.setValue(LoadingState.LOADING);

        userProfileRepository.getProfileForUid(uid)
                .addOnSuccessListener(userProfile -> {
                    mUserProfile.setValue(userProfile);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    Timber.e(e, "error loading userProfile for uid '%s'", uid);
                    mLoadingState.setValue(LoadingState.ERROR);
                })
        ;

        // Get the posts by the logged in user
        postRepository.getPostsByUser(uid).addOnSuccessListener(mPosts::setValue);
    }
}
