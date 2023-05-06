package com.styleshow.ui.user_profile;

import java.util.List;
import java.util.concurrent.Executors;

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

    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>();
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

        var executor = Executors.newSingleThreadExecutor();

        mLoadingState.observeForever(state -> {
            Timber.d("Loading state: %s", state);
        });

        // Get the posts by user with the provided uid
        postRepository.getPostsByUser(uid)
                .addOnSuccessListener(executor, posts -> {
                    Timber.d("Start");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Timber.d("End");

                    mPosts.postValue(posts);
                    mLoadingState.postValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnCompleteListener(__ -> {
                    executor.shutdown();
                })
                .addOnFailureListener(e -> {
                    Timber.e(e, "error loading posts for uid '%s'", uid);
                    mLoadingState.setValue(LoadingState.ERROR);
                });
    }
}
