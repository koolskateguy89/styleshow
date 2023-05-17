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

    private final MutableLiveData<UserProfile> mUserProfile = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

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

        // Get the profile of the logged in user
        var profileTask = userProfileRepository.getProfileForUid(uid)
                .addOnSuccessListener(mUserProfile::setValue)
                .addOnFailureListener(e -> {
                    Timber.w(e, "error loading userProfile for uid '%s'", uid);
                });

        var finalTask = profileTask.continueWithTask(profTask -> {
            if (!profTask.isSuccessful())
                return null;

            var profile = profTask.getResult();

            // Get the posts by the logged in user
            return postRepository.getPostsByUser(profile)
                    .addOnSuccessListener(mPosts::setValue)
                    .addOnFailureListener(e -> {
                        Timber.w(e, "error loading posts for uid '%s'", uid);
                    });
        });

        finalTask.addOnSuccessListener(task -> mLoadingState.setValue(LoadingState.SUCCESS_IDLE))
                .addOnFailureListener(e -> mLoadingState.setValue(LoadingState.ERROR));
    }

    public void postUpdated(int index, Post post) {
        var posts = mPosts.getValue();
        // This should never not happen
        if (posts != null)
            posts.set(index, post);
        // Updating the recycler view is handled by the view
    }

    public void postDeleted(int index) {
        var posts = mPosts.getValue();
        // This should never not happen
        if (posts != null)
            posts.remove(index);
        // Updating the recycler view is handled by the view
    }
}
