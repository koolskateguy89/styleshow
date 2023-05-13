package com.styleshow.ui.post;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class PostViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<Post> post = new MutableLiveData<>();
    private boolean originalLikeState;

    @Inject
    public PostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public LiveData<Post> getPost() {
        return post;
    }

    @MainThread
    public void setPost(Post post) {
        this.post.setValue(post);
        originalLikeState = post.isLiked();
    }

    public boolean hasPostChanged() {
        return originalLikeState != post.getValue().isLiked();
    }

    public void likeButtonClicked() {
        var post = this.post.getValue();
        boolean isLiked = post.isLiked();

        if (isLiked) {
            // Unlike
            postRepository.unlikePost(post.getId());
        } else {
            // Like
            postRepository.likePost(post.getId());
        }

        // Update the post - optimistic update
        this.post.setValue(post.withLiked(!isLiked));
    }

    public void commentButtonClicked() {
        Timber.i("comment button clicked");
        // TODO?: im not too sure what to do here
    }
}
