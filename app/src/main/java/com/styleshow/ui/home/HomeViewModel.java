package com.styleshow.ui.home;

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
public class HomeViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public HomeViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public void loadPosts() {
        Timber.i("Loading posts");

        mLoadingState.setValue(LoadingState.LOADING);

        postRepository.getAllPosts()
                .addOnSuccessListener(posts -> {
                    mPosts.setValue(posts);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                    Timber.d("Loaded %d posts", posts.size());
                })
                .addOnFailureListener(e -> {
                    mLoadingState.setValue(LoadingState.ERROR);
                });
    }
}
