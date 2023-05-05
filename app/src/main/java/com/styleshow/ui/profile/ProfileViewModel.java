package com.styleshow.ui.profile;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Tasks;
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

    private final MutableLiveData<UserProfile> mUserProfile = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>();
    private final MutableLiveData<LoadingState> mLoadingState = new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public ProfileViewModel(
            LoginRepository loginRepository,
            UserProfileRepository userProfileRepository,
            PostRepository postRepository
    ) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
        this.postRepository = postRepository;
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
    }

    public void loadProfile() {
        Timber.d("loading profile");

        String uid = loginRepository.getCurrentUser().getUid();
        mLoadingState.setValue(LoadingState.LOADING);

        var profileTask = userProfileRepository.getProfileForUid(uid)
                .addOnSuccessListener(mUserProfile::setValue)
                .addOnFailureListener(e -> {
                    Timber.e(e, "error loading userProfile for uid '%s'", uid);
                });

        // Get the posts by the logged in user
        var postTask = postRepository.getPostsByUser(uid)
                .addOnSuccessListener(mPosts::setValue)
                .addOnFailureListener(e -> {
                    Timber.e(e, "error loading posts for uid '%s'", uid);
                });

        Tasks.whenAll(profileTask, postTask)
                .addOnSuccessListener(__ -> mLoadingState.setValue(LoadingState.SUCCESS_IDLE))
                .addOnFailureListener(e -> mLoadingState.setValue(LoadingState.ERROR))
        ;
    }
}
