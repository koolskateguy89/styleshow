package com.styleshow.ui.user_profile;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState = new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public UserProfileViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public void loadPosts(String uid) {
        Timber.d("loading posts");

        mLoadingState.setValue(LoadingState.LOADING);

        // Get the posts by user with the provided uid
        postRepository.getPostsByUser(uid)
                .addOnSuccessListener(posts -> {
                    mPosts.setValue(posts);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "error loading posts for uid '%s'", uid);
                    mLoadingState.setValue(LoadingState.ERROR);
                });
    }

    public void postUpdated(int index, Post post) {
        var posts = mPosts.getValue();
        // This should never happen
        if (posts != null)
            posts.set(index, post);
        // Updating the recycler view is handled by the view
    }
}
